package com.umc.biblioteca.controller;

import com.umc.biblioteca.service.BookService;
import com.umc.biblioteca.service.LoanService;
import com.umc.biblioteca.service.UserService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Validated
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final UserService userService;

    public LoanController(LoanService loanService, BookService bookService, UserService userService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/loans")
    public String listLoans(Model model) {
        model.addAttribute("loans", loanService.findAll());
        return "loans/list";
    }

    @GetMapping("/loans/new")
    public String newLoan(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("users", userService.findAll());
        return "loans/form";
    }

    @PostMapping("/loans")
    public String saveLoan(@RequestParam @NotBlank(message = "Livro é obrigatório") String bookId,
                           @RequestParam @NotBlank(message = "Usuário é obrigatório") String userId,
                           @RequestParam(defaultValue = "7") @Min(value = 1, message = "Dias deve ser maior que 0") int days,
                           RedirectAttributes redirectAttributes) {
        loanService.createLoan(bookId, userId, days);
        redirectAttributes.addFlashAttribute("success", "Empréstimo registrado com sucesso.");
        return "redirect:/loans";
    }

    @PostMapping("/loans/return/{id}")
    public String returnLoan(@PathVariable @NonNull String id, RedirectAttributes redirectAttributes) {
        loanService.returnLoan(id);
        redirectAttributes.addFlashAttribute("success", "Livro devolvido com sucesso.");
        return "redirect:/loans";
    }
}
