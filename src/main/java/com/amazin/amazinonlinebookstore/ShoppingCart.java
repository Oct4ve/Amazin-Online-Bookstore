package com.amazin.amazinonlinebookstore;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    public static /* we might not want static here */ ArrayList<Book> cart;
    private final String username;

    public ShoppingCart(String username) {
        cart = new ArrayList<Book>();
        this.username = username;
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
