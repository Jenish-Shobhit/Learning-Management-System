package com.project.LMS.dto;

public class LeaderboardDTO {
    private String userName;
    private Double averageProgress;

    // Constructor that matches query column aliases
    public LeaderboardDTO(String userName, Double averageProgress) {
        this.userName = userName;
        this.averageProgress = averageProgress;
    }

    // Default constructor (may be needed for serialization)
    public LeaderboardDTO() {
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getAverageProgress() {
        return averageProgress;
    }

    public void setAverageProgress(Double averageProgress) {
        this.averageProgress = averageProgress;
    }

    @Override
    public String toString() {
        return "LeaderboardDTO{" +
                "userName='" + userName + '\'' +
                ", averageProgress=" + averageProgress +
                '}';
    }
}

