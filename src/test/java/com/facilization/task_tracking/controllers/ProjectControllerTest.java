package com.facilization.task_tracking.controllers;

import com.facilization.task_tracking.dto.ProjectResponse;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createProject_shouldReturn201() throws Exception {
        ProjectResponse response = new ProjectResponse(1L, "Projekti", "test", LocalDateTime.now(), 1L);
        when(projectService.createProject(any())).thenReturn(response);

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Projekti");
        request.put("description", "test");
        request.put("ownerId", 1);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Projekti"));
    }

    @Test
    void getProjectById_shouldReturn200() throws Exception {
        ProjectResponse response = new ProjectResponse(1L, "Projekti", "test", LocalDateTime.now(), 1L);
        when(projectService.getProjectById(eq(1L))).thenReturn(response);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Projekti"));
    }

    @Test
    void getProjectById_whenNotFound_shouldReturn404() throws Exception {
        when(projectService.getProjectById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("Project not found with id: 999"));

        mockMvc.perform(get("/api/projects/999"))
                .andExpect(status().isNotFound());
    }
}