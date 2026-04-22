package com.umc.biblioteca.service;

import com.umc.biblioteca.model.User;
import com.umc.biblioteca.model.UserRole;
import com.umc.biblioteca.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.LEITOR);
        user.setActive(true);
    }

    @Test
    void register_ShouldCreateUser_WhenValidData() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.register("Test User", "test@example.com", "password", UserRole.LEITOR);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userService.register("Test User", "test@example.com", "password", UserRole.LEITOR));

        assertEquals("Já existe um usuário com este e-mail.", exception.getMessage());
    }

    @Test
    void register_ShouldThrowException_WhenPasswordTooShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userService.register("Test User", "test@example.com", "123", UserRole.LEITOR));

        assertEquals("A senha deve ter pelo menos 6 caracteres.", exception.getMessage());
    }

    @Test
    void findAll_ShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User result = userService.findById("1");

        assertEquals("Test User", result.getName());
    }

    @Test
    void findById_ShouldThrowException_WhenNotExists() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userService.findById("1"));

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }
}