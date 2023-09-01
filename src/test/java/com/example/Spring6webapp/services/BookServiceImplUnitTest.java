package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.models.book.Genre;
import com.example.Spring6webapp.repositories.AuthorRepository;
import com.example.Spring6webapp.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplUnitTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookServiceImpl bookService;

    Author getTestAuthor() {
        return Author.builder()
                .firstName("test")
                .lastName("test")
                .build();
    }

    Book getTestBook() {
        return Book.builder()
                .title("test")
                .genre(Genre.FANTASY)
                .build();
    }

    // getBookPage tests
    @Test
    void getBookPage_should_returnBookPage() {
        // given
        final PageImpl<Book> expectedPage = new PageImpl<>(Collections.singletonList(getTestBook()));
        given(bookRepository.findAll(any())).willReturn(expectedPage);

        // when
        final var actualPage = bookService.getBookPage(0);

        // then
        assertThat(actualPage).isSameAs(expectedPage);
        verify(bookRepository, times(1)).findAll(any());
    }

    // createNewBook tests
    @Test
    void createNewBook_should_returnCreatedBook() {
        // given
        final Book expectedBook = getTestBook();
        given(bookRepository.save(any())).willReturn(expectedBook);

        // when
        final var actualBook = bookService.createNewBook(getTestBook());

        // then
        assertThat(actualBook).isSameAs(expectedBook);
        verify(bookRepository, times(1)).save(any());
    }

    // getBookById tests
    @Test
    void getBookById_should_returnBookOptionalFromRepository() {
        // given
        final Long BOOK_ID = 2137L;
        final Optional<Book> expectedBookOptional = Optional.of(getTestBook());
        given(bookRepository.findById(eq(BOOK_ID))).willReturn(expectedBookOptional);

        // when
        final var actualBookOptional = bookService.getBookById(BOOK_ID);

        // then
        assertThat(actualBookOptional).isSameAs(expectedBookOptional);
        verify(bookRepository, times(1)).findById(eq(BOOK_ID));
    }

    // updateBookById tests
    @Test
    void updateBookById_should_returnUpdatedBookOptional_when_BookFound() {
        // given
        final Long BOOK_ID = 2137L;
        final Optional<Book> bookOptional = Optional.of(getTestBook());
        given(bookRepository.findById(eq(BOOK_ID))).willReturn(bookOptional);

        final Book expectedBook = getTestBook();
        given(bookRepository.save(any())).willReturn(expectedBook);

        // when
        final var actualBookOptional = bookService.updateBookById(getTestBook(), BOOK_ID);

        // then
        assertThat(actualBookOptional.isPresent()).isTrue();
        assertThat(actualBookOptional.get()).isSameAs(expectedBook);
        verify(bookRepository, times(1)).findById(eq(BOOK_ID));
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void updateBookById_should_returnEmptyOptional_when_bookNotFound() {
        // given
        final Long BOOK_ID = 2137L;
        given(bookRepository.findById(eq(BOOK_ID))).willReturn(Optional.empty());

        // when
        final var actualBookOptional = bookService.updateBookById(getTestBook(), BOOK_ID);

        // then
        assertThat(actualBookOptional.isEmpty()).isTrue();
        verify(bookRepository, times(1)).findById(eq(BOOK_ID));
        verify(bookRepository, never()).save(any());
    }
//
//    @Test
//    void deleteBookById() {
//    }
//
//    @Test
//    void getAuthorsNotOwningBook() {
//    }
//
//    @Test
//    void addAuthorToBook() {
//    }
//
//    @Test
//    void removeAuthorFromBook() {
//    }
}