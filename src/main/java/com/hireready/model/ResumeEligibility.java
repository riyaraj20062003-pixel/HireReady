package com.hireready.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing eligibility evaluation result for a candidate resume against a job.
 */
public class ResumeEligibility {
    private Resume resume;
    private double candidateCgpa;
    private int candidateGraduationYear;
    private List<String> matchingSkills = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();
    private boolean cgpaEligible;
    private boolean graduationYearEligible;
    private boolean skillsEligible;
    private boolean overallEligible;

    public ResumeEligibility() {}

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public double getCandidateCgpa() {
        return candidateCgpa;
    }

    public void setCandidateCgpa(double candidateCgpa) {
        this.candidateCgpa = candidateCgpa;
    }

    public int getCandidateGraduationYear() {
        return candidateGraduationYear;
    }

    public void setCandidateGraduationYear(int candidateGraduationYear) {
        this.candidateGraduationYear = candidateGraduationYear;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public boolean isCgpaEligible() {
        return cgpaEligible;
    }

    public void setCgpaEligible(boolean cgpaEligible) {
        this.cgpaEligible = cgpaEligible;
    }

    public boolean isGraduationYearEligible() {
        return graduationYearEligible;
    }

    public void setGraduationYearEligible(boolean graduationYearEligible) {
        this.graduationYearEligible = graduationYearEligible;
    }

    public boolean isSkillsEligible() {
        return skillsEligible;
    }

    public void setSkillsEligible(boolean skillsEligible) {
        this.skillsEligible = skillsEligible;
    }

    public boolean isOverallEligible() {
        return overallEligible;
    }

    public void setOverallEligible(boolean overallEligible) {
        this.overallEligible = overallEligible;
    }
}
