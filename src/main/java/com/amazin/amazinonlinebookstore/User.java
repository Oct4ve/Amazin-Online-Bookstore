package com.amazin.amazinonlinebookstore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private ShoppingCart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PreviousPurchase> previousPurchases;

    public User(){
        this.username = "";
        this.password = "";
        this.permissions = PERMISSIONS.BASIC;
        this.cart = null;
        this.previousPurchases = null;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.permissions = PERMISSIONS.BASIC;
        this.cart = new ShoppingCart(this);
        this.previousPurchases = new ArrayList<PreviousPurchase>();
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
    public void setCart(ShoppingCart cart) {
        this.cart = cart;
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

    public void similarCustomers(){

    }


    public static double jaccardDistance(List<Book> list1, List<Book> list2) {
        // Create a list to store the intersection
        List<Book> intersection = new ArrayList<>(list1);
        intersection.retainAll(list2); // Retains only elements that are common to both lists

        // Create a list to store the union
        List<Book> union = new ArrayList<>(list1);
        union.addAll(list2); // Add all elements from both lists

        // Remove duplicates to get a proper union
        Set<Book> uniqueUnion = new HashSet<>(union);

        // Calculate Jaccard Index
        double jaccardIndex = (double) intersection.size() / uniqueUnion.size();

        // Calculate and return Jaccard Distance
        return 1.0 - jaccardIndex;
    }

    public List<Book> convertPreviousPurchasesToOneList(List<PreviousPurchase> previousPurchases) {
        List<Book> user_purchases = new ArrayList<>();
        for (PreviousPurchase innerList : previousPurchases) {
            user_purchases.addAll(innerList.getPurchaseBooks());
        }
        return user_purchases;
    }

    public List<PreviousPurchase> getPreviousPurchases() {
        return previousPurchases;
    }

    public void setPreviousPurchases(List<PreviousPurchase> previousPurchases) {
        this.previousPurchases = previousPurchases;
    }
}
