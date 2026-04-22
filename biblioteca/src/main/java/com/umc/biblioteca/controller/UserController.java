package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.UserRole;
import com.umc.biblioteca.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", UserRole.values());
        return "users";
    }

    @PostMapping("/users")
    public String saveUser(@RequestParam @NotBlank(message = "Nome é obrigatório") String name,
                           @RequestParam @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
                           @RequestParam @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") String password,
                           @RequestParam UserRole role,
                           RedirectAttributes redirectAttributes) {
        userService.register(name, email, password, role);
        redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso.");
        return "redirect:/users";
    }
}
