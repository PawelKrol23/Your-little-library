package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.repositories.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    void should_returnAuthorPage() {
        // given
        PageImpl<Author> expectedPage = new PageImpl<>(Collections.singletonList(getTestAuthor()));
        given(authorRepository.findAll(any())).willReturn(expectedPage);

        // when
        Page<Author> resultPage = authorService.getAuthorPage(0);

        // then
        assertThat(resultPage).isEqualTo(expectedPage);
        verify(authorRepository, times(1)).findAll(any());
    }

    @Test
    void should_returnCreatedAuthor() {
        // given
        Author expectedAuthor = getTestAuthor();
        given(authorRepository.save(any())).willReturn(expectedAuthor);

        // when
        Author resultAuthor = authorService.createNewAuthor(getTestAuthor());

        // then
        assertThat(resultAuthor).isEqualTo(expectedAuthor);
        verify(authorRepository, times(1)).save(any());
    }

//    @Test
//    void getAuthorById() {
//    }
//
//    @Test
//    void updateAuthorById() {
//    }
//
//    @Test
//    void deleteAuthorById() {
//    }
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