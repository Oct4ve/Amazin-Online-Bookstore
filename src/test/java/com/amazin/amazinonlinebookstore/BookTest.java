package com.amazin.amazinonlinebookstore;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class BookTest {

    @Test
    public void testDefaultConstructor(){
        // Create new book with default attributes
        Book book = new Book();

        // Check that the book attributes are correct (everything is null, except price which is 0)
        assertNull(book.getTitle());
        assertNull(book.getDescription());
        assertNull(book.getAuthor());
        assertNull(book.getISBN());
        assertNull(book.getPublishDate());
        assertEquals(0, book.getPrice());
    }

    @Test
    public void testBookCreation(){
        // Create new book with custom attributes
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Check that the book has the correct attributes
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Best practices for Java", book.getDescription());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("8647823308954", book.getISBN());
        assertEquals(LocalDate.of(2024, 1, 1), book.getPublishDate());
        assertEquals(42.99, book.getPrice());
    }

    @Test
    public void testBookSettersAndGetters() {
        // Create new book
        Book book = new Book();

        // Set various attributes using setters
        book.setTitle("Effective Java");
        book.setDescription("Best practices for Java");
        book.setAuthor("Joshua Bloch");
        book.setISBN("8647823308954");
        book.setPrice(42.99);
        book.setPublishDate(LocalDate.of(2024,1,1));

        // Verify that the getters return the correct values
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Best practices for Java", book.getDescription());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("8647823308954", book.getISBN());
        assertEquals(LocalDate.of(2024, 1, 1), book.getPublishDate());
        assertEquals(42.99, book.getPrice());
    }

    @Test
    public void testBookToString(){
        // Create new book
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Verify that the toString method matches the expected string format
        String expectedString = "\nTitle: Effective Java\nAuthor: Joshua Bloch\nPublish Date: 2024-01-01\nISBN: 8647823308954\nPrice: 42.99\nStock Quantity: 10\nCart Quantity: 0";
        assertEquals(expectedString, book.toString());
    }

    @Test
    void testEquals_SameId() {
        Book book1 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);
        Book book2 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Simulate same ID for testing
        book1.setId(1L);
        book2.setId(1L);

        assertTrue(book1.equals(book2));
    }

    @Test
    void testEquals_DifferentId() {
        Book book1 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);
        Book book2 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Simulate different IDs
        book1.setId(1L);
        book2.setId(2L);

        assertFalse(book1.equals(book2));
    }

    @Test
    void testHashCode_SameId() {
        Book book1 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);
        Book book2 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Simulate same ID
        book1.setId(1L);
        book2.setId(1L);

        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void testHashCode_DifferentId() {
        Book book1 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);
        Book book2 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Simulate different IDs
        book1.setId(1L);
        book2.setId(2L);

        assertNotEquals(book1.hashCode(), book2.hashCode());
    }
    @Test
    void testHashCodeAndEquals_CollectionBehavior() {
        Book book1 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);
        Book book2 = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Simulate same ID
        book1.setId(1L);
        book2.setId(1L);

        HashSet<Book> bookSet = new HashSet<>();
        bookSet.add(book1);

        // Ensure book2 is recognized as the same object in the set
        assertTrue(bookSet.contains(book2));
    }


}
