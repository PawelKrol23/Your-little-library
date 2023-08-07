package com.example.Spring6webapp.repositories;

import com.example.Spring6webapp.models.author.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
