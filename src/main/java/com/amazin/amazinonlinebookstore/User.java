package com.amazin.amazinonlinebookstore;

public class User {
    private final String username;
    private final String password;
    private final PERMISSIONS permissions;
    private final ShoppingCart cart;

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
