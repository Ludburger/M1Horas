package com.umc.biblioteca.service;

import com.umc.biblioteca.model.Book;
import com.umc.biblioteca.repository.BookRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(@NonNull String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));
    }

    public Book save(Book book) {
        book.setTitle(book.getTitle().trim());
        book.setAuthor(book.getAuthor().trim());
        book.setPublisher(book.getPublisher().trim());
        book.setIsbn(book.getIsbn().trim());

        if (book.getId() == null || book.getId().isBlank()) {
            book.setAvailableQuantity(book.getTotalQuantity());
        } else {
            Book currentBook = findById(book.getId());
            int borrowedQuantity = Math.max(0, currentBook.getTotalQuantity() - currentBook.getAvailableQuantity());

            if (book.getTotalQuantity() < borrowedQuantity) {
                throw new IllegalStateException("A quantidade total não pode ser menor que a quantidade emprestada.");
            }

            book.setAvailableQuantity(book.getTotalQuantity() - borrowedQuantity);
        }

        return bookRepository.save(book);
    }

    public void deleteById(@NonNull String id) {
        bookRepository.deleteById(id);
    }

    public void decrementAvailable(@NonNull String id) {
        Book book = findById(id);
        if (book.getAvailableQuantity() <= 0) {
            throw new IllegalStateException("Não há exemplares disponíveis para empréstimo.");
        }
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
    }

    public void incrementAvailable(@NonNull String id) {
        Book book = findById(id);
        if (book.getAvailableQuantity() < book.getTotalQuantity()) {
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            bookRepository.save(book);
        }
    }
}
