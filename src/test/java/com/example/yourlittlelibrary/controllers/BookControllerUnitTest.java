package com.example.yourlittlelibrary.controllers;

import com.example.yourlittlelibrary.models.book.Book;
import com.example.yourlittlelibrary.models.book.Genre;
import com.example.yourlittlelibrary.services.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerUnitTest {

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
    @Test
    void createNewBookForm_should_returnNewBookForm() throws Exception {
        // given

        // when & then
        mockMvc.perform(get(BookController.BOOKS_CREATE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name("book/create"))
                .andExpect(model().attributeExists("book", "genres"));
    }

    @Test
    void createNewBook_shouldRedirectToBookPage_when_bookIsValid() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        Book book = getTestBook();
        book.setId(BOOK_ID);
        given(service.createNewBook(any())).willReturn(book);

        // when & then
        mockMvc.perform(post(BookController.BOOKS_CREATE_PATH)
                        .param("title", book.getTitle())
                        .param("publicationYear", book.getPublicationYear().toString())
                        .param("genre", book.getGenre().name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", BookController.BOOKS_PATH + "/%d".formatted(BOOK_ID)));
    }

    @Test
    void createNewBook_returnBookCreateForm_when_bookIsInvalid() throws Exception {
        // given
        Book book = getTestBook();
        book.setTitle("E");

        // when & then
        mockMvc.perform(post(BookController.BOOKS_CREATE_PATH)
                        .param("title", book.getTitle())
                        .param("publicationYear", book.getPublicationYear().toString())
                        .param("genre", book.getGenre().name()))
                .andExpect(status().isOk())
                .andExpect(view().name("book/create"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().hasErrors());
    }
    @Test
    void editBookForm_should_returnBookEditForm_when_bookFound() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        Book book = getTestBook();
        book.setId(BOOK_ID);
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.of(book));

        // when & then
        mockMvc.perform(get(BookController.BOOKS_EDIT_PATH, BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"))
                .andExpect(model().attributeExists("book", "genres"));
    }

    @Test
    void editBookForm_should_respondWith404_when_bookNotFound() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(get(BookController.BOOKS_EDIT_PATH, BOOK_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBookById_should_redirectToBookPage_when_bookFoundAndIsValid() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        Book book = getTestBook();
        book.setId(BOOK_ID);
        given(service.updateBookById(any(), eq(BOOK_ID))).willReturn(Optional.of(book));

        // when & then
        mockMvc.perform(put(BookController.BOOKS_EDIT_PATH, BOOK_ID)
                        .param("title", book.getTitle())
                        .param("publicationYear", book.getPublicationYear().toString())
                        .param("genre", book.getGenre().name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", BookController.BOOKS_PATH + "/%d".formatted(BOOK_ID)));
    }

    @Test
    void updateBookById_should_respondWith404_when_bookNotFound() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        Book book = getTestBook();
        given(service.updateBookById(any(), eq(BOOK_ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(put(BookController.BOOKS_EDIT_PATH, BOOK_ID)
                        .param("title", book.getTitle())
                        .param("publicationYear", book.getPublicationYear().toString())
                        .param("genre", book.getGenre().name()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBookById_should_returnBookEditForm_when_bookFoundAndIsInvalid() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        Book book = getTestBook();
        book.setTitle("E");

        // when & then
        mockMvc.perform(put(BookController.BOOKS_EDIT_PATH, BOOK_ID)
                        .param("title", book.getTitle())
                        .param("publicationYear", book.getPublicationYear().toString())
                        .param("genre", book.getGenre().name()))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"))
                .andExpect(model().attributeExists("book", "genres"))
                .andExpect(model().hasErrors());
    }
    @Test
    void deleteBookById_should_returnBookListView_when_bookExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;

        // when & then
        mockMvc.perform(delete(BookController.BOOKS_EDIT_PATH, BOOK_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", BookController.BOOKS_PATH));
    }

    @Test
    void deleteBookById_should_respondWith404_when_bookNotExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        willThrow(new EntityNotFoundException("No book with such Id"))
                .given(service)
                .deleteBookById(eq(BOOK_ID));

        // when & then
        mockMvc.perform(delete(BookController.BOOKS_EDIT_PATH, BOOK_ID))
                .andExpect(status().isNotFound());
    }
    @Test
    void addAuthorToBookPage_should_returnAddAuthorView_when_bookExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        Book book = getTestBook();
        book.setId(BOOK_ID);
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.of(book));

        // when & then
        mockMvc.perform(get(BookController.BOOKS_ADD_AUTHOR_PATH, BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("book/add_author"))
                .andExpect(model().attributeExists("book", "authors"));
    }

    @Test
    void addAuthorToBookPage_should_respondWith404_when_bookNotExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(get(BookController.BOOKS_ADD_AUTHOR_PATH, BOOK_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAuthorToBook_should_redirectToBookPage_when_authorAndBookExists() throws Exception {
        // given
        final Long ID = 2137L;
        Book book = getTestBook();
        book.setId(ID);
        given(service.addAuthorToBook(eq(ID), eq(ID))).willReturn(Optional.of(book));

        // when & then
        mockMvc.perform(put(BookController.BOOKS_ADD_AUTHOR_PATH + "/{authorId}", ID, ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books/%d".formatted(ID)));
    }

    @Test
    void addBookToAuthor_should_respondWith404_when_authorNotExists() throws Exception {
        // given
        final Long ID = 2137L;
        given(service.addAuthorToBook(eq(ID), eq(ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(put(BookController.BOOKS_ADD_AUTHOR_PATH + "/{authorId}", ID, ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void addBookToAuthor_should_respondWith404_when_bookNotExists() throws Exception {
        // given
        final Long ID = 2137L;
        given(service.addAuthorToBook(eq(ID), eq(ID))).willThrow(new EntityNotFoundException("No author with such Id"));

        // when & then
        mockMvc.perform(put(BookController.BOOKS_ADD_AUTHOR_PATH + "/{authorId}", ID, ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeAuthorFromBookPage_should_returnRemoveAuthorView_when_bookExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        Book book = getTestBook();
        book.setId(BOOK_ID);
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.of(book));

        // when & then
        mockMvc.perform(get(BookController.BOOKS_REMOVE_AUTHOR_PATH, BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("book/remove_author"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void removeAuthorFromBookPage_should_respondWith404_when_bookNotExists() throws Exception {
        // given
        final Long BOOK_ID = 2137L;
        given(service.getBookById(eq(BOOK_ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(get(BookController.BOOKS_REMOVE_AUTHOR_PATH, BOOK_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeBookFromAuthor_should_redirectToBookPage_when_authorAndBookExists() throws Exception {
        // given
        final Long ID = 2137L;
        Book book = getTestBook();
        book.setId(ID);
        given(service.removeAuthorFromBook(eq(ID), eq(ID))).willReturn(Optional.of(book));

        // when & then
        mockMvc.perform(delete(BookController.BOOKS_REMOVE_AUTHOR_PATH + "/{authorId}", ID, ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books/%d".formatted(ID)));
    }

    @Test
    void removeBookFromAuthor_should_respondWith404_when_bookNotExists() throws Exception {
        // given
        final Long ID = 2137L;
        given(service.removeAuthorFromBook(eq(ID), eq(ID))).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(delete(BookController.BOOKS_REMOVE_AUTHOR_PATH + "/{authorId}", ID, ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeBookFromAuthor_should_respondWith404_when_authorNotExists() throws Exception {
        // given
        final Long ID = 2137L;
        given(service.removeAuthorFromBook(eq(ID), eq(ID))).willThrow(new EntityNotFoundException("No author with such Id"));

        // when & then
        mockMvc.perform(delete(BookController.BOOKS_REMOVE_AUTHOR_PATH + "/{authorId}", ID, ID))
                .andExpect(status().isNotFound());
    }
}