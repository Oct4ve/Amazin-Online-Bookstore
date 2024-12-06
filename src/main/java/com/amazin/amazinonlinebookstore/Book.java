package com.amazin.amazinonlinebookstore;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String coverImagePath;
    private String description;
    private String ISBN;
    private String author;
    private LocalDate publishDate;
    private LocalDate receiveDate;
    private double price;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int stockQuantity;

    //Not persisted to the database
    @Column(nullable = false, columnDefinition = "int default 0")
    private int cartQuantity;

    public Book(){
        this.title = null;
        this.coverImagePath = null;
        this.description = null;
        this.author = null;
        this.ISBN = null;
        this.publishDate = null;
        this.price = 0;
        this.stockQuantity = 0;
        this.cartQuantity = 0;
    }
    public Book(String title, String coverImage, String description, String author, String ISBN, LocalDate publishDate, double price, int stockQuantity, int cartQuantity) {
        this.title = title;
        this.coverImagePath = coverImage;
        this.description = description;
        this.author = author;
        this.ISBN = ISBN;
        this.publishDate = publishDate;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.cartQuantity = cartQuantity;
    }

    public String getTitle() { return title; }
    // public Image getCover() { return cover; }
    public String getDescription() { return description; }
    public String getISBN() { return ISBN; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public long getId() { return id; }
    public LocalDate getPublishDate() { return publishDate; }
    public LocalDate getReceiveDate() { return receiveDate; }
    public String getCoverImagePath() {return coverImagePath;}
    public void setCoverImagePath(String coverImageURL) {this.coverImagePath = coverImageURL;}
    public int getStockQuantity() {
        return  stockQuantity;
    }
    public int getCartQuantity() {
        return  cartQuantity;
    }


    public void setTitle(String newTitle) { this.title = newTitle; }
    // public void setCover(Image cover) { this.cover = cover; }
    public void setDescription(String description) { this.description = description; }
    public void setISBN(String isbn) { this.ISBN = isbn; }
    public void setAuthor(String author) { this.author = author; }
    public void setPrice(double price) { this.price = price; }
    public void setId(long id) { this.id = id; }
    public void setPublishDate(LocalDate newDate) { this.publishDate = newDate; }
    public void setReceiveDate(LocalDate newDate) { this.receiveDate = newDate; }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    @Override
    public String toString(){
        return "\nTitle: " + title + "\nAuthor: " + author + "\nPublish Date: " + publishDate + "\nISBN: " + ISBN + "\nPrice: " + price + "\nStock Quantity: " + stockQuantity + "\nCart Quantity: " + cartQuantity;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return id == book.id; // Compare based on the unique id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash based on ID
    }
}


