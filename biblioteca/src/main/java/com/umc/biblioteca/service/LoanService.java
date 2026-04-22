package com.umc.biblioteca.service;

import com.umc.biblioteca.model.Book;
import com.umc.biblioteca.model.Loan;
import com.umc.biblioteca.model.User;
import com.umc.biblioteca.repository.LoanRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;

    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public List<Loan> findOpenLoans() {
        return loanRepository.findByReturnedFalse();
    }

    public Loan createLoan(@NonNull String bookId, @NonNull String userId, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("O prazo do empréstimo deve ser maior que zero.");
        }

        Book book = bookService.findById(bookId);
        User user = userService.findById(userId);

        if (!user.isActive()) {
            throw new IllegalStateException("Usuário inativo não pode realizar empréstimos.");
        }

        if (book.getAvailableQuantity() <= 0) {
            throw new IllegalStateException("Livro indisponível para empréstimo.");
        }

        Loan loan = new Loan();
        loan.setBookId(book.getId());
        loan.setBookTitle(book.getTitle());
        loan.setUserId(user.getId());
        loan.setUserName(user.getName());
        LocalDate loanDate = LocalDate.now();
        loan.setLoanDate(loanDate);
        loan.setDueDate(loanDate.plusDays(days));
        loan.setReturned(false);

        bookService.decrementAvailable(bookId);
        return loanRepository.save(loan);
    }

    public void returnLoan(@NonNull String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));

        if (loan.isReturned()) {
            throw new IllegalStateException("Este empréstimo já foi devolvido.");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);
        bookService.incrementAvailable(Objects.requireNonNull(loan.getBookId()));
    }
}
