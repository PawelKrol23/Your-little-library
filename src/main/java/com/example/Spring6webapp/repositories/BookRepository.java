package com.example.Spring6webapp.repositories;

import com.example.Spring6webapp.models.book.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
