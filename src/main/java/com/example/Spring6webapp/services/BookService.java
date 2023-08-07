package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.Book;

public interface BookService {
    Iterable<Book> findAll();
}
