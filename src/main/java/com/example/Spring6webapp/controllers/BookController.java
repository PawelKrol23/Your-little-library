package com.example.Spring6webapp.controllers;

import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.models.book.Genre;
import com.example.Spring6webapp.services.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping("/books")
    public String getBooks(Model model) {

        model.addAttribute("books", bookService.findAll());

        return "book/list";
    }

    @GetMapping("/books/{bookId}")
    public String getSingleBook(@PathVariable Long bookId,
                                  Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        model.addAttribute("book", foundBook);

        return "book/single";
    }

    @GetMapping("/books/create-new")
    public String createNewBookForm(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("book", new Book());

        return "book/create";
    }

    @PostMapping("/books/create-new")
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

    @GetMapping("/books/{bookId}/edit")
    public String editBookForm(@PathVariable Long bookId,
                               Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        model.addAttribute("book", foundBook);
        model.addAttribute("genres", Genre.values());

        return "book/edit";
    }

    @PutMapping("/books/{bookId}/edit")
    public String updateBookById(@PathVariable Long bookId,
                                 @Valid @ModelAttribute("book") Book book,
                                 BindingResult result,
                                 Model model) {
        if(result.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "book/edit";
        }

        Book updatedBook = bookService.updateBookById(book, bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        return "redirect:/books/%d".formatted(updatedBook.getId());
    }

    @DeleteMapping("/books/{bookId}/edit")
    public String deleteBookById(@PathVariable Long bookId) {

        try {
            bookService.deleteBookById(bookId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book already not exists");
        }

        return "redirect:/books";
    }

    @GetMapping("/books/{bookId}/add-author")
    public String addAuthorToBookPage(@PathVariable Long bookId,
                                      Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        model.addAttribute("book", foundBook);
        model.addAttribute("authors", bookService.getAuthorsNotOwningBook(foundBook));

        return "book/add_author";
    }

    @PutMapping("/books/{bookId}/add-author/{authorId}")
    public String addBookToAuthor(@PathVariable Long authorId,
                                  @PathVariable Long bookId) {

        Book updatedBook;
        try {
            updatedBook = bookService.addAuthorToBook(bookId, authorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }

        return "redirect:/books/%d".formatted(updatedBook.getId());
    }

    @GetMapping("/books/{bookId}/remove-author")
    public String removeAuthorFromBookPage(@PathVariable Long bookId,
                                           Model model) {
        Book foundBook = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        model.addAttribute("book", foundBook);

        return "book/remove_author";
    }

    @DeleteMapping("/books/{bookId}/remove-author/{authorId}")
    public String removeBookFromAuthor(@PathVariable Long authorId,
                                       @PathVariable Long bookId) {

        Book updatedBook;
        try {
            updatedBook = bookService.removeAuthorFromBook(bookId, authorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }

        return "redirect:/books/%d".formatted(updatedBook.getId());
    }
}
