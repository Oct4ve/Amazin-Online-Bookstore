package com.amazin.amazinonlinebookstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    // Mocking the BookRepository to simulate database interactions
    @Mock
    private BookRepository bookRepository;

    // Injecting the mocked BookRepository into the BookController
    @InjectMocks
    private  BookController bookController;

    // Initialize mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddBook() {
        // Create a book object and configure the mock repository behavior
        Book book = new Book("Effective Java", "Best practices for Java", "Joshua Bloch", "8647823308954", LocalDate.of(2024, 1, 1), 42.99);
        when(bookRepository.save(book)).thenReturn(book);

        // Call the addBook method in the controller
        Book savedBook = bookController.addBook(book);

        // Verify the returned book is not noll and has the expected title
        assertNotNull((savedBook));
        assertEquals("Effective Java", savedBook.getTitle());
    }

    @Test
    public void testRemoveBook(){
        // Create a book object and configure the mock repository behavior
        Book book = new Book("Effective Java", "Best practices for Java", "Joshua Bloch", "8647823308954", LocalDate.of(2024, 1, 1), 42.99);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Call the removeBook method in the controller
        bookController.removeBook(1L); // 1L = ID 1, long

        // Verify the delete method on the repository is called exactly once
        verify(bookRepository, times(1)).delete(book);
    }


}
