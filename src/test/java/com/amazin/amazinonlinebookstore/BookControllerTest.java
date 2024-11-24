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

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private  BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddBook() {
        Book book = new Book("Effective Java", "Best practices for Java", "Joshua Bloch", "8647823308954", LocalDate.of(2024, 1, 1), 42.99);
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookController.addBook(book);

        assertNotNull((savedBook));
        assertEquals("Effective Java", savedBook.getTitle());
    }

    @Test
    public void testRemoveBook(){
        Book book = new Book("Effective Java", "Best practices for Java", "Joshua Bloch", "8647823308954", LocalDate.of(2024, 1, 1), 42.99);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookController.removeBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }


}
