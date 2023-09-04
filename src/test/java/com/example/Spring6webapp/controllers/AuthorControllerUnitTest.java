package com.example.Spring6webapp.controllers;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.author.Nationality;
import com.example.Spring6webapp.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService service;

    Author getTestAuthor() {
        return Author.builder()
                .firstName("test")
                .lastName("test")
                .nationality(Nationality.UNITED_KINGDOM)
                .dateOfBirth(LocalDate.now())
                .build();
    }

    @Test
    void listAuthors_should_returnAuthorListView_when_authorPageIsNotEmpty() throws Exception {
        // given
        final var page = new PageImpl<>(Collections.singletonList(getTestAuthor()));
        given(service.getAuthorPage(eq(0))).willReturn(page);

        // when & then
        mockMvc.perform(get(AuthorController.AUTHORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("author/list"))
                .andExpect(model().attributeExists("authors"));
    }

    @Test
    void listAuthors_should_respondWith404_when_authorPageIsEmpty() throws Exception {
        // given
        final var page = new PageImpl<Author>(Collections.emptyList());
        given(service.getAuthorPage(eq(0))).willReturn(page);

        // when & then
        mockMvc.perform(get(AuthorController.AUTHORS_PATH))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSingleAuthor_should_returnSingleAuthorView_when_authorExists() throws Exception {
        // given
        final Long AUTHOR_ID = 2137L;
        final var author = getTestAuthor();
        given(service.getAuthorById(eq(AUTHOR_ID))).willReturn(Optional.of(author));

        // when & then
        mockMvc.perform(get(AuthorController.AUTHORS_PATH + "/%d".formatted(AUTHOR_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("author/single"))
                .andExpect(model().attributeExists("author"));
    }

    @Test
    void getSingleAuthor_should_respondWith404_when_authorNotExists() throws Exception {
        // given
        final Long AUTHOR_ID = 2137L;
        given(service.getAuthorById(eq(AUTHOR_ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(get(AuthorController.AUTHORS_PATH + "/%d".formatted(AUTHOR_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNewAuthorForm_should_returnNewAuthorForm() throws Exception {
        // given

        // when & then
        mockMvc.perform(get(AuthorController.AUTHORS_CREATE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("author/create"))
                .andExpect(model().attributeExists("author", "nationalities"));
    }

    @Test
    void createNewAuthor_shouldRedirectToAuthorPage_when_authorIsValid() throws Exception {
        // given
        final Long AUTHOR_ID = 2137L;
        Author author = getTestAuthor();
        author.setId(AUTHOR_ID);
        given(service.createNewAuthor(any())).willReturn(author);

        // when & then
        mockMvc.perform(post(AuthorController.AUTHORS_CREATE_PATH)
                        .param("firstName", author.getFirstName())
                        .param("lastName", author.getLastName())
                        .param("dateOfBirth", author.getDateOfBirth().toString())
                        .param("nationality", author.getNationality().name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", AuthorController.AUTHORS_PATH + "/%d".formatted(AUTHOR_ID)));
    }

    @Test
    void createNewAuthor_returnAuthorCreateForm_when_authorIsInvalid() throws Exception {
        // given
        Author author = getTestAuthor();
        author.setLastName("E");

        // when & then
        mockMvc.perform(post(AuthorController.AUTHORS_CREATE_PATH)
                        .param("firstName", author.getFirstName())
                        .param("lastName", author.getLastName())
                        .param("dateOfBirth", author.getDateOfBirth().toString())
                        .param("nationality", author.getNationality().name()))
                .andExpect(status().isOk())
                .andExpect(view().name("author/create"))
                .andExpect(model().attributeExists("nationalities"))
                .andExpect(model().hasErrors());
    }
//
//    @Test
//    void editAuthorForm() {
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
//    void addBookToAuthorPage() {
//    }
//
//    @Test
//    void addBookToAuthor() {
//    }
//
//    @Test
//    void removeBookFromAuthorPage() {
//    }
//
//    @Test
//    void removeBookFromAuthor() {
//    }
}