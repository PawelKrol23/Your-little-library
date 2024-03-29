package com.example.yourlittlelibrary.services;

import com.example.yourlittlelibrary.models.author.Author;
import com.example.yourlittlelibrary.models.book.Book;
import com.example.yourlittlelibrary.repositories.AuthorRepository;
import com.example.yourlittlelibrary.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final static Integer PAGE_SIZE = 12;
    private final static Sort SORT = Sort.by(Sort.Direction.DESC, "createdAt");

    @Override
    public Page<Book> getBookPage(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, SORT);
        return bookRepository.findAll(pageRequest);
    }

    @Override
    public Book createNewBook(Book book) {
        book.setId(null);
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    @Transactional
    public Optional<Book> updateBookById(Book newBookData, Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Book bookToUpdate = optionalBook.get();
        bookToUpdate.setTitle(newBookData.getTitle());
        bookToUpdate.setGenre(newBookData.getGenre());
        bookToUpdate.setPublicationYear(newBookData.getPublicationYear());
        Book savedBook = bookRepository.save(bookToUpdate);

        return Optional.of(savedBook);
    }

    @Override
    public void deleteBookById(Long bookId) {
        if(!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("No book with such Id");
        }

        bookRepository.deleteById(bookId);
    }

    @Override
    public List<Author> getAuthorsNotOwningBook(Book book) {
        return authorRepository.findAuthorsByBooksNotContaining(book);
    }

    @Override
    public Optional<Book> addAuthorToBook(Long bookId, Long authorId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if(optionalAuthor.isEmpty()) {
            throw new EntityNotFoundException("No Author with such Id");
        }

        Author foundAuthor = optionalAuthor.get();
        Book foundBook = optionalBook.get();

        foundBook.getAuthors().add(foundAuthor);
        foundAuthor.getBooks().add(foundBook);

        authorRepository.save(foundAuthor);
        return Optional.of(bookRepository.save(foundBook));
    }

    @Override
    public Optional<Book> removeAuthorFromBook(Long bookId, Long authorId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if(optionalAuthor.isEmpty()) {
            throw new EntityNotFoundException("No Author with such Id");
        }

        Author foundAuthor = optionalAuthor.get();
        Book foundBook = optionalBook.get();

        foundBook.getAuthors().remove(foundAuthor);
        foundAuthor.getBooks().remove(foundBook);

        authorRepository.save(foundAuthor);
        return Optional.of(bookRepository.save(foundBook));
    }
}
