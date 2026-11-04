package com.hireready.model;

/**
 * Model representing a Work Experience entry in a Resume.
 */
public class Experience {
    private int experienceId;
    private int resumeId;
    private String company;
    private String jobRole;
    private String duration;
    private String description;

    public Experience() {
    }

    public Experience(String company, String jobRole, String duration, String description) {
        this.company = company;
        this.jobRole = jobRole;
        this.duration = duration;
        this.description = description;
    }

    public Experience(int experienceId, int resumeId, String company, String jobRole, String duration, String description) {
        this.experienceId = experienceId;
        this.resumeId = resumeId;
        this.company = company;
        this.jobRole = jobRole;
        this.duration = duration;
        this.description = description;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(int experienceId) {
        this.experienceId = experienceId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
