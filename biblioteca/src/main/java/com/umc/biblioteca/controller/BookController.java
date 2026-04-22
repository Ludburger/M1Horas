package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.Book;
import com.umc.biblioteca.service.BookService;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books/list";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/form";
    }

    @PostMapping("/books")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "books/form";
        }

        bookService.save(book);
        redirectAttributes.addFlashAttribute("success", "Livro salvo com sucesso.");
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable @NonNull String id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "books/form";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable @NonNull String id, RedirectAttributes redirectAttributes) {
        bookService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Livro removido com sucesso.");
        return "redirect:/books";
    }
}
