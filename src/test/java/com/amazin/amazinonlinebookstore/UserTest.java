package com.amazin.amazinonlinebookstore;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testDefaultConstructor(){
        // Create user with default parameters
        User user = new User();

        // Check that the user has the default parameters
        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals(PERMISSIONS.BASIC, user.getPermissions());
        assertNull(user.getCart());
    }

    @Test
    public void testUserCreation(){
        // Create user with parameters
        User user = new User("user", "password");

        // Check that the user has the correct parameters
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(PERMISSIONS.BASIC, user.getPermissions());
        assertNotNull(user.getCart());
    }

    @Test
    public void testAddToUserCart(){
        // Create user and book
        User user = new User("user", "password");
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Add book to user's cart
        user.addToUserCart(book, 1);

        // Check if book was added to cart
        assertNotNull(user.getCart());
        assertEquals(1, user.getCart().getCartBooks().size());
        assertEquals("Effective Java", user.getCart().getCartBooks().get(0).getTitle());
    }

    @Test
    public void testAddSingleBookToCart() {
        // Create user and book
        User user = new User("user", "password");
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Add book to cart
        user.addToUserCart(book, 1);

        // Verify book is added
        assertNotNull(user.getCart());
        assertEquals(1, user.getCart().getCartBooks().size());
        assertEquals("Effective Java", user.getCart().getCartBooks().get(0).getTitle());
        assertEquals(1, user.getCart().getCartBooks().get(0).getCartQuantity());
    }

    @Test
    public void testAddDuplicateBookToCart() {
        // Create user and book
        User user = new User("user", "password");
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Add book to cart twice
        user.addToUserCart(book, 1);
        user.addToUserCart(book, 2);

        // Verify quantity is updated
        assertNotNull(user.getCart());
        assertEquals(1, user.getCart().getCartBooks().size());
        assertEquals("Effective Java", user.getCart().getCartBooks().get(0).getTitle());
        assertEquals(3, user.getCart().getCartBooks().get(0).getCartQuantity());
    }

    @Test
    public void testRemoveBookFromCart() {
        // Create user and book
        User user = new User("user", "password");
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);


        // Add and remove book
        user.addToUserCart(book, 3);
        user.removeFromUserCart(book, 2);

        // Verify quantity is reduced
        assertNotNull(user.getCart());
        assertEquals(1, user.getCart().getCartBooks().size());
        assertEquals("Effective Java", user.getCart().getCartBooks().get(0).getTitle());
        assertEquals(1, user.getCart().getCartBooks().get(0).getCartQuantity());
    }

    @Test
    public void testRemoveBookCompletelyFromCart() {
        // Create user and book
        User user = new User("user", "password");
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99, 10, 0);

        // Add and completely remove book
        user.addToUserCart(book, 2);
        user.removeFromUserCart(book, 2);

        // Verify cart is empty
        assertNotNull(user.getCart());
        assertTrue(user.getCart().getCartBooks().isEmpty());
    }
}
