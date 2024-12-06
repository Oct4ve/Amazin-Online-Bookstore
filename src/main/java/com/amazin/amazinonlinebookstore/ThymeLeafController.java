// This controls the front end display.

package com.amazin.amazinonlinebookstore;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Controller
@SessionAttributes("user") // it'll store the user into a session!
public class ThymeLeafController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    // Home page mapping to display all books (or a welcome message)
    @GetMapping("/")
    public String homePage(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("books", bookRepository.findAll()); // Adds all books to the model

        model.addAttribute("owner", "expectedOwnerUsername");

        // Displays who is logged in (if nobody it says Guest)
        if (user != null) {
            String username = user.getUsername();
            model.addAttribute("currentUser", username); // Passes the username to the view
        } else {
            model.addAttribute("currentUser", "Guest"); // Default value if no user is logged in
        }

        return "home"; // returns the home.html template
    }

    // shows the login page
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    // Logs user in if username and password are correct. If not, redirects back to the login page.
    @PostMapping("/login")
    public String loginAction(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session, Model model) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Username not found. Please try again.");
            return "login"; // Redirect back to login with an error message
        }

        if (!Objects.equals(password, user.getPassword())) {
            model.addAttribute("error", "Incorrect password. Please try again.");
            return "login"; // Redirect back to login with an error message
        }

        if (user.getCart() == null) {
            ShoppingCart cart = new ShoppingCart(user);
            user.setCart(cart);
            userRepository.save(user);
        }

        // If login is successful, add user to session
        session.setAttribute("user", user);
        return "redirect:/"; // Redirect to the home page
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // Render the signup form
    }

    @PostMapping("/signup")
    public String signupAction(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        // Check if the user already exists
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "signup"; // Redirect back to signup with an error
        }

        // Create a new user and save it to the repository
        User newUser = new User(username, password);
        userRepository.save(newUser);

        model.addAttribute("success", "Account created successfully! You can now log in.");
        return "redirect:/login"; // Redirect to the login page
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Save the user to persist the cart before clearing the session
            userRepository.save(user);
        }
        session.invalidate();
        sessionStatus.setComplete();
        return "redirect:/";
    }


    // Gets current user
    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // Displays all books
    @GetMapping("/{id}/view")
    public String viewBooks(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book with ID " + id + " not found"));
        model.addAttribute("Book", book);
        return "book_view";
    }

    // This should only be possible to reach if logged in.
    @GetMapping("/cart")
    public String viewCart(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("userBooks", user.getCart().getCartBooks());
        model.addAttribute("currentUser", user.getUsername()); // Passes the username to the view
        return "view_cart";
    }

    // Adds a book to the user's cart
    @PostMapping("/addToCart")
    public String addBookToCart(@RequestParam("bookId") Long bookId, @RequestParam("quantity") int quantity, HttpSession session, Model model) {
        User user = getUserFromSession(session);
        if (user == null || user.getCart() == null) {
            model.addAttribute("message", "Error: User or cart not found.");
            return "redirect:/";
        }

        Book bookToAdd = bookRepository.findByid(bookId);
        if (bookToAdd == null) {
            model.addAttribute("message", "Error: Book not found.");
            return "redirect:/cart";
        }

        // Add or update the book in the cart
        String result = user.getCart().addToCart(bookToAdd, quantity);
        if (result.startsWith("Error:")) {
            model.addAttribute("message", result);
            return "redirect:/cart";
        }

        userRepository.save(user);

        return "redirect:/cart";
    }


    @PostMapping("/removeFromCart")
    public String removeBookFromCart(@RequestParam("bookId") Long bookId, @RequestParam("quantity") int quantity, HttpSession session, Model model) {
        User user = getUserFromSession(session);
        Book bookToRemove = bookRepository.findByid(bookId);

        if (bookToRemove != null) {
            String result = user.getCart().removeFromCart(bookToRemove, quantity);

            if (result.startsWith("Error:")) {
                model.addAttribute("message", result);
                return "cart";
            }

            userRepository.save(user);
        } else {
            model.addAttribute("message", "Error: Book not found.");
            return "cart";
        }

        return "redirect:/cart";
    }

    // Get mapping for "Add Book" page
    @GetMapping("/add_book")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book()); // Empty Book object to bind the form fields
        return "add_book"; // Returns the add_book.html template
    }

    // Post mapping for "Add Book" page
    @PostMapping("/add_book")
    public String addBook(@ModelAttribute Book book,
                          @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
                          Model model) {
        // Check if a book with the same title already exists
        Book existingBook = bookRepository.findByTitle(book.getTitle());
        if (existingBook != null) {
            model.addAttribute("errorMessage", "A book with this title already exists.");
            return "add_book"; // Return to the same page with an error message
        }

        if (coverFile != null && !coverFile.isEmpty()) {
            try {
                // Get the absolute path to the static directory
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);

                // Ensure the directory exists
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Define the file path (use the original file name)
                String fileName = coverFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Save the file
                Files.write(filePath, coverFile.getBytes());

                // Set the relative URL for the image
                book.setCoverImagePath("/images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Failed to upload the cover image.");
                return "add_book";
            }
        }

        // Save the book to the database
        bookRepository.save(book);
        return "redirect:/"; // Redirect to home page after saving
    }


    // Contact Info page
    @GetMapping("/contact")
    public String showContactPage() {
        return "contact_info";
    }

    // Displays remove book page
    @GetMapping("/remove_book")
    public String showRemoveBookForm(Model model) {
        return "remove_book";
    }

    // Removes book from inventory
    // This should remove the book from all carts as well.
    @PostMapping("/remove_book")
    public String removeBook(@RequestParam("title") String title, @RequestParam("quantity") int quantity, Model model) {
        Book book = bookRepository.findByTitle(title);

        if (book != null) {
            int newStockQuantity = book.getStockQuantity() - quantity;

            if (newStockQuantity < 0) {
                model.addAttribute("message", "Error: no stock found");
            } else {
                book.setStockQuantity(newStockQuantity);
                bookRepository.save(book);
                model.addAttribute("message", "Removed " + quantity + " book(s) with title " + title + ".");
            }
        } else {
            model.addAttribute("message", "Error: Book with title '" + title + "' not found.");
        }

        return "remove_book";
    }

    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }

    @GetMapping("/sort_books")
    public String sortBooks(){
        return "sort_books";
    }

    @GetMapping("/sort_by_author")
    public String sortByAuthor(Model model) {
        List<Book> books = (List<Book>) bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getAuthor));
        model.addAttribute("books", books);
        return "sort_by_author";
    }
    @GetMapping("/sort_by_title")
    public String sortByTitle(Model model) {
        List<Book> books = (List<Book>) bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getTitle));
        model.addAttribute("books", books);
        return "sort_by_title";
    }
    @GetMapping("/sort_by_price_low")
    public String sortByPriceLow(Model model) {
        List<Book> books = (List<Book>) bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getPrice));
        model.addAttribute("books", books);
        return "sort_by_price_low";
    }
    @GetMapping("/sort_by_price_high")
    public String sortByPriceHigh(Model model) {
        List<Book> books = (List<Book>) bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getPrice).reversed());
        model.addAttribute("books", books);
        return "sort_by_price_high";
    }
    @GetMapping("/sort_by_date_old")
    public String sortByDateOldestFirst(Model model) {
        List<Book> books = (List<Book>) bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getPublishDate));
        model.addAttribute("books", books);
        return "sort_by_date_old";
    }
    @GetMapping("/sort_by_date_new")
    public String sortByDateNewestFirst(Model model) {
        List<Book> books = (List<Book>) bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getPublishDate).reversed());
        model.addAttribute("books", books);
        return "sort_by_date_new";
    }

    // Checkout page
    @GetMapping("/checkout")
    public String checkoutPage(Model model, @ModelAttribute("user") User user) {
        if (user == null || user.getCart() == null) {
            model.addAttribute("userBooks", List.of()); // Empty cart
            model.addAttribute("total", 0.0);
            return "checkout";
        }

        List<Book> userBooks = user.getCart().getCartBooks();
        double total = user.getCart().calculateTotal();

        model.addAttribute("userBooks", userBooks);
        model.addAttribute("total", total);
        model.addAttribute("currentUser", user.getUsername());
        return "checkout";
    }

    // Handle checkout
    @PostMapping("/checkout")
    public String completePurchase(@ModelAttribute("user") User user, Model model) {
        if (user != null && user.getCart() != null) {
            List<Book> userBooks = user.getCart().getCartBooks();

            for (Book book : userBooks) {
                Book bookInRepository = bookRepository.findByid(book.getId());
                if (bookInRepository != null) {
                    // Check if there's enough stock for the book
                    if (bookInRepository.getStockQuantity() < book.getCartQuantity()) {
                        model.addAttribute("message", "Error: Not enough stock for '" + book.getTitle() + "'.");
                        return "redirect:/cart";
                    }

                    // Decrease stock based on cart quantity
                    bookInRepository.setStockQuantity(
                            bookInRepository.getStockQuantity() - book.getCartQuantity()
                    );
                    bookRepository.save(bookInRepository);
                }
            }

            double total = user.getCart().calculateTotal();

            // Clear the user's cart
            user.getCart().emptyCart();
            userRepository.save(user); // Save updated user

            model.addAttribute("purchasedBooks", userBooks);
            model.addAttribute("total", total);
            model.addAttribute("message", "Purchase completed successfully!");
        } else {
            model.addAttribute("message", "Error: Cart is empty or user not found.");
        }

        return "purchased"; // Redirect to a checkout confirmation page
    }


    @PostMapping("/purchase")
    public String purchaseBooks(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || user.getCart() == null || user.getCart().getCartBooks().isEmpty()) {
            model.addAttribute("message", "Your cart is empty. Add books to make a purchase.");
            return "redirect:/cart"; // Redirect to cart if empty
        }

        List<Book> purchasedBooks = new ArrayList<>(user.getCart().getCartBooks());

        for (Book book : purchasedBooks) {
            Book bookInRepository = bookRepository.findByid(book.getId());
            if (bookInRepository != null) {
                // Validate stock availability
                if (bookInRepository.getStockQuantity() < book.getCartQuantity()) {
                    model.addAttribute("message", "Error: Not enough stock for '" + book.getTitle() + "'.");
                    return "redirect:/cart";
                }

                // Decrease stock
                bookInRepository.setStockQuantity(
                        bookInRepository.getStockQuantity() - book.getCartQuantity()
                );
                bookRepository.save(bookInRepository);
            }
        }

        double total = user.getCart().calculateTotal();

        saveCart(purchasedBooks, user, LocalDate.now());

        // Clear the user's cart after purchase
        user.getCart().emptyCart();
        userRepository.save(user); // Save updated user

        model.addAttribute("purchasedBooks", purchasedBooks);
        model.addAttribute("total", total);

        return "purchased"; // Redirect to the purchased confirmation page
    }

    @GetMapping("/previous_purchases")
    public String viewPreviousPurchases(Model model, @ModelAttribute("user") User user) {
        if (!purchaseRepository.findAllByUser(user).isEmpty()){
            model.addAttribute("purchases", purchaseRepository.findAllByUser(user));
        }
        return "previous_purchases";
    }

    @GetMapping("/search_book")
    public String showSearchBookForm(Model model) {
        return "search_book";
    }

    @PostMapping("/search_book")
    public String searchBook(@RequestParam("title") String title, Model model) {
        Book book = bookRepository.findByTitle(title);

        if (book != null) {
            model.addAttribute("Book", book);
            return "book_view";
        } else {
            model.addAttribute("message", "Error: Book with title '" + title + "' not found.");
        }

        return "search_book";
    }

    // Saves a cart to the purchaseRepository
    public void saveCart(List<Book> cart, User user, LocalDate date){
        PreviousPurchase purchase = new PreviousPurchase(user, date);
        purchase.setBooks(cart);
        purchaseRepository.save(purchase);
    }

    @GetMapping("/book_recommendations")
    public String showRecommendations(Model model) {return "book_recommendations";}

}

