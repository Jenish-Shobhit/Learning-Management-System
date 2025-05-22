package com.project.LMS.service;

import com.project.LMS.Repository.CourseRepository;
import com.project.LMS.Repository.EnrollmentRepository;
import com.project.LMS.Repository.UserRepository;
import com.project.LMS.dto.EnrollmentDTO;
import com.project.LMS.exceptions.CourseNotFoundException;
import com.project.LMS.exceptions.EnrollmentNotFoundException;
import com.project.LMS.exceptions.UserNotFoundException;
import com.project.LMS.model.Enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    public List<Enrollment> getEnrollmentsByUserId(Long userId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        // Return enrollments for the user
        return enrollmentRepository.findByUser_userId(userId);
    }


    public Enrollment saveEnrollment(EnrollmentDTO enrollment) {
    	
    	Enrollment newEnrollment = new Enrollment();
    	
    	newEnrollment.setCourse(courseRepository.findById(enrollment.getCourseId())
    			.orElseThrow(() -> new CourseNotFoundException("Course not found")));
    	
    	newEnrollment.setUser(userRepository.findById(enrollment.getUserId())
    			.orElseThrow(() -> new UserNotFoundException("User not found")));
    	
    	newEnrollment.setEnrollmentDate(enrollment.getEnrollmentDate());
    	
    	newEnrollment.setProgress(enrollment.getProgress());
    	
        return enrollmentRepository.save(newEnrollment);
    }

    public Enrollment updateCourseProgress(Long userId, Long courseId, Integer progress) {
        // Find the enrollment for this user and course
        Enrollment enrollment = enrollmentRepository.findByUser_userIdAndCourse_courseId(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found for user ID " + userId + " and course ID " + courseId));

        // Update the progress
        enrollment.setProgress(progress);

        // Save and return the updated enrollment
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment getCourseProgress(Long userId, Long courseId) {
        return enrollmentRepository.findByUser_userIdAndCourse_courseId(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found for user ID " + userId + " and course ID " + courseId));
    }


    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }
}