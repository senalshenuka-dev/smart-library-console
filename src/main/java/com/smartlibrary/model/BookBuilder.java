package com.smartlibrary.model;

// Builder pattern for Book creation.

public class BookBuilder {
    private String bookId = "B" + System.currentTimeMillis();
    private String title;
    private String author;
    private String category;
    private String isbn;

    public BookBuilder withBookId(String id) { this.bookId = id; return this; }
    public BookBuilder withTitle(String t) { this.title = t; return this; }
    public BookBuilder withAuthor(String a) { this.author = a; return this; }
    public BookBuilder withCategory(String c) { this.category = c; return this; }
    public BookBuilder withISBN(String i) { this.isbn = i; return this; }

    public Book build() {
        return new Book(bookId, title == null ? "Untitled" : title,
                author == null ? "Unknown" : author,
                category == null ? "General" : category,
                isbn == null ? "" : isbn);
    }
}