package com.facilization.task_tracking.controllers;

import com.facilization.task_tracking.dto.TaskResponse;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.models.Priority;
import com.facilization.task_tracking.models.Status;
import com.facilization.task_tracking.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createTask_shouldReturn201() throws Exception {
        TaskResponse response = new TaskResponse(1L, "Task i pare", "test",
                Status.TODO, Priority.HIGH, LocalDate.now(), LocalDateTime.now(), 1L, null);
        when(taskService.createTask(eq(1L), any())).thenReturn(response);

        Map<String, Object> request = new HashMap<>();
        request.put("title", "Task i pare");
        request.put("description", "test");
        request.put("status", "TODO");
        request.put("priority", "HIGH");
        request.put("dueDate", LocalDate.now().toString());

        mockMvc.perform(post("/api/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task i pare"));
    }

    @Test
    void getTaskById_shouldReturn200() throws Exception {
        TaskResponse response = new TaskResponse(1L, "Task i pare", "test",
                Status.TODO, Priority.HIGH, LocalDate.now(), LocalDateTime.now(), 1L, null);
        when(taskService.getTaskById(eq(1L))).thenReturn(response);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task i pare"));
    }

    @Test
    void getTaskById_whenNotFound_shouldReturn404() throws Exception {
        when(taskService.getTaskById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Task not found with id: 999"));

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }
}