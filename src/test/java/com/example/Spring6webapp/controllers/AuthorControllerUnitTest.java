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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void createNewAuthorForm_should_returnNewAuthorForm() throws Exception {
        // given

        // when & then
        mockMvc.perform(get(AuthorController.AUTHORS_CREATE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("author/create"))
                .andExpect(model().attributeExists("author", "nationalities"));
    }
//
//    @Test
//    void createNewAuthor() {
//    }
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