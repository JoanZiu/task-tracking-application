package com.facilization.task_tracking.services;

import com.facilization.task_tracking.dto.CreateUserRequest;
import com.facilization.task_tracking.dto.UserResponse;
import com.facilization.task_tracking.exception.ResourceNotFoundException;
import com.facilization.task_tracking.models.User;
import com.facilization.task_tracking.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldReturnUserResponse() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("eno");
        request.setEmail("eno@test.com");
        request.setPassword("secret123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("eno");
        savedUser.setEmail("eno@test.com");
        savedUser.setPassword("secret123");
        savedUser.setCreatedAt(LocalDateTime.now());

        // i themi mock-ut: kur te thirret save(), kthe savedUser
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // ACT — ekzekuto metoden qe testojme
        UserResponse response = userService.createUser(request);

        // ASSERT — kontrollo rezultatin
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("eno", response.getUsername());
        assertEquals("eno@test.com", response.getEmail());
        // password NUK eshte te response (kontroll i nenkuptuar — UserResponse s'ka password)

        // verifiko qe save() u thirr
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_whenExists_shouldReturnUser() {
        // ARRANGE
        User user = new User();
        user.setId(1L);
        user.setUsername("eno");
        user.setEmail("eno@test.com");
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // ACT
        UserResponse response = userService.getUserById(1L);

        // ASSERT
        assertEquals(1L, response.getId());
        assertEquals("eno", response.getUsername());
    }

    @Test
    void getUserById_whenNotFound_shouldThrowException() {
        // ARRANGE — mock kthen Optional bosh (s'gjendet)
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT + ASSERT — presim qe te hedhe ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }
}