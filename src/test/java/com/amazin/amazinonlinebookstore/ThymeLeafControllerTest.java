package com.amazin.amazinonlinebookstore;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ThymeLeafController.class)
public class ThymeLeafControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    public void testHomePage() throws Exception {
        // Mock repository response
        Mockito.when(bookRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("currentUser", "Guest"));
    }

    @Test
    public void testViewBooks() throws Exception {
        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Sample Book");

        // Mock repository response
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        mockMvc.perform(get("/1/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("book_view"))
                .andExpect(model().attribute("Book", mockBook));
    }

    @Test
    public void testAddBookForm() throws Exception {
        mockMvc.perform(get("/add_book"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_book"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    public void testAddBook() throws Exception {
        Book mockBook = new Book();
        mockBook.setTitle("New Book");

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mockBook);

        mockMvc.perform(post("/add_book")
                        .param("title", "New Book")
                        .param("description", "Test Description")
                        .param("author", "Test Author")
                        .param("isbn", "1234567890123")
                        .param("price", "40.00")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

   /* @Test
    public void testAddBook_TitleAlreadyExists() throws Exception {
        Book existingBook = new Book();
        existingBook.setTitle("Existing Book");

        Mockito.when(bookRepository.findByTitle("Existing Book")).thenReturn(existingBook);

        mockMvc.perform(post("/add_book")
                        .param("title", "Existing Book")
                        .param("description", "Duplicate Book")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("add_book"))
                .andExpect(model().attributeExists("errorMessage"));
    }*/

    @Test
    public void testContactPage() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact_info"));
    }

    @Test
    public void testRemoveBookForm() throws Exception {
        mockMvc.perform(get("/remove_book"))
                .andExpect(status().isOk())
                .andExpect(view().name("remove_book"));
    }

    @Test
    public void testRemoveBook() throws Exception {
        Book mockBook = new Book();
        mockBook.setTitle("Sample Book");

        Mockito.when(bookRepository.findByTitle("Sample Book")).thenReturn(mockBook);

        mockMvc.perform(post("/remove_book")
                        .param("title", "Sample Book")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("remove_book"))
                .andExpect(model().attribute("message", "Book with title 'Sample Book' was successfully deleted."));
    }

    @Test
    public void testRemoveBook_NotFound() throws Exception {
        Mockito.when(bookRepository.findByTitle("Nonexistent Book")).thenReturn(null);

        mockMvc.perform(post("/remove_book")
                        .param("title", "Nonexistent Book")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("remove_book"))
                .andExpect(model().attribute("message", "Error: Book with title 'Nonexistent Book' not found."));
    }

    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testLogicAction_UserNotExist() throws Exception {
        Mockito.when(userRepository.findByUsername("newuser")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("username", "newuser")
                        .param("password", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Username not found. Please try again."));
    }

    @Test
    public void testLoginAction_UserExists() throws Exception {
        User existingUser = new User("existinguser", "password123");
        Mockito.when(userRepository.findByUsername("existinguser")).thenReturn(existingUser);

        mockMvc.perform(post("/login")
                        .param("username", "existinguser")
                        .param("password", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testSortByAuthor() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("Z Title","", "Description", "Author B", "12345", null, 20.0),
                new Book("A Title","", "Description", "Author A", "12346", null, 10.0)
        );
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // Perform a GET request to the /sort_by_author endpoint
        mockMvc.perform(get("/sort_by_author"))
                .andExpect(status().isOk())
                .andExpect(view().name("sort_by_author"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books.stream()
                        .sorted(Comparator.comparing(Book::getAuthor))
                        .toList())); // // Verify the books are sorted by author alphabetically
    }

    @Test
    public void testSortByTitle() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("Z Title","", "Description", "Author B", "12345", null, 20.0),
                new Book("A Title","", "Description", "Author A", "12346", null, 10.0)
        );
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // Perform a GET request to the /sort_by_title endpoint
        mockMvc.perform(get("/sort_by_title"))
                .andExpect(status().isOk())
                .andExpect(view().name("sort_by_title"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books.stream()
                        .sorted(Comparator.comparing(Book::getTitle))
                        .toList())); // Validate books are sorted alphabetically by title
    }

    @Test
    public void testSortByPriceLow() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("Book1","", "Description", "Author A", "12345", null, 20.0),
                new Book("Book2","", "Description", "Author B", "12346", null, 10.0)
        );
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // Perform a GET request to the /sort_by_price_low endpoint
        mockMvc.perform(get("/sort_by_price_low"))
                .andExpect(status().isOk())
                .andExpect(view().name("sort_by_price_low"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books.stream()
                        .sorted(Comparator.comparingDouble(Book::getPrice))
                        .toList())); // Verify the books are sorted in ascending order of price
    }

    @Test
    public void testSortByPriceHigh() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("Book1","", "Description", "Author A", "12345", null, 10.0),
                new Book("Book2","", "Description", "Author B", "12346", null, 20.0)
        );
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // Perform a GET request to the /sort_by_price_high endpoint
        mockMvc.perform(get("/sort_by_price_high"))
                .andExpect(status().isOk())
                .andExpect(view().name("sort_by_price_high"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books.stream()
                        .sorted((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()))
                        .toList())); // Verify the books are sorted in descending order of price
    }

    @Test
    public void testSortByDateOldestFirst() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("Book1","", "Description", "Author A", "12345", LocalDate.of(2000, 01, 01), 10.0),
                new Book("Book2","", "Description", "Author B", "12346", LocalDate.of(1900, 01, 01), 20.0)
        );
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // Perform a GET request to the /sort_by_date_old endpoint
        mockMvc.perform(get("/sort_by_date_old"))
                .andExpect(status().isOk())
                .andExpect(view().name("sort_by_date_old"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books.stream()
                        .sorted(Comparator.comparing(Book::getPublishDate))
                        .toList())); // Validate books are sorted by the oldest publishing date first
    }

    @Test
    public void testSortByDateNewestFirst() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("Book1","", "Description", "Author A", "12345", LocalDate.of(1900, 01, 01), 10.0),
                new Book("Book2","", "Description", "Author B", "12346", LocalDate.of(2000, 01, 01), 20.0)
        );
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // Perform a GET request to the /sort_by_date_new endpoint
        mockMvc.perform(get("/sort_by_date_new"))
                .andExpect(status().isOk())
                .andExpect(view().name("sort_by_date_new"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books.stream()
                        .sorted((b1, b2) -> b2.getPublishDate().compareTo(b1.getPublishDate()))
                        .toList())); // Verify books are sorted by the newest publishing date first
    }

    @Test
    public void testCheckout() throws Exception {
        // Mock user and cart setup
        User mockUser = new User("testuser", "password");
        ShoppingCart mockCart = new ShoppingCart(mockUser);

        Book book1 = new Book("Book1","", "Description1", "Author1", "12345", null, 10.0);
        Book book2 = new Book("Book2","", "Description2", "Author2", "67890", null, 20.0);
        mockCart.addToCart(book1);
        mockCart.addToCart(book2);

        mockUser.getCart().addToCart(book1);
        mockUser.getCart().addToCart(book2);

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(get("/checkout").sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"))
                .andExpect(model().attributeExists("userBooks"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attribute("total", 30.0));
    }

    @Test
    public void testPurchase() throws Exception {
        // Mock user and cart setup
        User mockUser = new User("testuser", "password");
        ShoppingCart mockCart = new ShoppingCart(mockUser);

        Book book1 = new Book("Book1","", "Description1", "Author1", "12345", null, 10.0);
        Book book2 = new Book("Book2","", "Description2", "Author2", "67890", null, 20.0);
        mockCart.addToCart(book1);
        mockCart.addToCart(book2);

        mockUser.getCart().addToCart(book1);
        mockUser.getCart().addToCart(book2);

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(post("/purchase").sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("purchased"))
                .andExpect(model().attributeExists("purchasedBooks"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attribute("total", 30.0));

        // Assert that the cart is cleared after purchase
        assertEquals(0, mockUser.getCart().getCartBooks().size());
    }
    @Test
    public void testLogout() throws Exception {
        // Mock a session and simulate a logout
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "testUser"); // Add a mock attribute

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection()) // Expect a redirect status
                .andExpect(redirectedUrl("/"));        // Expect redirection to "/"

        // Verify that the session is invalidated
        assert session.isInvalid();  // Assert that the session is invalidated
    }
}
