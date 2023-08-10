package com.example.Spring6webapp.services;

import com.example.Spring6webapp.models.author.Author;
import com.example.Spring6webapp.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author createNewAuthor(Author author) {
        author.setId(null);
        Author createdAuthor = authorRepository.save(author);
        return createdAuthor;
    }

    @Override
    public Optional<Author> getAuthorById(Long authorId) {
        return authorRepository.findById(authorId);
    }
}
