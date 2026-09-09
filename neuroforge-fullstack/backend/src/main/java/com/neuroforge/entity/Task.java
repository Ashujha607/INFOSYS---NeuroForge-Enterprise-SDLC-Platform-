package com.neuroforge.entity;

import jakarta.persistence.*;

@Entity 
@Table(name="tasks")
public class Task {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    private String description;
    @Column(nullable=false) private String status = "TODO";
    @Column(nullable=false) private String priority = "MEDIUM";
    @ManyToOne @JoinColumn(name="project_id", nullable=true) private Project project;
    @ManyToOne @JoinColumn(name="sprint_id") private Sprint sprint;
    @ManyToOne @JoinColumn(name="assigned_to") private User assignedTo;

    public Task() {}

    public Task(Long id, String title, String description, String status, String priority, Project project, Sprint sprint, User assignedTo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status != null ? status : "TODO";
        this.priority = priority != null ? priority : "MEDIUM";
        this.project = project;
        this.sprint = sprint;
        this.assignedTo = assignedTo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public Sprint getSprint() { return sprint; }
    public void setSprint(Sprint sprint) { this.sprint = sprint; }

    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }
}
