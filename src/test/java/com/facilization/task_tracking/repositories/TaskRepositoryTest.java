package com.facilization.task_tracking.repositories;

import com.facilization.task_tracking.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findByAssigneeId_shouldReturnUserTasks() {
        // ARRANGE — krijo te dhena testimi ne databaze

        // nje user (assignee)
        User user = new User();
        user.setUsername("eno");
        user.setEmail("eno@test.com");
        user.setPassword("secret123");
        user.setCreatedAt(LocalDateTime.now());
        entityManager.persist(user);

        // nje projekt (task duhet projekt)
        Project project = new Project();
        project.setName("Projekti");
        project.setDescription("test");
        project.setCreatedAt(LocalDateTime.now());
        project.setOwner(user);
        entityManager.persist(project);

        // nje task i caktuar user-it
        Task task = new Task();
        task.setTitle("Task i pare");
        task.setStatus(Status.TODO);
        task.setPriority(Priority.HIGH);
        task.setCreatedAt(LocalDateTime.now());
        task.setProject(project);
        task.setAssignee(user);
        entityManager.persist(task);

        entityManager.flush();   // detyron shkrimin ne databaze

        // ACT — thirr metoden e repository-t
        List<Task> result = taskRepository.findByAssigneeId(user.getId());

        // ASSERT — kontrollo
        assertEquals(1, result.size());
        assertEquals("Task i pare", result.get(0).getTitle());
        assertEquals(user.getId(), result.get(0).getAssignee().getId());
    }

    @Test
    void findByDueDate_shouldReturnTasksDueOnThatDate() {
        // ARRANGE
        User user = new User();
        user.setUsername("eno");
        user.setEmail("eno@test.com");
        user.setPassword("secret123");
        user.setCreatedAt(LocalDateTime.now());
        entityManager.persist(user);

        Project project = new Project();
        project.setName("Projekti");
        project.setCreatedAt(LocalDateTime.now());
        project.setOwner(user);
        entityManager.persist(project);

        LocalDate today = LocalDate.now();

        Task task = new Task();
        task.setTitle("Task sot");
        task.setStatus(Status.TODO);
        task.setPriority(Priority.MEDIUM);
        task.setDueDate(today);                 // skadon sot
        task.setCreatedAt(LocalDateTime.now());
        task.setProject(project);
        entityManager.persist(task);

        entityManager.flush();

        // ACT
        List<Task> result = taskRepository.findByDueDate(today);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("Task sot", result.get(0).getTitle());
    }
}