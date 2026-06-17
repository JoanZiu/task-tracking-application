package com.facilization.task_tracking.services;

import com.facilization.task_tracking.dto.CreateProjectRequest;
import com.facilization.task_tracking.dto.ProjectResponse;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.models.Project;
import com.facilization.task_tracking.models.User;
import com.facilization.task_tracking.repositories.ProjectRepository;
import com.facilization.task_tracking.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_shouldReturnProjectResponse() {
        User owner = new User();
        owner.setId(1L);
        owner.setUsername("eno");

        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Projekti");
        request.setDescription("test");
        request.setOwnerId(1L);

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("Projekti");
        savedProject.setDescription("test");
        savedProject.setCreatedAt(LocalDateTime.now());
        savedProject.setOwner(owner);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        ProjectResponse response = projectService.createProject(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Projekti", response.getName());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void getProjectById_whenExists_shouldReturnProject() {
        User owner = new User();
        owner.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setName("Projekti");
        project.setCreatedAt(LocalDateTime.now());
        project.setOwner(owner);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.getProjectById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Projekti", response.getName());
    }

    @Test
    void getProjectById_whenNotFound_shouldThrowException() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.getProjectById(999L);
        });
    }
}