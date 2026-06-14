package com.facilization.task_tracking.repositories;

import com.facilization.task_tracking.models.Task;
import com.facilization.task_tracking.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Task-et e nje projekti (me pagination)
    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    // Task-et e nje projekti, filtruar sipas statusit (me pagination)
    Page<Task> findByProjectIdAndStatus(Long projectId, Status status, Pageable pageable);

    // Task-et e caktuara nje useri
    List<Task> findByAssigneeId(Long assigneeId);

    // Task-et qe skadojne ne nje date te caktuar
    List<Task> findByDueDate(LocalDate dueDate);
}