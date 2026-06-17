package com.facilization.task_tracking.controllers;

import com.facilization.task_tracking.dto.UserResponse;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createUser_shouldReturn201() throws Exception {
        // ARRANGE — pergatit cfare do ktheje service-i (mock)
        UserResponse response = new UserResponse(1L, "eno", "eno@test.com", LocalDateTime.now());
        when(userService.createUser(any())).thenReturn(response);

        // trupi i kerkeses (JSON)
        Map<String, String> request = new HashMap<>();
        request.put("username", "eno");
        request.put("email", "eno@test.com");
        request.put("password", "secret123");

        // ACT + ASSERT — dergo POST dhe kontrollo pergjigjen
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())                      // 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("eno"))
                .andExpect(jsonPath("$.email").value("eno@test.com"));
    }

    @Test
    void getUserById_shouldReturn200() throws Exception {
        // ARRANGE
        UserResponse response = new UserResponse(1L, "eno", "eno@test.com", LocalDateTime.now());
        when(userService.getUserById(eq(1L))).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())                           // 200
                .andExpect(jsonPath("$.username").value("eno"));
    }

    @Test
    void getUserById_whenNotFound_shouldReturn404() throws Exception {
        // ARRANGE — service hedh exception
        when(userService.getUserById(eq(999L)))
                .thenThrow(new ResourceNotFoundException("User not found with id: 999"));

        // ACT + ASSERT
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());                    // 404
    }
}