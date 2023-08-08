package com.example.Spring6webapp.controllers;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.author.Nationality;
import com.example.Spring6webapp.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());

        return "author/list";
    }

    @GetMapping("/authors/create-new")
    public String createNewAuthorForm(Model model) {
        model.addAttribute("nationalities", Nationality.values());
        model.addAttribute("author", new Author());

        return "author/create";
    }

    @PostMapping("/authors/create-new")
    public String createNewAuthor(@Valid @ModelAttribute Author author,
                                  BindingResult result,
                                  Model model) {
        if(result.hasErrors()) {
            model.addAttribute("nationalities", Nationality.values());
            model.addAttribute("author", author);
            return "author/create";
        }

        authorService.createNewAuthor(author);
        return "redirect:/authors";
    }
}
