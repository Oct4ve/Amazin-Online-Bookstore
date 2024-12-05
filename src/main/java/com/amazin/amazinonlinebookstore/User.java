package com.amazin.amazinonlinebookstore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private final String username;
    private final String password;
    private final PERMISSIONS permissions;
    @OneToOne(cascade = {CascadeType.ALL, CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private final ShoppingCart cart;

    public User(){
        this.username = "";
        this.password = "";
        this.permissions = PERMISSIONS.BASIC;
        this.cart = null;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.permissions = PERMISSIONS.BASIC;
        this.cart = new ShoppingCart(this);
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public PERMISSIONS getPermissions() {
        return permissions;
    }
    public ShoppingCart getCart() {
        return cart;
    }
    public void addToUserCart(Book book, int quantity){
        assert this.cart != null;
        for (Book cartBook : this.cart.getCartBooks()) {
            cartBook.setCartQuantity(cartBook.getCartQuantity() + quantity);
            return;
        }
        book.setCartQuantity(quantity);
        this.cart.addToCart(book, quantity);
    }
    public void removeFromUserCart(Book book, int quantity) {
        for (Book cartBook : this.cart.getCartBooks()) {
            if (cartBook.getId() == book.getId()) {
                int currentQuantity = cartBook.getCartQuantity();

                if (currentQuantity <= quantity) {
                    this.cart.getCartBooks().remove(cartBook);
                } else {
                    cartBook.setCartQuantity(currentQuantity - quantity);
                }
                return;
            }
        }
        System.out.println("Book not found in cart.");
    }
}
