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
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> getAuthorById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public Optional<Author> updateAuthorById(Author newAuthorData, Long authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if(optionalAuthor.isEmpty()) {
            return Optional.empty();
        }

        Author authorToUpdate = optionalAuthor.get();
        authorToUpdate.setFirstName(newAuthorData.getFirstName());
        authorToUpdate.setLastName(newAuthorData.getLastName());
        authorToUpdate.setDateOfBirth(newAuthorData.getDateOfBirth());
        authorToUpdate.setNationality(newAuthorData.getNationality());
        Author savedAuthor = authorRepository.save(authorToUpdate);

        return Optional.of(savedAuthor);
    }
}
