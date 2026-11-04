package com.hireready.model;

/**
 * Model representing a Project entry in a Resume.
 */
public class Project {
    private int projectId;
    private int resumeId;
    private String title;
    private String technologies;
    private String description;
    private String projectLink;

    public Project() {
    }

    public Project(String title, String technologies, String description, String projectLink) {
        this.title = title;
        this.technologies = technologies;
        this.description = description;
        this.projectLink = projectLink;
    }

    public Project(int projectId, int resumeId, String title, String technologies, String description, String projectLink) {
        this.projectId = projectId;
        this.resumeId = resumeId;
        this.title = title;
        this.technologies = technologies;
        this.description = description;
        this.projectLink = projectLink;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }
}
