package com.example.yourlittlelibrary.repositories;

import com.example.yourlittlelibrary.models.author.Author;
import com.example.yourlittlelibrary.models.author.Nationality;
import com.example.yourlittlelibrary.models.book.Book;
import com.example.yourlittlelibrary.models.book.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

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
    void findBooksByAuthorsNotContaining_should_returnBooksNotWrittenByAuthor() {
        // given
        Author author = getTestAuthor();
        Book book1 = getTestBook(), book2 = getTestBook();

        author.getBooks().add(book1);
        book1.getAuthors().add(author);

        entityManager.persist(author);
        entityManager.persist(book1);
        entityManager.persist(book2);

        // when
        final List<Book> actualBookList = bookRepository.findBooksByAuthorsNotContaining(author);

        // then
        assertThat(actualBookList).isNotNull();
        assertThat(actualBookList).contains(book2);
        assertThat(actualBookList).doesNotContain(book1);
    }

    @Test
    void findAll_should_returnPageOfBooks() {
        // given
        final int PAGE_SIZE = 1;
        Book book1 = getTestBook(), book2 = getTestBook();
        entityManager.persist(book1);
        entityManager.persist(book2);
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        // when
        final Page<Book> actualBookPage = bookRepository.findAll(pageRequest);

        // then
        assertThat(actualBookPage).isNotNull();
        assertThat(actualBookPage.getSize()).isEqualTo(PAGE_SIZE);
        assertThat(actualBookPage).contains(book2);
        assertThat(actualBookPage).doesNotContain(book1);
    }
}