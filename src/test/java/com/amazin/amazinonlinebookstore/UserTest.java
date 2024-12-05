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
        Book book = new Book("Effective Java","",  "Best practices for Java", "Joshua Bloch","8647823308954", LocalDate.of(2024, 1, 1), 42.99);

        // Add book to user's cart
        user.addToUserCart(book);

        // Check if book was added to cart
        assertNotNull(user.getCart());
        assertEquals(1, user.getCart().getCartBooks().size());
        assertEquals("Effective Java", user.getCart().getCartBooks().get(0).getTitle());
    }

    @Test
    public void testMultipleBooksInCart(){
        // Create user and books
        User user = new User("user", "password");
        Book book1 = new Book("Book1","", "Description1", "Author1", "1111111111111", LocalDate.of(2024, 11, 24), 10.00);
        Book book2 = new Book("Book2","", "Description2", "Author2", "2222222222222", LocalDate.of(2023, 10, 23), 20.00);

        // Add books to user's cart
        user.addToUserCart(book1);
        user.addToUserCart(book2);

        // Check the cart size and contents
        assertNotNull(user.getCart());
        assertEquals(2, user.getCart().getCartBooks().size());
        assertEquals("Book1", user.getCart().getCartBooks().get(0).getTitle());
        assertEquals("Book2", user.getCart().getCartBooks().get(1).getTitle());

    }
}
