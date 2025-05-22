package com.project.LMS.dto;

public class ProgressTrackerDTO {
    private String userName;
    private String userEmail;
    private Double averageProgress;
    private String courseName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getAverageProgress() {
        return averageProgress;
    }

    public void setAverageProgress(Double averageProgress) {
        this.averageProgress = averageProgress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ProgressTrackerDTO(String userName, String userEmail, String courseName, Double averageProgress) {
        this.userName = userName;
        this.courseName = courseName;
        this.averageProgress = averageProgress;
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "ProgressTrackerDTO{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", averageProgress=" + averageProgress +
                ", courseName='" + courseName + '\'' +
                '}';
    }
// Constructor that matches query column aliases

}
