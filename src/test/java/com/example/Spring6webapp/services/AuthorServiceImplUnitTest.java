package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.models.book.Genre;
import com.example.Spring6webapp.repositories.AuthorRepository;
import com.example.Spring6webapp.repositories.BookRepository;
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
class AuthorServiceImplUnitTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    AuthorServiceImpl authorService;

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

    // getAuthorPage tests
    @Test
    void getAuthorPage_should_returnAuthorPage() {
        // given
        final PageImpl<Author> expectedPage = new PageImpl<>(Collections.singletonList(getTestAuthor()));
        given(authorRepository.findAll(any())).willReturn(expectedPage);

        // when
        final var actualPage = authorService.getAuthorPage(0);

        // then
        assertThat(actualPage).isSameAs(expectedPage);
        verify(authorRepository, times(1)).findAll(any());
    }

    // createNewAuthor tests
    @Test
    void createNewAuthor_should_returnCreatedAuthor() {
        // given
        final Author expectedAuthor = getTestAuthor();
        given(authorRepository.save(any())).willReturn(expectedAuthor);

        // when
        final var actualAuthor = authorService.createNewAuthor(getTestAuthor());

        // then
        assertThat(actualAuthor).isSameAs(expectedAuthor);
        verify(authorRepository, times(1)).save(any());
    }

    // getAuthorById tests
    @Test
    void getAuthorById_should_returnAuthorOptionalFromRepository() {
        // given
        final Long AUTHOR_ID = 2137L;
        final Optional<Author> expectedAuthorOptional = Optional.of(getTestAuthor());
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(expectedAuthorOptional);

        // when
        final var actualAuthorOptional = authorService.getAuthorById(AUTHOR_ID);

        // then
        assertThat(actualAuthorOptional).isSameAs(expectedAuthorOptional);
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
    }

    // updateAuthorById tests
    @Test
    void updateAuthorById_should_returnUpdatedAuthorOptional_when_authorFound() {
        // given
        final Long AUTHOR_ID = 2137L;
        final Optional<Author> authorOptional = Optional.of(getTestAuthor());
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(authorOptional);

        Author expectedAuthor = getTestAuthor();
        given(authorRepository.save(any())).willReturn(expectedAuthor);

        // when
        final var actualAuthorOptional = authorService.updateAuthorById(getTestAuthor(), AUTHOR_ID);

        // then
        assertThat(actualAuthorOptional.isPresent()).isTrue();
        assertThat(actualAuthorOptional.get()).isSameAs(expectedAuthor);
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
        verify(authorRepository, times(1)).save(any());
    }

    @Test
    void updateAuthorById_should_returnEmptyOptional_when_authorNotFound() {
        // given
        final Long AUTHOR_ID = 2137L;
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(Optional.empty());

        // when
        final var actualAuthorOptional = authorService.updateAuthorById(getTestAuthor(), AUTHOR_ID);

        // then
        assertThat(actualAuthorOptional.isEmpty()).isTrue();
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
        verify(authorRepository, never()).save(any());
    }

    // deleteAuthorById tests
    @Test
    void deleteAuthorById_should_notThrowAnything_when_authorFound() {
        // given
        final Long AUTHOR_ID = 2137L;
        final Author foundAuthor = getTestAuthor();
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(Optional.of(foundAuthor));

        // when
        assertDoesNotThrow(() -> authorService.deleteAuthorById(AUTHOR_ID));

        // then
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
        verify(authorRepository, times(1)).delete(same(foundAuthor));
    }

    @Test
    void deleteAuthorById_should_throwError_when_authorNotFound() {
        // given
        final Long AUTHOR_ID = 2137L;
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> authorService.deleteAuthorById(AUTHOR_ID));

        // then
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
        verify(authorRepository, never()).delete(any());
    }

    // getBooksNotWrittenBy tests
    @Test
    void getBooksNotWrittenBy_should_returnBooks() {
        // given
        final Author testAuthor = getTestAuthor();
        final List<Book> expectedBookList = Collections.singletonList(getTestBook());
        given(bookRepository.findBooksByAuthorsNotContaining(same(testAuthor))).willReturn(expectedBookList);

        // when
        final var actualBookList = authorService.getBooksNotWrittenBy(testAuthor);

        // then
        assertThat(actualBookList).isNotNull();
        assertThat(actualBookList).isSameAs(expectedBookList);
        verify(bookRepository, times(1)).findBooksByAuthorsNotContaining(same(testAuthor));
    }

    // addBookToAuthor tests
    @Test
    void addBookToAuthor_should_returnAuthor_when_authorAndBookFound() {
        // given
        final Long ID = 2137L;
        final Author author = getTestAuthor();
        final Book book = getTestBook();

        given(authorRepository.findById(eq(ID))).willReturn(Optional.of(author));
        given(bookRepository.findById(eq(ID))).willReturn(Optional.of(book));
        given(bookRepository.save(same(book))).willReturn(book);
        given(authorRepository.save(same(author))).willReturn(author);

        // when
        final var actualAuthorOptional = authorService.addBookToAuthor(ID, ID);

        // then
        assertThat(actualAuthorOptional.isPresent()).isTrue();
        final var actualAuthor = actualAuthorOptional.get();
        assertThat(actualAuthor.getBooks()).contains(book);
        assertThat(book.getAuthors()).contains(author);

        verify(authorRepository, times(1)).findById(eq(ID));
        verify(bookRepository, times(1)).findById(eq(ID));
        verify(bookRepository, times(1)).save(same(book));
        verify(authorRepository, times(1)).save(same(author));
    }

    @Test
    void addBookToAuthor_should_returnEmptyOptional_when_authorNotFound() {
        // given
        final Long ID = 2137L;
        given(authorRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        final var actualAuthorOptional = authorService.addBookToAuthor(ID, ID);

        // then
        assertThat(actualAuthorOptional.isEmpty()).isTrue();

        verify(authorRepository, times(1)).findById(eq(ID));
        verify(bookRepository, never()).findById(eq(ID));
        verify(bookRepository, never()).save(any());
        verify(authorRepository, never()).save(any());
    }

    @Test
    void addBookToAuthor_should_throwError_when_bookNotExists() {
        // given
        final Long ID = 2137L;
        final Author author = getTestAuthor();
        given(authorRepository.findById(eq(ID))).willReturn(Optional.of(author));
        given(bookRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> authorService.addBookToAuthor(ID, ID));

        // then
        verify(authorRepository, times(1)).findById(eq(ID));
        verify(bookRepository, times(1)).findById(eq(ID));
        verify(bookRepository, never()).save(any());
        verify(authorRepository, never()).save(any());
    }

    // removeBookFromAuthor tests
    @Test
    void removeBookFromAuthor_should_returnAuthor_when_authorAndBookFound() {
        // given
        final Long ID = 2137L;
        final Author author = getTestAuthor();
        final Book book = getTestBook();

        author.getBooks().add(book);
        book.getAuthors().add(author);

        given(authorRepository.findById(eq(ID))).willReturn(Optional.of(author));
        given(bookRepository.findById(eq(ID))).willReturn(Optional.of(book));
        given(bookRepository.save(same(book))).willReturn(book);
        given(authorRepository.save(same(author))).willReturn(author);

        // when
        final var actualAuthorOptional = authorService.removeBookFromAuthor(ID, ID);

        // then
        assertThat(actualAuthorOptional.isPresent()).isTrue();
        final var actualAuthor = actualAuthorOptional.get();
        assertThat(actualAuthor.getBooks()).doesNotContain(book);
        assertThat(book.getAuthors()).doesNotContain(author);

        verify(authorRepository, times(1)).findById(eq(ID));
        verify(bookRepository, times(1)).findById(eq(ID));
        verify(bookRepository, times(1)).save(same(book));
        verify(authorRepository, times(1)).save(same(author));
    }

    @Test
    void removeBookFromAuthor_should_returnEmptyOptional_when_authorNotFound() {
        // given
        final Long ID = 2137L;
        given(authorRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        final var actualAuthorOptional = authorService.removeBookFromAuthor(ID, ID);

        // then
        assertThat(actualAuthorOptional.isEmpty()).isTrue();

        verify(authorRepository, times(1)).findById(eq(ID));
        verify(bookRepository, never()).findById(eq(ID));
        verify(bookRepository, never()).save(any());
        verify(authorRepository, never()).save(any());
    }

    @Test
    void removeBookFromAuthor_should_throwError_when_bookNotExists() {
        // given
        final Long ID = 2137L;
        final Author author = getTestAuthor();
        given(authorRepository.findById(eq(ID))).willReturn(Optional.of(author));
        given(bookRepository.findById(eq(ID))).willReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> authorService.removeBookFromAuthor(ID, ID));

        // then
        verify(authorRepository, times(1)).findById(eq(ID));
        verify(bookRepository, times(1)).findById(eq(ID));
        verify(bookRepository, never()).save(any());
        verify(authorRepository, never()).save(any());
    }
}