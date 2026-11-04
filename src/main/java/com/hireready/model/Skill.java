package com.hireready.model;

/**
 * Model representing a Skill category and its comma-separated or list values in a Resume.
 */
public class Skill {
    private int skillId;
    private int resumeId;
    private String skillCategory; // e.g. "Programming Languages", "Technical Skills", "Tools/Technologies"
    private String skillNames;

    public Skill() {
    }

    public Skill(String skillCategory, String skillNames) {
        this.skillCategory = skillCategory;
        this.skillNames = skillNames;
    }

    public Skill(int skillId, int resumeId, String skillCategory, String skillNames) {
        this.skillId = skillId;
        this.resumeId = resumeId;
        this.skillCategory = skillCategory;
        this.skillNames = skillNames;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public String getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(String skillCategory) {
        this.skillCategory = skillCategory;
    }

    public String getSkillNames() {
        return skillNames;
    }

    public void setSkillNames(String skillNames) {
        this.skillNames = skillNames;
    }
}
