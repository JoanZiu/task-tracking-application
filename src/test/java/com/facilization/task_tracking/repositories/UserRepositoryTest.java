package com.facilization.task_tracking.repositories;

import com.facilization.task_tracking.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindById_shouldWork() {
        User user = new User();
        user.setUsername("eno");
        user.setEmail("eno@test.com");
        user.setPassword("secret123");
        user.setCreatedAt(LocalDateTime.now());
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findById(user.getId());

        assertTrue(found.isPresent());
        assertEquals("eno", found.get().getUsername());
    }
}