package com.amazin.amazinonlinebookstore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shopping_cart_id")
    private List<Book> cart;
    @OneToOne
    private User user;
    public ShoppingCart(){
        cart = new ArrayList<Book>();
        this.user = null;
    }

    public ShoppingCart(User user) {
        cart = new ArrayList<Book>();
        this.user = user;
    }

    public List<Book> getCartBooks(){
        return cart;
    }

    public void setCart(List<Book> cart) {
        this.cart = cart;
    }

    public User getUser(){
        return user;
    }

    public String addToCart(Book book, int quantity) {
        for (Book existingBook : cart) {
            if (existingBook.getId() == book.getId()) {
                int newCartQuantity = existingBook.getCartQuantity() + quantity;

                if (newCartQuantity > existingBook.getStockQuantity()) {
                    // Ensure the user can't exceed stock quantity
                    return "Error: Cannot add more than what is in stock (" + existingBook.getStockQuantity() + ").";
                }

                // Update the quantity if it doesn't exceed stock
                existingBook.setCartQuantity(newCartQuantity);
                book.setCartQuantity(newCartQuantity);
                return "Book quantity updated in cart.";
            }
        }

        // If the book is not in the cart, add it
        if (quantity <= book.getStockQuantity()) {
            book.setCartQuantity(quantity);
            cart.add(book);
            return "Book added to cart.";
        } else {
            return "Error: Cannot add more than what is in stock (" + book.getStockQuantity() + ").";
        }
    }

    public String removeFromCart(Book book, int quantity) {
        for (Book existingBook : cart) {
            if (existingBook.getId() == book.getId()) {
                int currentCartQuantity = existingBook.getCartQuantity();
                int newCartQuantity = currentCartQuantity - quantity;

                if (newCartQuantity <= 0) {
                    // Remove the book from the cart if the quantity is zero or less
                    cart.remove(existingBook);
                    return "Book removed from cart.";
                } else {
                    // Update the cart quantity
                    existingBook.setCartQuantity(newCartQuantity);
                    return "Book quantity updated in cart.";
                }
            }
        }

        return "Error: Book not found in cart.";
    }

    public void emptyCart(){
        cart.clear();
    }

    public Book getFromCart(int index){
        return cart.get(index);
    }

    public double calculateTotal() {
        double total = 0;
        for (Book book : cart) {
            total += book.getPrice() * book.getCartQuantity();
        }
        return total;
    }

}
