package com.example.Spring6webapp.Bootstrap;

import com.example.Spring6webapp.domain.Author;
import com.example.Spring6webapp.domain.Book;
import com.example.Spring6webapp.repositories.AuthorRepository;
import com.example.Spring6webapp.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BootstrapData(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Author author = new Author();
        author.setFirstName("Eric");
        author.setLastName("Evans");

        Book book = new Book();
        book.setTitle("Domain Driven Design");
        book.setIsbn("123456");

        Author authorSaved = authorRepository.save(author);
        Book bookSaved = bookRepository.save(book);

        Author author1 = new Author();
        author1.setFirstName("Rod");
        author1.setLastName("Johnson");

        Book book1 = new Book();
        book1.setTitle("J2EE Development without EJB");
        book1.setIsbn("54757585");

        Author author1Saved = authorRepository.save(author1);
        Book book1Saved = bookRepository.save(book1);

        authorSaved.getBooks().add(book);
        author1Saved.getBooks().add(book1);

        authorRepository.save(authorSaved);
        authorRepository.save(author1Saved);

        System.out.println("In Bootstrap");
        System.out.println("Author count: " + authorRepository.count());
        System.out.println("Book count: " + bookRepository.count());
    }
}
