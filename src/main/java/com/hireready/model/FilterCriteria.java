package com.hireready.model;

/**
 * Encapsulates search and eligibility filter criteria for resumes.
 */
public class FilterCriteria {
    private Double minCgpa;
    private String degree;
    private String skill;
    private Integer passingYear;
    private String keyword;

    public FilterCriteria() {
    }

    public Double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Integer getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(Integer passingYear) {
        this.passingYear = passingYear;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isEmpty() {
        return minCgpa == null &&
               (degree == null || degree.trim().isEmpty()) &&
               (skill == null || skill.trim().isEmpty()) &&
               passingYear == null &&
               (keyword == null || keyword.trim().isEmpty());
    }
}
