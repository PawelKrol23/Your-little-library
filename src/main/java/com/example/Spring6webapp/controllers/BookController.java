package com.example.Spring6webapp.controllers;

import com.example.Spring6webapp.models.book.Book;
import com.example.Spring6webapp.models.book.Genre;
import com.example.Spring6webapp.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping("/books")
    public String getBooks(Model model) {

        model.addAttribute("books", bookService.findAll());

        return "book/list";
    }

    @GetMapping("/books/create-new")
    public String createNewAuthorForm(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("book", new Book());

        return "book/create";
    }

    @PostMapping("/books/create-new")
    public String createNewAuthor(@Valid @ModelAttribute("book") Book book,
                                  BindingResult result,
                                  Model model) {
        if(result.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "book/create";
        }

        bookService.createNewBook(book);
        return "redirect:/books";
    }
}
