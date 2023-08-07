package com.example.Spring6webapp.Bootstrap;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.author.Nationality;
import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.repositories.AuthorRepository;
import com.example.Spring6webapp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        Author author = new Author();
        author.setFirstName("Eric");
        author.setLastName("Evans");
        author.setNationality(Nationality.UNITED_KINGDOM);

        Book book = new Book();
        book.setTitle("Domain Driven Design");
        book.setIsbn("123456");

        Author authorSaved = authorRepository.save(author);
        Book bookSaved = bookRepository.save(book);

        Author author1 = new Author();
        author1.setFirstName("Rod");
        author1.setLastName("Johnson");
        author1.setNationality(Nationality.UNITED_KINGDOM);

        Author author2 = new Author();
        author2.setFirstName("Paweł");
        author2.setLastName("Król");
        author2.setNationality(Nationality.UNITED_KINGDOM);
        authorRepository.save(author2);

        Book book1 = new Book();
        book1.setTitle("J2EE Development without EJB");
        book1.setIsbn("54757585");

        Author author1Saved = authorRepository.save(author1);
        Book book1Saved = bookRepository.save(book1);

        authorSaved.getBooks().add(bookSaved);
        author1Saved.getBooks().add(book1Saved);
        bookSaved.getAuthors().add(authorSaved);
        book1Saved.getAuthors().add(author1Saved);

        authorRepository.save(authorSaved);
        authorRepository.save(author1Saved);
        bookRepository.save(bookSaved);
        bookRepository.save(book1Saved);

        System.out.println("In Bootstrap");
        System.out.println("Author count: " + authorRepository.count());
        System.out.println("Book count: " + bookRepository.count());
    }
}
