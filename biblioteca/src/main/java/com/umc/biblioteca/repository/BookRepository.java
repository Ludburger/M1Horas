package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}
