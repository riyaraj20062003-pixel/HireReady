package com.hireready.model;

/**
 * Model representing an Education entry in a Resume.
 */
public class Education {
    private int educationId;
    private int resumeId;
    private String degree;
    private String institution;
    private int passingYear;
    private double cgpaOrPercentage;

    public Education() {
    }

    public Education(String degree, String institution, int passingYear, double cgpaOrPercentage) {
        this.degree = degree;
        this.institution = institution;
        this.passingYear = passingYear;
        this.cgpaOrPercentage = cgpaOrPercentage;
    }

    public Education(int educationId, int resumeId, String degree, String institution, int passingYear, double cgpaOrPercentage) {
        this.educationId = educationId;
        this.resumeId = resumeId;
        this.degree = degree;
        this.institution = institution;
        this.passingYear = passingYear;
        this.cgpaOrPercentage = cgpaOrPercentage;
    }

    public int getEducationId() {
        return educationId;
    }

    public void setEducationId(int educationId) {
        this.educationId = educationId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public int getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(int passingYear) {
        this.passingYear = passingYear;
    }

    public double getCgpaOrPercentage() {
        return cgpaOrPercentage;
    }

    public void setCgpaOrPercentage(double cgpaOrPercentage) {
        this.cgpaOrPercentage = cgpaOrPercentage;
    }
}
