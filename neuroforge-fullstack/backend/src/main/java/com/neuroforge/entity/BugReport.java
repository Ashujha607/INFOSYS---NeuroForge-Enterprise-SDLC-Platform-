package com.neuroforge.entity;

import jakarta.persistence.*;

@Entity 
@Table(name="bug_reports")
public class BugReport {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    private String description;
    @Column(nullable=false) private String severity = "MEDIUM";
    @Column(nullable=false) private String status = "OPEN";
    @ManyToOne @JoinColumn(name="project_id", nullable=true) private Project project;
    @ManyToOne @JoinColumn(name="reported_by") private User reportedBy;

    public BugReport() {}

    public BugReport(Long id, String title, String description, String severity, String status, Project project, User reportedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.severity = severity != null ? severity : "MEDIUM";
        this.status = status != null ? status : "OPEN";
        this.project = project;
        this.reportedBy = reportedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public User getReportedBy() { return reportedBy; }
    public void setReportedBy(User reportedBy) { this.reportedBy = reportedBy; }
}
