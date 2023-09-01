package com.example.Spring6webapp.repositories;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.author.Nationality;
import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.models.book.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    Author getTestAuthor() {
        return Author.builder()
                .firstName("test")
                .lastName("test")
                .nationality(Nationality.UNITED_KINGDOM)
                .dateOfBirth(LocalDate.now())
                .build();
    }

    Book getTestBook() {
        return Book.builder()
                .title("test")
                .genre(Genre.FANTASY)
                .publicationYear(2002)
                .build();
    }

    @Test
    void findAuthorsByBooksNotContaining_should_returnAuthorsNotOwningBook() {
        // given
        Book book = getTestBook();
        Author author1 = getTestAuthor(), author2 = getTestAuthor();

        author1.getBooks().add(book);
        book.getAuthors().add(author1);

        entityManager.persist(book);
        entityManager.persist(author1);
        entityManager.persist(author2);

        // when
        final List<Author> actualAuthorList = authorRepository.findAuthorsByBooksNotContaining(book);

        // then
        assertThat(actualAuthorList).isNotNull();
        assertThat(actualAuthorList).contains(author2);
        assertThat(actualAuthorList).doesNotContain(author1);
    }

//    @Test
//    void findAll() {
//    }
}