package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.book.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Page<Book> getBookPage(Integer page);

    Book createNewBook(Book book);

    Optional<Book> getBookById(Long bookId);

    Optional<Book> updateBookById(Book newBookData, Long bookId);

    void deleteBookById(Long bookId);

    List<Author> getAuthorsNotOwningBook(Book book);

    Optional<Book> addAuthorToBook(Long bookId, Long authorId);

    Optional<Book> removeAuthorFromBook(Long bookId, Long authorId);
}
