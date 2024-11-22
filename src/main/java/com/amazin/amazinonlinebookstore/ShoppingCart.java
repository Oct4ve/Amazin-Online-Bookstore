package com.amazin.amazinonlinebookstore;
import java.util.ArrayList;

public class ShoppingCart {

    public ArrayList<Book> cart;

    public ShoppingCart() {
        cart = new ArrayList<Book>();
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
