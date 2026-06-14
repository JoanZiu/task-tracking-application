package com.facilization.task_tracking.repositories;

import com.facilization.task_tracking.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
