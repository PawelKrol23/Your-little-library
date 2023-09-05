package com.example.Spring6webapp.controllers;

import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.models.book.Genre;
import com.example.Spring6webapp.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService service;

    Book getTestBook() {
        return Book.builder()
                .title("test")
                .publicationYear(2020)
                .genre(Genre.FANTASY)
                .build();
    }

    @Test
    void getBooks_should_returnBookListView_when_bookPageIsNotEmpty() throws Exception {
        // given
        final var page = new PageImpl<>(Collections.singletonList(getTestBook()));
        given(service.getBookPage(eq(0))).willReturn(page);

        // when & then
        mockMvc.perform(get(BookController.BOOKS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("book/list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void getBooks_should_respondWith404_when_bookPageIsEmpty() throws Exception {
        // given
        final var page = new PageImpl<Book>(Collections.emptyList());
        given(service.getBookPage(eq(0))).willReturn(page);

        // when & then
        mockMvc.perform(get(BookController.BOOKS_PATH))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSingleBook_should_returnSingleBookView_when_bookExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        final var book = getTestBook();
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.of(book));

        // when & then
        mockMvc.perform(get(BookController.BOOKS_ID_PATH, BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("book/single"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void getSingleBook_should_respondWith404_when_bookNotExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(get(BookController.BOOKS_ID_PATH, BOOK_ID))
                .andExpect(status().isNotFound());
    }
//
//    @Test
//    void createNewBookForm() {
//    }
//
//    @Test
//    void createNewBook() {
//    }
//
//    @Test
//    void editBookForm() {
//    }
//
//    @Test
//    void updateBookById() {
//    }
//
//    @Test
//    void deleteBookById() {
//    }
//
//    @Test
//    void addAuthorToBookPage() {
//    }
//
//    @Test
//    void addBookToAuthor() {
//    }
//
//    @Test
//    void removeAuthorFromBookPage() {
//    }
//
//    @Test
//    void removeBookFromAuthor() {
//    }
}