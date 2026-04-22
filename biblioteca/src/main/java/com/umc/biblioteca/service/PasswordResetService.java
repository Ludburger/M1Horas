package com.umc.biblioteca.service;

import com.umc.biblioteca.model.User;
import com.umc.biblioteca.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_BYTES = 32;
    private static final int TOKEN_EXPIRATION_MINUTES = 30;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.mail.from:no-reply@biblioteca.local}")
    private String fromEmail;

    public PasswordResetService(UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void requestPasswordReset(String email, String baseUrl) {
        userRepository.findByEmailIgnoreCase(email.trim().toLowerCase())
                .ifPresent(user -> {
                    String token = generateToken();
                    user.setPasswordResetToken(token);
                    user.setPasswordResetTokenExpiresAt(LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES));
                    userRepository.save(user);
                    sendResetEmail(user, buildResetLink(baseUrl, token));
                });
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Link de recuperacao invalido."));

        if (user.getPasswordResetTokenExpiresAt() == null ||
                user.getPasswordResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Link de recuperacao expirado. Solicite um novo link.");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        userRepository.save(user);
    }

    private String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String buildResetLink(String baseUrl, String token) {
        return baseUrl + "/reset-password?token=" + token;
    }

    private void sendResetEmail(User user, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Recuperacao de senha - Biblioteca");
        message.setText("""
                Ola, %s.

                Recebemos uma solicitacao para redefinir sua senha no sistema Biblioteca.

                Acesse o link abaixo para criar uma nova senha:
                %s

                Este link expira em %d minutos.

                Se voce nao solicitou a recuperacao, ignore este e-mail.
                """.formatted(user.getName(), resetLink, TOKEN_EXPIRATION_MINUTES));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            logger.warn("Nao foi possivel enviar o e-mail de recuperacao. Link gerado: {}", resetLink, ex);
        }
    }
}
