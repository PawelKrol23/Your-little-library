package com.example.yourlittlelibrary.repositories;

import com.example.yourlittlelibrary.models.author.Author;
import com.example.yourlittlelibrary.models.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findAuthorsByBooksNotContaining(Book book);
    Page<Author> findAll(Pageable pageable);
}
