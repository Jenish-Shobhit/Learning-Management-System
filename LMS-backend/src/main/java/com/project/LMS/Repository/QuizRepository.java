package com.project.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.LMS.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}