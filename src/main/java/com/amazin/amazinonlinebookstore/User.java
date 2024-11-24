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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
    public void addToUserCart(Book book){
        this.cart.addToCart(book);
    }
}
