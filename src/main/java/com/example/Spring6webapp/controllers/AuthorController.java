package com.example.Spring6webapp.controllers;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.models.author.Nationality;
import com.example.Spring6webapp.services.AuthorService;
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
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());

        return "author/list";
    }

    @GetMapping("/authors/{authorId}")
    public String getSingleAuthor(@PathVariable Long authorId,
                                  Model model) {
        Author foundAuthor = authorService.getAuthorById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        model.addAttribute("author", foundAuthor);

        return "author/single";
    }

    @GetMapping("/authors/create-new")
    public String createNewAuthorForm(Model model) {
        model.addAttribute("nationalities", Nationality.values());
        model.addAttribute("author", new Author());

        return "author/create";
    }

    @PostMapping("/authors/create-new")
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

    @GetMapping("/authors/{authorId}/edit")
    public String editAuthorForm(@PathVariable Long authorId,
                                 Model model) {
        Author foundAuthor = authorService.getAuthorById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        model.addAttribute("author", foundAuthor);
        model.addAttribute("nationalities", Nationality.values());

        return "author/edit";
    }

    @PutMapping("/authors/{authorId}/edit")
    public String updateAuthorById(@PathVariable Long authorId,
                                   @Valid @ModelAttribute("author") Author author,
                                   BindingResult result,
                                   Model model) {
        if(result.hasErrors()) {
            model.addAttribute("nationalities", Nationality.values());
            return "author/edit";
        }

        Author updatedAuthor = authorService.updateAuthorById(author, authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        return "redirect:/authors/%d".formatted(updatedAuthor.getId());
    }

    @DeleteMapping("/authors/{authorId}/edit")
    public String deleteAuthorById(@PathVariable Long authorId) {

        try {
            authorService.deleteAuthorById(authorId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author already not exists");
        }

        return "redirect:/authors";
    }
}
