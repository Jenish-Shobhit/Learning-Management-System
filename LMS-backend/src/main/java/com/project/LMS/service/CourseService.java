package com.project.LMS.service;

import com.project.LMS.Repository.CourseRepository;
import com.project.LMS.exceptions.CourseNotFoundException;
import com.project.LMS.model.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;

	// Get all courses
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	// Get a course by ID
	public Course getCourseById(Long id) {
		return courseRepository.findById(id)
				.orElseThrow(() -> new CourseNotFoundException("Course not found"));
	}

	// Add a new course
	public Course addCourse(Course course) {
		return courseRepository.save(course);
	}

	// Update an existing course
	public Course updateCourseById(Long id, Course updatedCourse) {
		Course existingCourse = courseRepository.findById(id)
				.orElseThrow(() -> new CourseNotFoundException("Course not found"));

		// Update fields only if new values are provided
		existingCourse
				.setTitle(updatedCourse.getTitle() != null ? updatedCourse.getTitle() : existingCourse.getTitle());
		existingCourse.setDescription(updatedCourse.getDescription() != null ? updatedCourse.getDescription()
				: existingCourse.getDescription());
		existingCourse.setPrerequisites(updatedCourse.getPrerequisites() != null ? updatedCourse.getPrerequisites()
				: existingCourse.getPrerequisites());
		existingCourse.setVideoUrl(
				updatedCourse.getVideoUrl() != null ? updatedCourse.getVideoUrl() : existingCourse.getVideoUrl());
		existingCourse.setQuizIds(
				updatedCourse.getQuizIds() != null ? updatedCourse.getQuizIds() : existingCourse.getQuizIds());

		return courseRepository.save(existingCourse);
	}

	// Delete a course
	public void deleteCourseById(Long id) {
		courseRepository.deleteById(id);

	}
}
