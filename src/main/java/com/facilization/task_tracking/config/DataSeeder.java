package com.facilization.task_tracking.config;

import com.facilization.task_tracking.models.User;
import com.facilization.task_tracking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("joan").isEmpty()) {
            User joan = new User();
            joan.setUsername("joan");
            joan.setEmail("joan@test.com");
            joan.setPassword(passwordEncoder.encode("joan12345"));   // enkriptohet
            joan.setCreatedAt(LocalDateTime.now());
            userRepository.save(joan);
        }
    }
}