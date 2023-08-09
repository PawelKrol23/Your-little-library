package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.book.Book;

import java.util.Optional;

public interface BookService {
    Iterable<Book> findAll();

    Book createNewBook(Book book);

    Optional<Book> getBookById(Long bookId);

    Optional<Book> updateBookById(Book newBookData, Long bookId);

    void deleteBookById(Long bookId);
}
