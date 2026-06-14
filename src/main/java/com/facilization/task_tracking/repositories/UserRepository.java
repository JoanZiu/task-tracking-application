package com.facilization.task_tracking.repositories;

import com.facilization.task_tracking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
