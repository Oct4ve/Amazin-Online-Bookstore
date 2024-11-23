package com.amazin.amazinonlinebookstore;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    private ShoppingCart cart;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart("samwilson29");
        book1 = new Book();
        book1.setTitle("Book One");
        book1.setDescription("Book One Description");
        book1.setAuthor("Book One Author");
        book1.setIsbn("Book One Isbn");
        book1.setIsbn("Book One Isbn");
        book1.setPrice(20.00);

        book2 = new Book();
        book2.setTitle("Book Two");
        book2.setDescription("Book Two Description");
        book2.setAuthor("Book Two Author");
        book2.setIsbn("Book Two Isbn");
        book2.setIsbn("Book Two Isbn");
        book2.setPrice(30.00);
    }

    @Test
    public void testAddToCart() {
        cart.addToCart(book1);
        assertTrue(ShoppingCart.cart.contains(book1));
        assertEquals(1, ShoppingCart.cart.size());
    }

    @Test
    void testRemoveFromCart() {
        cart.addToCart(book1);
        cart.addToCart(book2);

        cart.removeFromCart(book1);

        assertEquals(1, ShoppingCart.cart.size());
        assertFalse(ShoppingCart.cart.contains(book1));
        assertTrue(ShoppingCart.cart.contains(book2));
    }

    @Test
    void testEmptyCart() {
        cart.addToCart(book1);
        cart.addToCart(book2);

        cart.emptyCart();

        assertEquals(0, ShoppingCart.cart.size());
    }

    @Test
    void testGetFromCart() {
        cart.addToCart(book1);
        cart.addToCart(book2);

        assertEquals(book1, cart.getFromCart(0));
        assertEquals(book2, cart.getFromCart(1));
    }

    @Test
    void testGetFromCart_InvalidIndex() {
        cart.addToCart(book1);

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            cart.getFromCart(5);
        });

        assertNotNull(exception);
    }

    @Test
    void testCalculateTotal() {
        cart.addToCart(book1);
        cart.addToCart(book2);

        double total = cart.calculateTotal();

        assertEquals(20.00 + 30.00, total, 0.001);
    }

    @Test
    void testCalculateTotal_EmptyCart() {
        double total = cart.calculateTotal();

        assertEquals(0.0, total, 0.001);
    }
}