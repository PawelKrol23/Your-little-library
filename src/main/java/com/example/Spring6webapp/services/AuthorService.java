package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.book.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Page<Author> findAll(Integer pageNumber);

    Author createNewAuthor(Author author);

    Optional<Author> getAuthorById(Long authorId);

    Optional<Author> updateAuthorById(Author newAuthorData, Long authorId);

    void deleteAuthorById(Long authorId);

    List<Book> getBooksNotWrittenBy(Author foundAuthor);

    Optional<Author> addBookToAuthor(Long bookId, Long authorId);

    Optional<Author> removeBookFromAuthor(Long bookId, Long authorId);
}
