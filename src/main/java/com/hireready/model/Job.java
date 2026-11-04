package com.hireready.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a Job posting with recruitment eligibility criteria.
 */
public class Job {
    private int jobId;
    private String companyName;
    private String jobTitle;
    private String jobDescription;
    private Double minimumCgpa;
    private Integer minimumExperience;
    private String minimumDegree;
    private Integer graduationYear;
    private String location;
    private String jobType;
    private Date applicationDeadline;
    private Timestamp createdAt;
    private List<String> requiredSkills = new ArrayList<>();

    public Job() {}

    public Job(int jobId, String companyName, String jobTitle, String jobDescription,
               Double minimumCgpa, Integer minimumExperience, String minimumDegree,
               Integer graduationYear, String location, String jobType) {
        this.jobId = jobId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.minimumCgpa = minimumCgpa;
        this.minimumExperience = minimumExperience;
        this.minimumDegree = minimumDegree;
        this.graduationYear = graduationYear;
        this.location = location;
        this.jobType = jobType;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Double getMinimumCgpa() {
        return minimumCgpa;
    }

    public void setMinimumCgpa(Double minimumCgpa) {
        this.minimumCgpa = minimumCgpa;
    }

    public Integer getMinimumExperience() {
        return minimumExperience;
    }

    public void setMinimumExperience(Integer minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    public String getMinimumDegree() {
        return minimumDegree;
    }

    public void setMinimumDegree(String minimumDegree) {
        this.minimumDegree = minimumDegree;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Date getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(Date applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
