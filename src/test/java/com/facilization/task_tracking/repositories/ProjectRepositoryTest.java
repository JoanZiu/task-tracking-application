package com.facilization.task_tracking.repositories;

import com.facilization.task_tracking.models.Project;
import com.facilization.task_tracking.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void saveAndFindById_shouldWork() {
        User owner = new User();
        owner.setUsername("eno");
        owner.setEmail("eno@test.com");
        owner.setPassword("secret123");
        owner.setCreatedAt(LocalDateTime.now());
        entityManager.persist(owner);

        Project project = new Project();
        project.setName("Projekti");
        project.setDescription("test");
        project.setCreatedAt(LocalDateTime.now());
        project.setOwner(owner);
        entityManager.persist(project);
        entityManager.flush();

        Optional<Project> found = projectRepository.findById(project.getId());

        assertTrue(found.isPresent());
        assertEquals("Projekti", found.get().getName());
    }
}