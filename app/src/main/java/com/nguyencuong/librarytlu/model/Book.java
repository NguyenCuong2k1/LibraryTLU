package com.nguyencuong.librarytlu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {
    @Expose
    @SerializedName("books_file")
    private String books_file;
    @Expose
    @SerializedName("librarian_id")
    private String librarian_id;
    @Expose
    @SerializedName("books_availability")
    private String books_availability;
    @Expose
    @SerializedName("books_quantity")
    private String books_quantity;
    @Expose
    @SerializedName("books_price")
    private String books_price;
    @Expose
    @SerializedName("books_purchase_date")
    private String books_purchase_date;
    @Expose
    @SerializedName("books_publication_name")
    private String books_publication_name;
    @Expose
    @SerializedName("books_author_name")
    private String books_author_name;
    @Expose
    @SerializedName("books_image")
    private String books_image;
    @Expose
    @SerializedName("books_name")
    private String books_name;
    @Expose
    @SerializedName("id")
    private String id;

    public String getBooks_file() {
        return books_file;
    }

    public void setBooks_file(String books_file) {
        this.books_file = books_file;
    }

    public String getLibrarian_id() {
        return librarian_id;
    }

    public void setLibrarian_id(String librarian_id) {
        this.librarian_id = librarian_id;
    }

    public String getBooks_availability() {
        return books_availability;
    }

    public void setBooks_availability(String books_availability) {
        this.books_availability = books_availability;
    }

    public String getBooks_quantity() {
        return books_quantity;
    }

    public void setBooks_quantity(String books_quantity) {
        this.books_quantity = books_quantity;
    }

    public String getBooks_price() {
        return books_price;
    }

    public void setBooks_price(String books_price) {
        this.books_price = books_price;
    }

    public String getBooks_purchase_date() {
        return books_purchase_date;
    }

    public void setBooks_purchase_date(String books_purchase_date) {
        this.books_purchase_date = books_purchase_date;
    }

    public String getBooks_publication_name() {
        return books_publication_name;
    }

    public void setBooks_publication_name(String books_publication_name) {
        this.books_publication_name = books_publication_name;
    }

    public String getBooks_author_name() {
        return books_author_name;
    }

    public void setBooks_author_name(String books_author_name) {
        this.books_author_name = books_author_name;
    }

    public String getBooks_image() {
        return books_image;
    }

    public void setBooks_image(String books_image) {
        this.books_image = books_image;
    }

    public String getBooks_name() {
        return books_name;
    }

    public void setBooks_name(String books_name) {
        this.books_name = books_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "books_file='" + books_file + '\'' +
                ", librarian_id='" + librarian_id + '\'' +
                ", books_availability='" + books_availability + '\'' +
                ", books_quantity='" + books_quantity + '\'' +
                ", books_price='" + books_price + '\'' +
                ", books_purchase_date='" + books_purchase_date + '\'' +
                ", books_publication_name='" + books_publication_name + '\'' +
                ", books_author_name='" + books_author_name + '\'' +
                ", books_image='" + books_image + '\'' +
                ", books_name='" + books_name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
