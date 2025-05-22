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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.LMS.model.Question;
import com.project.LMS.response.StandardResponse;
import com.project.LMS.service.QuestionService;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	private QuestionService questionService;

	@GetMapping
	public ResponseEntity<StandardResponse<List<Question>>> getAllQuestions() {
		try {
			logger.info("Fetching all questions");
			List<Question> questions = questionService.getAllQuestions();
			return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", questions),
					HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching all questions", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch questions", e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<StandardResponse<Question>> getQuestionById(@PathVariable Long id) {
		try {
			logger.info("Fetching question with id: {}", id);
			Question question = questionService.getQuestionById(id);
			if (question != null) {
				return new ResponseEntity<>(new StandardResponse<>(HttpStatus.OK.value(), "Success", question),
						HttpStatus.OK);
			} else {
				logger.warn("Question with id: {} not found", id);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
			}
		} catch (Exception e) {
			logger.error("Error fetching question with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch question", e);
		}
	}
	

	@PostMapping
	public ResponseEntity<StandardResponse<Question>> createQuestion(@RequestBody Question question) {
		try {
			logger.info("Creating new question");
			Question savedQuestion = questionService.saveQuestion(question);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.CREATED.value(), "Question created successfully", savedQuestion),
					HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating question", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create question", e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<StandardResponse<Void>> deleteQuestion(@PathVariable Long id) {
		try {
			logger.info("Deleting question with id: {}", id);
			questionService.deleteQuestion(id);
			return new ResponseEntity<>(
					new StandardResponse<>(HttpStatus.NO_CONTENT.value(), "Question deleted successfully", null),
					HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error deleting question with id: {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete question", e);
		}
	}
}
