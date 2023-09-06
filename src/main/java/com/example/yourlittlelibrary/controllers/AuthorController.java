package com.example.yourlittlelibrary.controllers;

import com.example.yourlittlelibrary.models.author.Author;
import com.example.yourlittlelibrary.models.author.Nationality;
import com.example.yourlittlelibrary.services.AuthorService;
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
public class AuthorController {

    // Dependencies
    private final AuthorService authorService;

    // Error messages
    public static final String AUTHOR_NOT_FOUND_ERROR_MESSAGE = "No author with such ID";

    // Paths
    public static final String AUTHORS_PATH = "/authors";
    public static final String AUTHORS_ID_PATH = AUTHORS_PATH + "/{authorId}";
    public static final String AUTHORS_CREATE_PATH = AUTHORS_PATH + "/create-new";
    public static final String AUTHORS_EDIT_PATH = AUTHORS_ID_PATH + "/edit";
    public static final String AUTHORS_ADD_BOOK_PATH = AUTHORS_ID_PATH + "/add-book";
    public static final String AUTHORS_REMOVE_BOOK_PATH = AUTHORS_ID_PATH + "/remove-book";

    @GetMapping(AUTHORS_PATH)
    public String listAuthors(@RequestParam(required = false, defaultValue = "0") Integer page,
                              Model model) {
        if(page < 0) {
            page = 0;
        }

        Page<Author> authorPage = authorService.getAuthorPage(page);
        if(authorPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such a author page");
        }

        model.addAttribute("authors", authorPage);
        return "author/list";
    }

    @GetMapping(AUTHORS_ID_PATH)
    public String getSingleAuthor(@PathVariable Long authorId,
                                  Model model) {
        Author foundAuthor = authorService.getAuthorById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AUTHOR_NOT_FOUND_ERROR_MESSAGE));
        model.addAttribute("author", foundAuthor);

        return "author/single";
    }

    @GetMapping(AUTHORS_CREATE_PATH)
    public String createNewAuthorForm(Model model) {
        model.addAttribute("nationalities", Nationality.values());
        model.addAttribute("author", new Author());

        return "author/create";
    }

    @PostMapping(AUTHORS_CREATE_PATH)
    public String createNewAuthor(@Valid @ModelAttribute("author") Author author,
                                  BindingResult result,
                                  Model model) {
        if(result.hasErrors()) {
            model.addAttribute("nationalities", Nationality.values());
            return "author/create";
        }

        Author createdAuthor = authorService.createNewAuthor(author);
        return "redirect:/authors/%d".formatted(createdAuthor.getId());
    }

    @GetMapping(AUTHORS_EDIT_PATH)
    public String editAuthorForm(@PathVariable Long authorId,
                                 Model model) {
        Author foundAuthor = authorService.getAuthorById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AUTHOR_NOT_FOUND_ERROR_MESSAGE));

        model.addAttribute("author", foundAuthor);
        model.addAttribute("nationalities", Nationality.values());

        return "author/edit";
    }

    @PutMapping(AUTHORS_EDIT_PATH)
    public String updateAuthorById(@PathVariable Long authorId,
                                   @Valid @ModelAttribute("author") Author author,
                                   BindingResult result,
                                   Model model) {
        if(result.hasErrors()) {
            model.addAttribute("nationalities", Nationality.values());
            return "author/edit";
        }

        Author updatedAuthor = authorService.updateAuthorById(author, authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AUTHOR_NOT_FOUND_ERROR_MESSAGE));

        return "redirect:/authors/%d".formatted(updatedAuthor.getId());
    }

    @DeleteMapping(AUTHORS_EDIT_PATH)
    public String deleteAuthorById(@PathVariable Long authorId) {

        try {
            authorService.deleteAuthorById(authorId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author already not exists");
        }

        return "redirect:/authors";
    }

    @GetMapping(AUTHORS_ADD_BOOK_PATH)
    public String addBookToAuthorPage(@PathVariable Long authorId,
                                      Model model) {
        Author foundAuthor = authorService.getAuthorById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AUTHOR_NOT_FOUND_ERROR_MESSAGE));
        model.addAttribute("author", foundAuthor);
        model.addAttribute("books", authorService.getBooksNotWrittenBy(foundAuthor));

        return "author/add_book";
    }

    @PutMapping(AUTHORS_ADD_BOOK_PATH + "/{bookId}")
    public String addBookToAuthor(@PathVariable Long authorId,
                                  @PathVariable Long bookId) {

        Author updatedAuthor;
        try {
            updatedAuthor = authorService.addBookToAuthor(bookId, authorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AUTHOR_NOT_FOUND_ERROR_MESSAGE));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, BookController.BOOK_NOT_FOUND_ERROR_MESSAGE);
        }

        return "redirect:/authors/%d".formatted(updatedAuthor.getId());
    }

    @GetMapping(AUTHORS_REMOVE_BOOK_PATH)
    public String removeBookFromAuthorPage(@PathVariable Long authorId,
                                           Model model) {
        Author foundAuthor = authorService.getAuthorById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AUTHOR_NOT_FOUND_ERROR_MESSAGE));
        model.addAttribute("author", foundAuthor);

        return "author/remove_book";
    }

    @DeleteMapping(AUTHORS_REMOVE_BOOK_PATH + "/{bookId}")
    public String removeBookFromAuthor(@PathVariable Long authorId,
                                       @PathVariable Long bookId) {

        Author updatedAuthor;
        try {
            updatedAuthor = authorService.removeBookFromAuthor(bookId, authorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AUTHOR_NOT_FOUND_ERROR_MESSAGE));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, BookController.BOOK_NOT_FOUND_ERROR_MESSAGE);
        }

        return "redirect:/authors/%d".formatted(updatedAuthor.getId());
    }
}
