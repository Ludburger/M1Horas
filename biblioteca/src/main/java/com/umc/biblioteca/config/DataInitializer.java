package com.umc.biblioteca.config;

import com.umc.biblioteca.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDefaultAdmin(UserService userService) {
        return args -> {
            try {
                userService.createDefaultAdminIfNotExists();
            } catch (DataAccessException ex) {
                logger.warn(
                        "MongoDB is unavailable, so the default admin user was not initialized. " +
                                "Start MongoDB and restart the application to create it.",
                        ex
                );
            }
        };
    }
}
