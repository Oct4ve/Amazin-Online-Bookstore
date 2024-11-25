package com.amazin.amazinonlinebookstore;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    private ShoppingCart cart;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        User user1 = new User("pietro", "adamvoski");
        cart = new ShoppingCart(user1);
        book1 = new Book();
        book1.setTitle("Book One");
        book1.setDescription("Book One Description");
        book1.setAuthor("Book One Author");
        book1.setISBN("Book One Isbn");
        book1.setISBN("Book One Isbn");
        book1.setPrice(20.00);
        book1.setId(1L);

        book2 = new Book();
        book2.setTitle("Book Two");
        book2.setDescription("Book Two Description");
        book2.setAuthor("Book Two Author");
        book2.setISBN("Book Two Isbn");
        book2.setISBN("Book Two Isbn");
        book2.setPrice(30.00);
        book2.setId(2L);
    }

    @Test
    public void testDefaultConstructor(){

        // Create a cart with the default constructor
        ShoppingCart cart0 = new ShoppingCart();


        // Assert that the cart is initialized correctly
        assertNotNull(cart0.getCartBooks());
        assertTrue(cart0.getCartBooks().isEmpty());
        assertNull(cart0.getUser());
    }

    @Test
    public void testParameterizedConstructor(){

        // Asser the cart and user are initialized correctly
        assertNotNull(cart.getCartBooks());
        assertTrue(cart.getCartBooks().isEmpty());
        assertEquals("pietro", cart.getUser().getUsername());
        assertEquals("adamvoski", cart.getUser().getPassword());
    }
    @Test
    public void testAddToCart() {
        // Add book to cart
        cart.addToCart(book1);

        // Assert that cart contains the books and has the correct size
        assertTrue(cart.getCartBooks().contains(book1));
        assertEquals(1, cart.getCartBooks().size());
    }

    @Test
    void testRemoveFromCart() {
        // Add books to cart
        cart.addToCart(book1);
        cart.addToCart(book2);

        // Remove one book from the cart
        cart.removeFromCart(book1);

        // Assert the remaining state of the cart
        assertEquals(1, cart.getCartBooks().size());
        assertFalse(cart.getCartBooks().contains(book1));
        assertTrue(cart.getCartBooks().contains(book2));
    }

    @Test
    void testEmptyCart() {
        // Add books to cart
        cart.addToCart(book1);
        cart.addToCart(book2);

        // Empty all books from cart
        cart.emptyCart();

        // Assert that the cart is now empty
        assertEquals(0, cart.getCartBooks().size());
    }

    @Test
    void testGetFromCart() {
        // Add books to cart
        cart.addToCart(book1);
        cart.addToCart(book2);

        // Assert retrieval by index
        assertEquals(book1, cart.getFromCart(0));
        assertEquals(book2, cart.getFromCart(1));
    }

    @Test
    void testGetFromCart_InvalidIndex() {
        // Add book to cart
        cart.addToCart(book1);

        // Assert an invalid index throws an IndexOutOfBoundsException
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            cart.getFromCart(5);
        });

        assertNotNull(exception);
    }

    @Test
    void testCalculateTotal() {
        // Add books to the cart
        cart.addToCart(book1);
        cart.addToCart(book2);

        // Calculate the total price
        double total = cart.calculateTotal();

        // Assert the total matches the expected sum
        assertEquals(20.00 + 30.00, total, 0.001);
    }

    @Test
    void testCalculateTotal_EmptyCart() {
        //Calculate the total for an empty cart
        double total = cart.calculateTotal();

        // Assert that the total is 0
        assertEquals(0.0, total, 0.001);
    }
}