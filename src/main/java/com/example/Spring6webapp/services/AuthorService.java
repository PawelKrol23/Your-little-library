package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;

public interface AuthorService {

    Iterable<Author> findAll();

    Author createNewAuthor(Author author);
}
