package com.umc.biblioteca.service;

import com.umc.biblioteca.model.User;
import com.umc.biblioteca.model.UserRole;
import com.umc.biblioteca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.admin.password:admin123}")
    private String defaultAdminPassword;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(@NonNull String name, @NonNull String email, @NonNull String password, UserRole role) {
        String normalizedName = name.trim();
        String normalizedEmail = email.trim().toLowerCase();

        if (normalizedName.isBlank()) {
            throw new IllegalArgumentException("Informe o nome do usuário.");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail.");
        }

        User user = new User();
        user.setName(normalizedName);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role == null ? UserRole.LEITOR : role);
        user.setActive(true);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(@NonNull String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public void createDefaultAdminIfNotExists() {
        if (!userRepository.existsByEmailIgnoreCase("admin@biblioteca.com")) {
            register("Administrador", "admin@biblioteca.com", defaultAdminPassword, UserRole.ADMIN);
        }
    }
}
