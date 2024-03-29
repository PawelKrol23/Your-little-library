package com.example.yourlittlelibrary.services;

import com.example.yourlittlelibrary.models.author.Author;
import com.example.yourlittlelibrary.models.book.Book;
import com.example.yourlittlelibrary.models.book.Genre;
import com.example.yourlittlelibrary.repositories.AuthorRepository;
import com.example.yourlittlelibrary.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    // deleteBookById tests
    @Test
    void deleteBookById_should_notThrowAnything_when_bookFound() {
        // given
        final Long BOOK_ID = 2137L;
        given(bookRepository.existsById(eq(BOOK_ID))).willReturn(true);

        // when
        assertDoesNotThrow(() -> bookService.deleteBookById(BOOK_ID));

        // then
        verify(bookRepository, times(1)).existsById(eq(BOOK_ID));
        verify(bookRepository, times(1)).deleteById(eq(BOOK_ID));
    }

    @Test
    void deleteBookById_should_throwError_when_bookNotFound() {
        // given
        final Long BOOK_ID = 2137L;
        given(bookRepository.existsById(eq(BOOK_ID))).willReturn(false);

        // when
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookById(BOOK_ID));

        // then
        verify(bookRepository, times(1)).existsById(eq(BOOK_ID));
        verify(bookRepository, never()).deleteById(eq(BOOK_ID));
    }

    // getAuthorsNotOwningBook tests
    @Test
    void getAuthorsNotOwningBook_should_returnAuthors() {
        // given
        final Book testBook = getTestBook();
        final List<Author> expectedAuthorList = Collections.singletonList(getTestAuthor());
        given(authorRepository.findAuthorsByBooksNotContaining(same(testBook))).willReturn(expectedAuthorList);

        // when
        final var actualBookList = bookService.getAuthorsNotOwningBook(testBook);

        // then
        assertThat(actualBookList).isNotNull();
        assertThat(actualBookList).isSameAs(expectedAuthorList);
        verify(authorRepository, times(1)).findAuthorsByBooksNotContaining(same(testBook));
    }

    // addAuthorToBook tests
    @Test
    void addAuthorToBook_should_returnBook_when_authorAndBookFound() {
        // given
        final Long ID = 2137L;
        final Author author = getTestAuthor();
        final Book book = getTestBook();

        given(bookRepository.findById(eq(ID))).willReturn(Optional.of(book));
        given(authorRepository.findById(eq(ID))).willReturn(Optional.of(author));
        given(authorRepository.save(same(author))).willReturn(author);
        given(bookRepository.save(same(book))).willReturn(book);

        // when
        final var actualBookOptional = bookService.addAuthorToBook(ID, ID);

        // then
        assertThat(actualBookOptional.isPresent()).isTrue();
        final var actualBook = actualBookOptional.get();
        assertThat(actualBook.getAuthors()).contains(author);
        assertThat(author.getBooks()).contains(book);

        verify(bookRepository, times(1)).findById(eq(ID));
        verify(authorRepository, times(1)).findById(eq(ID));
        verify(authorRepository, times(1)).save(same(author));
        verify(bookRepository, times(1)).save(same(book));
    }

    @Test
    void addAuthorToBook_should_returnEmptyOptional_when_bookNotFound() {
        // given
        final Long ID = 2137L;
        given(bookRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        final var actualBookOptional = bookService.addAuthorToBook(ID, ID);

        // then
        assertThat(actualBookOptional.isEmpty()).isTrue();

        verify(bookRepository, times(1)).findById(eq(ID));
        verify(authorRepository, never()).findById(eq(ID));
        verify(authorRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addAuthorToBook_should_throwError_when_authorNotExists() {
        // given
        final Long ID = 2137L;
        final Book book = getTestBook();
        given(bookRepository.findById(eq(ID))).willReturn(Optional.of(book));
        given(authorRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> bookService.addAuthorToBook(ID, ID));

        // then
        verify(bookRepository, times(1)).findById(eq(ID));
        verify(authorRepository, times(1)).findById(eq(ID));
        verify(authorRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    // removeAuthorFromBook tests
    @Test
    void removeAuthorFromBook_should_returnBook_when_authorAndBookFound() {
        // given
        final Long ID = 2137L;
        final Author author = getTestAuthor();
        final Book book = getTestBook();

        author.getBooks().add(book);
        book.getAuthors().add(author);

        given(bookRepository.findById(eq(ID))).willReturn(Optional.of(book));
        given(authorRepository.findById(eq(ID))).willReturn(Optional.of(author));
        given(authorRepository.save(same(author))).willReturn(author);
        given(bookRepository.save(same(book))).willReturn(book);

        // when
        final var actualBookOptional = bookService.removeAuthorFromBook(ID, ID);

        // then
        assertThat(actualBookOptional.isPresent()).isTrue();
        final var actualBook = actualBookOptional.get();
        assertThat(actualBook.getAuthors()).doesNotContain(author);
        assertThat(author.getBooks()).doesNotContain(book);

        verify(bookRepository, times(1)).findById(eq(ID));
        verify(authorRepository, times(1)).findById(eq(ID));
        verify(authorRepository, times(1)).save(same(author));
        verify(bookRepository, times(1)).save(same(book));
    }

    @Test
    void removeAuthorFromBook_should_returnEmptyOptional_when_bookNotFound() {
        // given
        final Long ID = 2137L;
        given(bookRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        final var actualBookOptional = bookService.removeAuthorFromBook(ID, ID);

        // then
        assertThat(actualBookOptional.isEmpty()).isTrue();

        verify(bookRepository, times(1)).findById(eq(ID));
        verify(authorRepository, never()).findById(eq(ID));
        verify(authorRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void removeAuthorFromBook_should_throwError_when_authorNotExists() {
        // given
        final Long ID = 2137L;
        final Book book = getTestBook();
        given(bookRepository.findById(eq(ID))).willReturn(Optional.of(book));
        given(authorRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> bookService.removeAuthorFromBook(ID, ID));

        // then
        verify(bookRepository, times(1)).findById(eq(ID));
        verify(authorRepository, times(1)).findById(eq(ID));
        verify(authorRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }
}