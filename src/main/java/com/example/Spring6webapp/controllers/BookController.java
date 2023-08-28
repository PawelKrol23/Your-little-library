package com.example.Spring6webapp.controllers;

import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.models.book.Genre;
import com.example.Spring6webapp.services.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class BookController {

    // Dependencies
    private final BookService bookService;

    // Error messages
    public static final String BOOK_NOT_FOUND_ERROR_MESSAGE = "No author with such ID";

    // Paths
    public static final String BOOKS_PATH = "/books";
    public static final String BOOKS_ID_PATH = BOOKS_PATH + "/{bookId}";
    public static final String BOOKS_CREATE_PATH = BOOKS_PATH + "/create-new";
    public static final String BOOKS_EDIT_PATH = BOOKS_ID_PATH + "/edit";
    public static final String BOOKS_ADD_AUTHOR_PATH = BOOKS_ID_PATH + "/add-author";
    public static final String BOOKS_REMOVE_AUTHOR_PATH = BOOKS_ID_PATH + "/remove-author";

    @RequestMapping(BOOKS_PATH)
    public String getBooks(@RequestParam(required = false, defaultValue = "0") Integer page,
                           Model model) {
        if(page < 0) {
            page = 0;
        }

        Page<Book> bookPage = bookService.getBookPage(page);
        if(bookPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such a book page");
        }

        model.addAttribute("books", bookPage);
        return "book/list";
    }

    @GetMapping(BOOKS_ID_PATH)
    public String getSingleBook(@PathVariable Long bookId,
                                  Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_ERROR_MESSAGE));
        model.addAttribute("book", foundBook);

        return "book/single";
    }

    @GetMapping(BOOKS_CREATE_PATH)
    public String createNewBookForm(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("book", new Book());

        return "book/create";
    }

    @PostMapping(BOOKS_CREATE_PATH)
    public String createNewBook(@Valid @ModelAttribute("book") Book book,
                                  BindingResult result,
                                  Model model) {
        if(result.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "book/create";
        }

        Long newBookId = bookService.createNewBook(book).getId();
        return "redirect:/books/%d".formatted(newBookId);
    }

    @GetMapping(BOOKS_EDIT_PATH)
    public String editBookForm(@PathVariable Long bookId,
                               Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_ERROR_MESSAGE));

        model.addAttribute("book", foundBook);
        model.addAttribute("genres", Genre.values());

        return "book/edit";
    }

    @PutMapping(BOOKS_EDIT_PATH)
    public String updateBookById(@PathVariable Long bookId,
                                 @Valid @ModelAttribute("book") Book book,
                                 BindingResult result,
                                 Model model) {
        if(result.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "book/edit";
        }

        Book updatedBook = bookService.updateBookById(book, bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_ERROR_MESSAGE));

        return "redirect:/books/%d".formatted(updatedBook.getId());
    }

    @DeleteMapping(BOOKS_EDIT_PATH)
    public String deleteBookById(@PathVariable Long bookId) {

        try {
            bookService.deleteBookById(bookId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book already not exists");
        }

        return "redirect:/books";
    }

    @GetMapping(BOOKS_ADD_AUTHOR_PATH)
    public String addAuthorToBookPage(@PathVariable Long bookId,
                                      Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_ERROR_MESSAGE));
        model.addAttribute("book", foundBook);
        model.addAttribute("authors", bookService.getAuthorsNotOwningBook(foundBook));

        return "book/add_author";
    }

    @PutMapping(BOOKS_ADD_AUTHOR_PATH + "/{authorId}")
    public String addBookToAuthor(@PathVariable Long authorId,
                                  @PathVariable Long bookId) {

        Book updatedBook;
        try {
            updatedBook = bookService.addAuthorToBook(bookId, authorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_ERROR_MESSAGE));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, AuthorController.AUTHOR_NOT_FOUND_ERROR_MESSAGE);
        }

        return "redirect:/books/%d".formatted(updatedBook.getId());
    }

    @GetMapping(BOOKS_REMOVE_AUTHOR_PATH)
    public String removeAuthorFromBookPage(@PathVariable Long bookId,
                                           Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_ERROR_MESSAGE));
        model.addAttribute("book", foundBook);

        return "book/remove_author";
    }

    @DeleteMapping(BOOKS_REMOVE_AUTHOR_PATH + "/{authorId}")
    public String removeBookFromAuthor(@PathVariable Long authorId,
                                       @PathVariable Long bookId) {

        Book updatedBook;
        try {
            updatedBook = bookService.removeAuthorFromBook(bookId, authorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_ERROR_MESSAGE));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, AuthorController.AUTHOR_NOT_FOUND_ERROR_MESSAGE);
        }

        return "redirect:/books/%d".formatted(updatedBook.getId());
    }
}
