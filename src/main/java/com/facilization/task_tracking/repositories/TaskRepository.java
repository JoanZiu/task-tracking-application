package com.facilization.task_tracking.repositories;

import com.facilization.task_tracking.models.Task;
import com.facilization.task_tracking.models.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Page<Task> findByProjectIdAndStatus(Long projectId, Status status, Pageable pageable);

    List<Task> findByAssigneeId(Long assigneeId);

    List<Task> findByDueDate(LocalDate dueDate);
}