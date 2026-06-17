package com.facilization.task_tracking.specifications;

import com.facilization.task_tracking.models.Status;
import com.facilization.task_tracking.models.Priority;
import com.facilization.task_tracking.models.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    // ndertron nje Specification dinamike sipas filtrave te dhena
    public static Specification<Task> withFilters(String title, Status status, Priority priority) {
        return (root, query, cb) -> {
            // fillojme me nje kusht qe eshte gjithmone i vertete
            var predicate = cb.conjunction();

            // shto filter per titullin nese eshte dhene
            if (title != null && !title.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            // shto filter per status nese eshte dhene
            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }
            // shto filter per prioritet nese eshte dhene
            if (priority != null) {
                predicate = cb.and(predicate, cb.equal(root.get("priority"), priority));
            }

            return predicate;
        };
    }
}