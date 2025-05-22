package com.project.LMS.service;

import com.project.LMS.Repository.EnrollmentRepository;
import com.project.LMS.dto.ProgressTrackerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressTracking {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<ProgressTrackerDTO> getProgressTracking() {
        List<Object[]> results = enrollmentRepository.getAllUsersByAverageProgress();
        List<ProgressTrackerDTO> progressTracker = new ArrayList<>();
        for (Object[] row : results) {
            String userName = (String) row[0];
            String userEmail = (String) row[1];
            String courseName = (String) row[2];
            Double avgProgress = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
            progressTracker.add(new ProgressTrackerDTO(userName, userEmail, courseName,avgProgress));
        }
        return progressTracker;
    }
}