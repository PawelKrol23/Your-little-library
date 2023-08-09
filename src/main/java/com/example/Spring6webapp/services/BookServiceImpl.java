package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void createNewBook(Book book) {
        book.setId(null);
        bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }
}
