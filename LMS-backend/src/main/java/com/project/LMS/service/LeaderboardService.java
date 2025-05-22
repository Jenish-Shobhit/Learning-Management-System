package com.project.LMS.service;

import com.project.LMS.Repository.EnrollmentRepository;
import com.project.LMS.dto.LeaderboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<LeaderboardDTO> getLeaderboard() {
        List<Object[]> results = enrollmentRepository.getTopUsersByAverageProgress();
        List<LeaderboardDTO> leaderboard = new ArrayList<>();
        for (Object[] row : results) {
            String userName = (String) row[0];
            Double avgProgress = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            leaderboard.add(new LeaderboardDTO(userName, avgProgress));
        }
        return leaderboard;
    }
}
