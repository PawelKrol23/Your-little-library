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
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final static Integer PAGE_SIZE = 12;
    private final static Sort SORT = Sort.by(Sort.Direction.DESC, "createdAt");

    @Override
    public Page<Author> getAuthorPage(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, SORT);
        return authorRepository.findAll(pageRequest);
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

    @Override
    public Optional<Author> removeBookFromAuthor(Long bookId, Long authorId) {
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

        foundBook.getAuthors().remove(foundAuthor);
        foundAuthor.getBooks().remove(foundBook);

        bookRepository.save(foundBook);
        return Optional.of(authorRepository.save(foundAuthor));
    }
}
