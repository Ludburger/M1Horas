package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByPasswordResetToken(String passwordResetToken);
    boolean existsByEmailIgnoreCase(String email);
}
