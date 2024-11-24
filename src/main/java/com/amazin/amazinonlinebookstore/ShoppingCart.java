package com.amazin.amazinonlinebookstore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany
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

    public User getUser(){
        return user;
    }

    public void addToCart(Book book){
        cart.add(book);
    }

    public void removeFromCart(Book book){
        cart.remove(book);
    }

    public void emptyCart(){
        cart.clear();
    }

    public Book getFromCart(int index){
        return cart.get(index);
    }

    public double calculateTotal(){
        double total = 0;
        for (Book book: cart) {
            total += book.getPrice();
        }

        return total;
    }


}
