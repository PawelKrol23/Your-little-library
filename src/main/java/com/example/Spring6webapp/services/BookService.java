package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.book.Book;

public interface BookService {
    Iterable<Book> findAll();
}
