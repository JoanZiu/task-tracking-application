package com.facilization.task_tracking.controllers;

import com.facilization.task_tracking.dto.CreateTaskRequest;
import com.facilization.task_tracking.dto.TaskResponse;
import com.facilization.task_tracking.dto.UpdateTaskRequest;
import com.facilization.task_tracking.models.Status;
import com.facilization.task_tracking.services.TaskService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.facilization.task_tracking.models.Priority;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long projectId,
                                                   @Valid @RequestBody CreateTaskRequest request) {
        TaskResponse response = taskService.createTask(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Page<TaskResponse>> getTasksByProject(@PathVariable Long projectId,
                                                                @RequestParam(required = false) Status status,
                                                                @ParameterObject Pageable pageable) {
        Page<TaskResponse> response = taskService.getTasksByProject(projectId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/due-today")
    public ResponseEntity<List<TaskResponse>> getTasksDueToday()
    {
        List<TaskResponse> response=taskService.getTasksDueToday();
        return ResponseEntity.ok(response);

    }


    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateTaskRequest request) {
        TaskResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long userId) {
        List<TaskResponse> response = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/search")
    public ResponseEntity<Page<TaskResponse>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @ParameterObject Pageable pageable) {
        Page<TaskResponse> response = taskService.searchTasks(title, status, priority, pageable);
        return ResponseEntity.ok(response);
    }
}