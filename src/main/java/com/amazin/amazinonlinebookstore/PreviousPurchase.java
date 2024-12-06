package com.amazin.amazinonlinebookstore;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "purchase")
public class PreviousPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Book> books = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate purchaseDate;

    public PreviousPurchase(){
        // default constructor for persistence
        books = new ArrayList<Book>();
        this.user = null;
        this.purchaseDate = null;
    }

    public PreviousPurchase(User user, LocalDate date) {
        this.user = user;
        this.purchaseDate = date;
    }

    public List<Book> getPurchaseBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double calculateTotal() {
        double total = 0;
        for (Book book : books) {
            total += book.getPrice() * book.getCartQuantity();
        }
        return total;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public Long getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreviousPurchase that = (PreviousPurchase) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}