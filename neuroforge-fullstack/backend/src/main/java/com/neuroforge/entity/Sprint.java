package com.neuroforge.entity;

import jakarta.persistence.*;

@Entity 
@Table(name="sprints")
public class Sprint {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    private String goal;
    @Column(nullable=false) private String status = "PLANNED";
    @ManyToOne @JoinColumn(name="project_id", nullable=true) private Project project;

    public Sprint() {}

    public Sprint(Long id, String name, String goal, String status, Project project) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.status = status != null ? status : "PLANNED";
        this.project = project;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}
