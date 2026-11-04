package com.hireready.model;

/**
 * Model representing an Achievement entry in a Resume.
 */
public class Achievement {
    private int achievementId;
    private int resumeId;
    private String title;
    private String description;

    public Achievement() {
    }

    public Achievement(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Achievement(int achievementId, int resumeId, String title, String description) {
        this.achievementId = achievementId;
        this.resumeId = resumeId;
        this.title = title;
        this.description = description;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
