package com.amazin.amazinonlinebookstore;


import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;


public class BookTest {

    @Test
    public void testBookCreation(){
        Book book = new Book("Effective Java",  "Best practices for Java", "Joshua Bloch","8647823308954", new Date(124, 1, 1), 42.99);

        assertEquals("Effective Java", book.getTitle());
        assertEquals("Best practices for Java", book.getDescription());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("8647823308954", book.getISBN());
        assertEquals(new Date(124, 1, 1), book.getPublishDate());
        assertEquals(42.99, book.getPrice());
    }

    @Test
    public void testBookSettersAndGetters() {
        Book book = new Book();

        book.setTitle("Effective Java");
        book.setDescription("Best practices for Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("8647823308954");
        book.setPrice(42.99);
        book.setPublishDate(new Date(124,1,1));

        assertEquals("Effective Java", book.getTitle());
        assertEquals("Best practices for Java", book.getDescription());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("8647823308954", book.getISBN());
        assertEquals(new Date(124, 1, 1), book.getPublishDate());
        assertEquals(42.99, book.getPrice());
    }

    @Test
    public void testBookToString(){
        Book book = new Book("Effective Java",  "Best practices for Java", "Joshua Bloch","8647823308954", new Date(124, 1, 1), 42.99);

        String expectedString = "\nTitle: Effective Java\nAuthor: Joshua Bloch\nPublish Date: Thu Feb 01 00:00:00 EST 2024\nISBN: 8647823308954\nPrice: 42.99";
        assertEquals(expectedString, book.toString());
    }


}