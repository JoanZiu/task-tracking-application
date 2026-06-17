package com.facilization.task_tracking.services;

import com.facilization.task_tracking.dto.CreateTaskRequest;
import com.facilization.task_tracking.dto.TaskResponse;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.models.*;
import com.facilization.task_tracking.repositories.ProjectRepository;
import com.facilization.task_tracking.repositories.TaskRepository;
import com.facilization.task_tracking.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private EmailService emailService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldReturnTaskResponse() {
        Project project = new Project();
        project.setId(1L);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task i pare");
        request.setDescription("test");
        request.setStatus(Status.TODO);
        request.setPriority(Priority.HIGH);
        request.setDueDate(LocalDate.now());

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Task i pare");
        savedTask.setStatus(Status.TODO);
        savedTask.setPriority(Priority.HIGH);
        savedTask.setCreatedAt(LocalDateTime.now());
        savedTask.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.createTask(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Task i pare", response.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getTaskById_whenExists_shouldReturnTask() {
        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task i pare");
        task.setStatus(Status.TODO);
        task.setPriority(Priority.HIGH);
        task.setCreatedAt(LocalDateTime.now());
        task.setProject(project);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Task i pare", response.getTitle());
    }

    @Test
    void getTaskById_whenNotFound_shouldThrowException() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(999L);
        });
    }
}