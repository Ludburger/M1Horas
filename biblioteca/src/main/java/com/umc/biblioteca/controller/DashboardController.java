package com.umc.biblioteca.controller;

import com.umc.biblioteca.service.BookService;
import com.umc.biblioteca.service.LoanService;
import com.umc.biblioteca.service.ReservationService;
import com.umc.biblioteca.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final BookService bookService;
    private final LoanService loanService;
    private final ReservationService reservationService;
    private final UserService userService;

    public DashboardController(BookService bookService, LoanService loanService, ReservationService reservationService, UserService userService) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("bookCount", bookService.findAll().size());
        model.addAttribute("userCount", userService.findAll().size());
        model.addAttribute("loanCount", loanService.findOpenLoans().size());
        model.addAttribute("reservationCount", reservationService.findAll().size());
        return "dashboard";
    }
}
