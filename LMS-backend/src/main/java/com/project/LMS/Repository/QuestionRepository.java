package com.project.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.LMS.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}