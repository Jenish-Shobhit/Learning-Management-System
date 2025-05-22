package com.project.LMS.service;

import com.project.LMS.Repository.QuestionRepository;
import com.project.LMS.Repository.QuizRepository;
import com.project.LMS.exceptions.QuizNotFoundException;
import com.project.LMS.model.Question;
import com.project.LMS.model.Quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {
	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuestionRepository questionRepository;

	public List<Quiz> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public Quiz getQuizById(Long id) {
		return quizRepository.findById(id).orElse(null);
	}

	public List<Question> getAllQuestionOfQuizes(Long id) {
		Optional<Quiz> optionalQuiz = quizRepository.findById(id);
		if (optionalQuiz.isPresent()) {
			Quiz existingQuiz = optionalQuiz.get();

			String[] questionIds = existingQuiz.getQuestions().split(",");

			List<Question> questions = List.of(questionIds).stream()
					.map(Long::valueOf)
					.map(questionRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());

			return questions;

		}

		else {
			throw new QuizNotFoundException("Quiz with ID " + id + " not found");
		}

	}

	public Quiz saveQuiz(Quiz quiz) {
		return quizRepository.save(quiz);
	}

	public Quiz updateQuiz(Quiz updatedQuiz) {

		Optional<Quiz> optionalQuiz = quizRepository.findById(updatedQuiz.getQuizId());

		if (optionalQuiz.isPresent()) {

			Quiz existingQuiz = optionalQuiz.get();

			if (updatedQuiz.getQuizName() != null && !updatedQuiz.getQuizName().isEmpty()) {

				existingQuiz.setQuizName(updatedQuiz.getQuizName());
			}
			if (updatedQuiz.getMarks() != null) {

				existingQuiz.setMarks(updatedQuiz.getMarks());
			}
			if (updatedQuiz.getMinPassScore() != null) {

				existingQuiz.setMinPassScore(updatedQuiz.getMinPassScore());
			}

			if (updatedQuiz.getQuestions() != null) {
				existingQuiz.setQuestions(updatedQuiz.getQuestions());
			}

			return quizRepository.save(existingQuiz);

		}

		else {
			throw new QuizNotFoundException("Quiz with ID " + updatedQuiz.getQuizId() + " not found");
		}

	}

	public void deleteQuiz(Long id) {
		quizRepository.deleteById(id);
	}
}
