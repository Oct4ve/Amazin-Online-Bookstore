// This controls the front end display.

package com.amazin.amazinonlinebookstore;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@SessionAttributes("user") // it'll store the user into a session!
public class ThymeLeafController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    // Home page mapping to display all books (or a welcome message)
    @GetMapping("/")
    public String homePage(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("books", bookRepository.findAll()); // Adds all books to the model

        // Displays who is logged in (if nobody it says Guest)
        if (user != null) {
            model.addAttribute("currentUser", user.getUsername()); // Passes the username to the view
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
    public String addBookToCart(@RequestParam("bookId") Long bookId, HttpSession session) {
        User user = getUserFromSession(session);
        Book bookToAdd = bookRepository.findByid(bookId);

        if (bookToAdd != null && !user.getCart().getCartBooks().contains(bookToAdd)) {
            // Add the book to the cart only if it's not already in the cart
            user.addToUserCart(bookToAdd);
            userRepository.save(user);
        } else {
            // Handle the case where the book is already in the cart or doesn't exist
            // I gotta put something here to display the error message on the page
            System.out.println("Book is already in the cart or not found.");
        }

        return "redirect:/cart";
    }

    @PostMapping("/removeFromCart")
    public String removeBookFromCart(@RequestParam("bookId") Long bookId, HttpSession session) {
        User user = getUserFromSession(session);
        user.removeFromUserCart(bookRepository.findByid(bookId));
        userRepository.save(user);
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
                          @RequestParam(value = "coverFile", required = false) MultipartFile coverFile, Model model) {

        // checks if the title already exists
        if (bookRepository.findByTitle(book.getTitle()) != null) {
            model.addAttribute("errorMessage", "A book with this title already exists. Please choose a different title.");
            model.addAttribute("book", book); // Retain the input values in the form
            return "add_book"; // Return to the form with an error message
        }

        bookRepository.save(book); // Save the book to the database
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
    public String removeBook(@RequestParam("title") String title, Model model) {
        Book book = bookRepository.findByTitle(title);

        if (book != null) {
            bookRepository.delete(book);
            model.addAttribute("message", "Book with title '" + title + "' was successfully deleted.");
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
}

