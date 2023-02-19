package com.example.Spring6webapp.services;

import com.example.Spring6webapp.domain.Author;

public interface AuthorService {

    Iterable<Author> findAll();
}
