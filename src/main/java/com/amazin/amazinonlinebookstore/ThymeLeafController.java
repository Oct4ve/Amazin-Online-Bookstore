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

import java.util.NoSuchElementException;

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

        if (user != null) {
            model.addAttribute("currentUser", user.getUsername()); // Passes the username to the view
        } else {
            model.addAttribute("currentUser", "Guest"); // Default value if no user is logged in
        }

        return "home"; // returns the home.html template
    }

    // shows the login page
    @GetMapping("/login")
    public String loginPage(Model model){
        return "login";
    }

    // Gets the username and password of user and assigns the user to the current session
    @PostMapping("/login")
    public String loginAction(@RequestParam ("username") String username,
                              @RequestParam ("password") String password,
                              HttpSession session, Model model){
        User user = null;
        if (userRepository.findByUsername(username) == null){
            user = new User(username, password);
            userRepository.save(user);
        } else {
            user = userRepository.findByUsername(username);
        }

        session.setAttribute("user", user);
        return "redirect:/";
    }

    @ModelAttribute("user")
    public User getUserFromSession(HttpSession session){
        return (User) session.getAttribute("user");
    }

    @GetMapping("/{id}/view")
    public String viewBooks(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book with ID " + id + " not found"));
        model.addAttribute("Book", book);
        return "book_view";
    }

    // Get mapping for "Add Book" page
    @GetMapping("/add_book")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book()); // Empty Book object to bind the form fields
        return "add_book"; // Returns the add_book.html template
    }

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

    @GetMapping("/contact")
    public String showContactPage(){
        return "contact_info";
    }

    @GetMapping("/remove_book")
    public String showRemoveBookForm(Model model) {
        return "remove_book";
    }

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

}

