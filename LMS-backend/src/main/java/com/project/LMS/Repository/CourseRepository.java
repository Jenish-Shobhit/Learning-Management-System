package com.project.LMS.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.LMS.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	Optional<Course> findByTitle(String courseName);
}