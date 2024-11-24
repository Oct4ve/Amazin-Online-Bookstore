package com.amazin.amazinonlinebookstore;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ThymeLeafController.class)
public class ThymeLeafControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void testHomePage() throws Exception {
        // Mock repository response
        Mockito.when(bookRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("books"));
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
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

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
                .andExpect(model().attributeExists("message"));
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


}
