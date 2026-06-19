package com.facilization.task_tracking.services;

import com.facilization.task_tracking.dto.CreateProjectRequest;
import com.facilization.task_tracking.dto.ProjectResponse;
import com.facilization.task_tracking.dto.UpdateProjectRequest;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.models.Project;
import com.facilization.task_tracking.models.User;
import com.facilization.task_tracking.repositories.ProjectRepository;
import com.facilization.task_tracking.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectResponse createProject(CreateProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getOwnerId()));
        project.setOwner(owner);

        Project saved = projectRepository.save(project);
        return mapToResponse(saved);
    }

    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return mapToResponse(project);
    }

    public Page<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public ProjectResponse updateProject(Long id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        Project saved = projectRepository.save(project);
        return mapToResponse(saved);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    // Metode ndihmese konvertimi Entity -> DTO ne nje vend
    private ProjectResponse mapToResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getOwner().getId()
        );
    }
}