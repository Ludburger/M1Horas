package com.umc.biblioteca.controller;

import com.umc.biblioteca.service.BookService;
import com.umc.biblioteca.service.ReservationService;
import com.umc.biblioteca.service.UserService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Validated
public class ReservationController {

    private final ReservationService reservationService;
    private final BookService bookService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, BookService bookService, UserService userService) {
        this.reservationService = reservationService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/reservations")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "reservations/list";
    }

    @GetMapping("/reservations/new")
    public String newReservation(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("users", userService.findAll());
        return "reservations/form";
    }

    @PostMapping("/reservations")
    public String saveReservation(@RequestParam @NotBlank(message = "Livro é obrigatório") String bookId,
                                  @RequestParam @NotBlank(message = "Usuário é obrigatório") String userId,
                                  RedirectAttributes redirectAttributes) {
        reservationService.createReservation(bookId, userId);
        redirectAttributes.addFlashAttribute("success", "Reserva criada com sucesso.");
        return "redirect:/reservations";
    }
}
