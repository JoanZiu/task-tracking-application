package com.facilization.task_tracking.services;

import com.facilization.task_tracking.dto.CreateTaskRequest;
import com.facilization.task_tracking.dto.TaskResponse;
import com.facilization.task_tracking.dto.UpdateTaskRequest;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.models.Project;
import com.facilization.task_tracking.models.Status;
import com.facilization.task_tracking.models.Task;
import com.facilization.task_tracking.models.User;
import com.facilization.task_tracking.repositories.ProjectRepository;
import com.facilization.task_tracking.repositories.TaskRepository;
import com.facilization.task_tracking.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.facilization.task_tracking.specifications.TaskSpecification;
import org.springframework.data.jpa.domain.Specification;
import com.facilization.task_tracking.models.Priority;


import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final EmailService emailService;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(EmailService emailService, TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.emailService = emailService;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse createTask(Long projectId, CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        task.setProject(project);

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssigneeId()));
            task.setAssignee(assignee);

            emailService.sendTaskAssignmentEmail(assignee.getEmail(), task.getTitle());
        }


        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return mapToResponse(task);
    }

    public Page<TaskResponse> getTasksByProject(Long projectId, Status status, Pageable pageable) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        Page<Task> tasks;
        if (status != null) {
            tasks = taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        } else {
            tasks = taskRepository.findByProjectId(projectId, pageable);
        }
        return tasks.map(this::mapToResponse);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    public Page<TaskResponse> searchTasks(String title, Status status, Priority priority, Pageable pageable) {
        Specification<Task> spec = TaskSpecification.withFilters(title, status, priority);
        return taskRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public List<TaskResponse> getTasksByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return taskRepository.findByAssigneeId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TaskResponse> getTasksDueToday() {
        return taskRepository.findByDueDate(LocalDate.now())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getProject().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null
        );
    }
}