// This controls the front end display.

package com.amazin.amazinonlinebookstore;

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

@Controller
public class ThymeLeafController {
    @Autowired
    private BookRepository bookRepository;

    // Home page mapping to display all books (or a welcome message)
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("books", bookRepository.findAll()); // Adds all books to the model
        return "home"; // returns the home.html template
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        return "login";
    }

    @PostMapping("/login")
    public String loginAction(@RequestParam ("username") String username,
                              @RequestParam ("password") String password){
        return "redirect:/";
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
                          @RequestParam(value = "coverFile", required = false) MultipartFile coverFile) {
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

    @GetMapping("/error")
    public String showErrorPage(){
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
}

