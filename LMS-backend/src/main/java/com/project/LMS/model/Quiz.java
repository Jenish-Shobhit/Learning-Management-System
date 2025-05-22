package com.project.LMS.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "quiz")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_seq")
	@SequenceGenerator(name = "enrollment_seq", sequenceName = "enrollment_seq", allocationSize = 1)
	private Long quizId;

	@NotBlank(message = "Quiz Name cannot be blank")
	private String quizName;

	@NotNull(message = "Marks cannot be null")
	@Min(value = 1, message = "Marks option must be greater than 0")
	private Integer marks;

	@NotNull(message = "MinPassScore cannot be null")
	@Min(value = 1, message = "MinPassScore option must be greater than 0")
	private Integer minPassScore;

	@OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Question> questionsList;

	private String questions;

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public Integer getMarks() {
		return marks;
	}

	public void setMarks(Integer marks) {
		this.marks = marks;
	}

	public Integer getMinPassScore() {
		return minPassScore;
	}

	public void setMinPassScore(Integer minPassScore) {
		this.minPassScore = minPassScore;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	

}