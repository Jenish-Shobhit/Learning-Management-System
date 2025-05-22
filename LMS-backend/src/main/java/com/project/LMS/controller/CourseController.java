package com.project.LMS.controller;

import com.project.LMS.exceptions.QuizNotFoundException;
import com.project.LMS.model.Course;
import com.project.LMS.response.StandardResponse;
import com.project.LMS.service.CourseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	private CourseService courseService;

	@GetMapping("/allCourses")
	public ResponseEntity<StandardResponse<List<Course>>> getAllCourses() {
		try {
			logger.info("Fetching all courses");
			List<Course> courses = courseService.getAllCourses();
			return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", courses),
					HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching all courses", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch courses", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<StandardResponse<Course>> getCourseById(@PathVariable Long id) {
		try {
			logger.info("Fetching course with id: {}", id);
			Course course = courseService.getCourseById(id);
			if (course != null) {
				return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", course),
						HttpStatus.OK);
			} else {
				logger.warn("Course with id: {} not found", id);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
			}
		} catch (Exception e) {
			logger.error("Error fetching course with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch course", e);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<StandardResponse<Course>> createCourse(@RequestBody Course course) {
		try {
			logger.info("Creating new course");
			Course savedCourse = courseService.addCourse(course);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.CREATED.value(), "Course created successfully", savedCourse),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating course", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create course", e);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<StandardResponse<Course>> updateCourse(@RequestBody Course course, @PathVariable Long id) {
		try {
			logger.info("Updating course with id: {}", id);
			Course updatedCourse = courseService.updateCourseById(id, course);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated", updatedCourse),
					HttpStatus.OK);
		} catch (QuizNotFoundException e) {
			logger.warn("Course with id: {} not found", id);
			return new ResponseEntity<>(new StandardResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null),
					HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.error("Error updating course with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update course", e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<StandardResponse<Course>> deleteCourse(@PathVariable Long id) {
		try {
			logger.info("Deleting course with id: {}", id);
			courseService.deleteCourseById(id);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.NO_CONTENT.value(), "Quiz deleted successfully", null),
					HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error deleting course with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create course", e);
		}
	}

}
