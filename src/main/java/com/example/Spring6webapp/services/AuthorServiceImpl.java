package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.repositories.AuthorRepository;
import com.example.Spring6webapp.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author createNewAuthor(Author author) {
        author.setId(null);
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> getAuthorById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public Optional<Author> updateAuthorById(Author newAuthorData, Long authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if(optionalAuthor.isEmpty()) {
            return Optional.empty();
        }

        Author authorToUpdate = optionalAuthor.get();
        authorToUpdate.setFirstName(newAuthorData.getFirstName());
        authorToUpdate.setLastName(newAuthorData.getLastName());
        authorToUpdate.setDateOfBirth(newAuthorData.getDateOfBirth());
        authorToUpdate.setNationality(newAuthorData.getNationality());
        Author savedAuthor = authorRepository.save(authorToUpdate);

        return Optional.of(savedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthorById(Long authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if(optionalAuthor.isEmpty()) {
            throw new EntityNotFoundException("No author with such Id");
        }

        Author foundAuthor = optionalAuthor.get();
        authorRepository.delete(foundAuthor);
    }

    @Override
    public List<Book> getBooksNotWrittenBy(Author author) {
        return bookRepository.findBooksByAuthorsNotContaining(author);
    }

    @Override
    public Optional<Author> addBookToAuthor(Long bookId, Long authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if(optionalAuthor.isEmpty()) {
            return Optional.empty();
        }

        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isEmpty()) {
            throw new EntityNotFoundException("No book with such Id");
        }

        Author foundAuthor = optionalAuthor.get();
        Book foundBook = optionalBook.get();

        foundBook.getAuthors().add(foundAuthor);
        foundAuthor.getBooks().add(foundBook);

        bookRepository.save(foundBook);
        return Optional.of(authorRepository.save(foundAuthor));
    }
}
