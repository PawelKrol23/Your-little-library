package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;

import java.util.Optional;

public interface AuthorService {

    Iterable<Author> findAll();

    Author createNewAuthor(Author author);

    Optional<Author> getAuthorById(Long authorId);

    Optional<Author> updateAuthorById(Author newAuthorData, Long authorId);

    void deleteAuthorById(Long authorId);
}
