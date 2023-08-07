package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.Author;

public interface AuthorService {

    Iterable<Author> findAll();
}
