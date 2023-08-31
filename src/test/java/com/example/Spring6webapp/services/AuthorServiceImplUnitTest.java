package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.repositories.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
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

    @InjectMocks
    AuthorServiceImpl authorService;

    Author getTestAuthor() {
        return Author.builder()
                .firstName("test")
                .lastName("test")
                .build();
    }

    // getAuthorPage tests
    @Test
    void should_returnAuthorPage() {
        // given
        final PageImpl<Author> expectedPage = new PageImpl<>(Collections.singletonList(getTestAuthor()));
        given(authorRepository.findAll(any())).willReturn(expectedPage);

        // when
        final var resultPage = authorService.getAuthorPage(0);

        // then
        assertThat(resultPage).isSameAs(expectedPage);
        verify(authorRepository, times(1)).findAll(any());
    }

    // createNewAuthor tests
    @Test
    void should_returnCreatedAuthor() {
        // given
        final Author expectedAuthor = getTestAuthor();
        given(authorRepository.save(any())).willReturn(expectedAuthor);

        // when
        final var resultAuthor = authorService.createNewAuthor(getTestAuthor());

        // then
        assertThat(resultAuthor).isSameAs(expectedAuthor);
        verify(authorRepository, times(1)).save(any());
    }

    // getAuthorById tests
    @Test
    void should_returnAuthorOptionalFromRepository() {
        // given
        final Long AUTHOR_ID = 2137L;
        final Optional<Author> expectedAuthorOptional = Optional.of(getTestAuthor());
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(expectedAuthorOptional);

        // when
        final var resultAuthorOptional = authorService.getAuthorById(AUTHOR_ID);

        // then
        assertThat(resultAuthorOptional).isSameAs(expectedAuthorOptional);
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
    }

    // updateAuthorById tests
    @Test
    void should_returnUpdatedAuthorOptional_when_authorFound() {
        // given
        final Long AUTHOR_ID = 2137L;
        final Optional<Author> authorOptional = Optional.of(getTestAuthor());
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(authorOptional);

        Author expectedAuthor = getTestAuthor();
        given(authorRepository.save(any())).willReturn(expectedAuthor);

        // when
        final var resultAuthorOptional = authorService.updateAuthorById(getTestAuthor(), AUTHOR_ID);

        // then
        assertThat(resultAuthorOptional.isPresent()).isTrue();
        assertThat(resultAuthorOptional.get()).isSameAs(expectedAuthor);
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
        verify(authorRepository, times(1)).save(any());
    }

    @Test
    void should_returnEmptyOptional_when_authorNotFound() {
        // given
        final Long AUTHOR_ID = 2137L;
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(Optional.empty());

        // when
        final var resultAuthorOptional = authorService.updateAuthorById(getTestAuthor(), AUTHOR_ID);

        // then
        assertThat(resultAuthorOptional.isEmpty()).isTrue();
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
        verify(authorRepository, never()).save(any());
    }

    // deleteAuthorById tests
    @Test
    void should_notThrowAnything_when_authorFound() {
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
    void should_throwError_when_authorNotFound() {
        // given
        final Long AUTHOR_ID = 2137L;
        given(authorRepository.findById(eq(AUTHOR_ID))).willReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> authorService.deleteAuthorById(AUTHOR_ID));

        // then
        verify(authorRepository, times(1)).findById(eq(AUTHOR_ID));
        verify(authorRepository, never()).delete(any());
    }
//
//    @Test
//    void getBooksNotWrittenBy() {
//    }
//
//    @Test
//    void addBookToAuthor() {
//    }
//
//    @Test
//    void removeBookFromAuthor() {
//    }
}