package com.example.yourlittlelibrary.repositories;

import com.example.yourlittlelibrary.models.author.Author;
import com.example.yourlittlelibrary.models.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findBooksByAuthorsNotContaining(Author author);
    Page<Book> findAll(Pageable pageable);
}
