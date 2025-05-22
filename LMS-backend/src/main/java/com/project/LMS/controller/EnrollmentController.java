package com.project.LMS.controller;

import com.project.LMS.dto.EnrollmentDTO;
import com.project.LMS.dto.LeaderboardDTO;
import com.project.LMS.dto.ProgressTrackerDTO;
import com.project.LMS.exceptions.EnrollmentNotFoundException;
import com.project.LMS.exceptions.UserNotFoundException;
import com.project.LMS.model.Enrollment;
import com.project.LMS.response.StandardResponse;
import com.project.LMS.service.EnrollmentService;

import com.project.LMS.service.LeaderboardService;
import com.project.LMS.service.ProgressTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private ProgressTracking progressTracking;

    @GetMapping
    public ResponseEntity<StandardResponse<List<Enrollment>>> getAllEnrollments() {
        try {
            logger.info("Fetching all enrollments");
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", enrollments), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching all enrollments", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch enrollments", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<Enrollment>> getEnrollmentById(@PathVariable Long id) {
        try {
            logger.info("Fetching enrollment with id: {}", id);
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);
            if (enrollment != null) {
                return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", enrollment), HttpStatus.OK);
            } else {
                logger.warn("Enrollment with id: {} not found", id);
                throw new EnrollmentNotFoundException("Enrollment not found");
            }
        } catch (EnrollmentNotFoundException e) {
            logger.warn("Enrollment with id: {} not found", id);
            return new ResponseEntity<>(new StandardResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error fetching enrollment with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch enrollment", e);
        }
    }

    // Add this method to EnrollmentController.java
    @GetMapping("/user/{userId}")
    public ResponseEntity<StandardResponse<List<Enrollment>>> getEnrollmentsByUserId(@PathVariable Long userId) {
        try {
            logger.info("Fetching enrollments for user ID: {}", userId);
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUserId(userId);

            StandardResponse<List<Enrollment>> response = new StandardResponse<>(
                    HttpStatus.OK.value(),
                    "Enrollments retrieved successfully",
                    enrollments
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            logger.error("User not found with ID: {}", userId, ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Error fetching enrollments for user ID: {}", userId, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving enrollments", ex);
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<StandardResponse<List<LeaderboardDTO>>> getLeaderboard() {
        logger.info("Request to get leaderboard");
        try {
            List<LeaderboardDTO> leaderboard = leaderboardService.getLeaderboard();

            StandardResponse<List<LeaderboardDTO>> response = new StandardResponse<>(
                    HttpStatus.OK.value(),
                    "Leaderboard retrieved successfully",
                    leaderboard
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving leaderboard", e);
            StandardResponse<List<LeaderboardDTO>> errorResponse = new StandardResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error retrieving leaderboard: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/progressTracking")
    public ResponseEntity<StandardResponse<List<ProgressTrackerDTO>>> getProgressTracking() {
        logger.info("Request to get progress tracking");
        try {
            List<ProgressTrackerDTO> progressTracker = progressTracking.getProgressTracking();

            StandardResponse<List<ProgressTrackerDTO>> response = new StandardResponse<>(
                    HttpStatus.OK.value(),
                    "progress retrieved successfully",
                    progressTracker
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving leaderboard", e);
            StandardResponse<List<ProgressTrackerDTO>> errorResponse = new StandardResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error retrieving leaderboard: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<StandardResponse<Enrollment>> createEnrollment(@RequestBody EnrollmentDTO enrollment) {
        try {
            logger.info("Creating new enrollment");
            Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);
            return new ResponseEntity<>(new StandardResponse<>(HttpStatus.CREATED.value(), "Enrollment created successfully", savedEnrollment), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating enrollment", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create enrollment", e);
        }
    }

    @GetMapping("/progress")
    public ResponseEntity<StandardResponse<Enrollment>> updateCourseProgress(
            @RequestParam Long userId,
            @RequestParam Long courseId
            ) {
        try {
            Enrollment updatedEnrollment = enrollmentService.getCourseProgress(userId, courseId);

            StandardResponse<Enrollment> response = new StandardResponse<>(
                    HttpStatus.OK.value(),
                    "Course progress updated successfully",
                    updatedEnrollment
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EnrollmentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating course progress: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating course progress", e);
        }
    }

    @PutMapping("/progress")
    public ResponseEntity<StandardResponse<Enrollment>> updateCourseProgress(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam Integer progress) {
        try {
            Enrollment updatedEnrollment = enrollmentService.updateCourseProgress(userId, courseId, progress);

            StandardResponse<Enrollment> response = new StandardResponse<>(
                    HttpStatus.OK.value(),
                    "Course progress updated successfully",
                    updatedEnrollment
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EnrollmentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating course progress: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating course progress", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Void>> deleteEnrollment(@PathVariable Long id) {
        try {
            logger.info("Deleting enrollment with id: {}", id);
            enrollmentService.deleteEnrollment(id);
            StandardResponse<Void> response = new StandardResponse<>(HttpStatus.NO_CONTENT.value(), "Enrollment deleted successfully", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting enrollment with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete enrollment", e);
        }
    }
}
