package com.facilization.task_tracking.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable =false , length=50)
    private String name;

    @Column(nullable = false,length=500)
    private String description;

    @Column(name = "created_at", nullable=false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Task> tasks=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id")
    private User owner;

    @PrePersist
    private void createTime()
    {
        this.createdAt=LocalDateTime.now();
    }

    public Project(){}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }



}






