package com.project.LMS.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.LMS.Repository.QuizRepository;
import com.project.LMS.exceptions.QuizNotFoundException;
import com.project.LMS.model.Question;
import com.project.LMS.model.Quiz;
import com.project.LMS.response.StandardResponse;
import com.project.LMS.service.QuizService;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/quizes")
public class QuizController {

	private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

	@Autowired
	private QuizService quizService;

	@GetMapping
	public ResponseEntity<StandardResponse<List<Quiz>>> getAllQuizzes() {
		try {
			logger.info("Fetching all quiz");
			List<Quiz> quiz = quizService.getAllQuizzes();
			return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", quiz), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching all quiz", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch quiz", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<StandardResponse<Quiz>> getQuizById(@PathVariable Long id) {
		try {
			logger.info("Fetching quiz with id: {}", id);
			Quiz quiz = quizService.getQuizById(id);
			if (quiz != null) {
				return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", quiz),
						HttpStatus.OK);
			} else {
				logger.warn("Quiz with id: {} not found", id);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
			}
		} catch (Exception e) {
			logger.error("Error fetching quiz with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch quiz", e);
		}
	}
	

	@GetMapping("/quiz/{id}")
	public ResponseEntity<StandardResponse<List<Question>>> getQuestionByQuizId(@PathVariable Long id) {
		try {
			logger.info("Fetching questions for quiz with id: {}", id);
			List<Question> questions = quizService.getAllQuestionOfQuizes(id);
			if (questions != null && !questions.isEmpty()) {
				return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", questions), HttpStatus.OK);
				}
			else {
				logger.warn("Questions for quiz with id: {} not found", id);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Questions not found");
				}
			} catch (Exception e) {
		logger.error("Error fetching questions for quiz with id: {}", id, e);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch questions", e);
		}
}


	@PostMapping
	public ResponseEntity<StandardResponse<Quiz>> createQuiz(@RequestBody Quiz quiz) {
		try {
			logger.info("Creating new quiz");
			Quiz savedQuiz = quizService.saveQuiz(quiz);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.CREATED.value(), "Quiz created successfully", savedQuiz),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating quiz", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create quiz", e);
		}
	}

	@PutMapping
	public ResponseEntity<StandardResponse<Quiz>> updateQuiz(@RequestBody Quiz quizdto) {
		
		try {
			logger.info("Updating quiz with id: {}", quizdto.getQuizId());
			Quiz updatedQuiz = quizService.updateQuiz(quizdto);
			return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Successfully Updated", updatedQuiz), HttpStatus.OK);
		} 
		catch (QuizNotFoundException e) {
			logger.warn("Quiz with id: {} not found", quizdto.getQuizId());
			return new ResponseEntity<>(new StandardResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null), HttpStatus.NOT_FOUND);
			}
		catch (Exception e) {
			logger.error("Error updating quiz with id: {}", quizdto.getQuizId(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update quiz", e);
		}
		
		
}

	@DeleteMapping("/{id}")
	public ResponseEntity<StandardResponse<Quiz>> deleteQuiz(@PathVariable Long id) {
		try {
			logger.info("Deleting quiz with id: {}", id);
			quizService.deleteQuiz(id);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.NO_CONTENT.value(), "Quiz deleted successfully", null),
					HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error deleting quiz with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete quiz", e);
		}
	}
}
