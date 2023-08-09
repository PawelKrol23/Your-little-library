package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.book.Book;

import java.util.Optional;

public interface BookService {
    Iterable<Book> findAll();

    void createNewBook(Book book);

    Optional<Book> getBookById(Long bookId);
}
