package com.project.LMS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_seq")
	@SequenceGenerator(name = "question_seq", sequenceName = "question_seq", allocationSize = 1)
	private Long questionId;

	@NotBlank(message = "Question description cannot be blank")
	private String questionDescrioption;

	@NotBlank(message = "Option 1 cannot be blank")
	private String option1;

	@NotBlank(message = "Option 2 cannot be blank")
	private String option2;

	@NotBlank(message = "Option 3 cannot be blank")
	private String option3;

	@NotBlank(message = "Option 4 cannot be blank")
	private String option4;

	@Min(value = 1, message = "Answer option must be between 1 and 4")
	@Max(value = 4, message = "Answer option must be between 1 and 4")
	private int answerOption;

	@ManyToOne
	private Quiz quiz;

	// Getters and Setters

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getQuestionDescrioption() {
		return questionDescrioption;
	}

	public void setQuestionDescrioption(String questionDescrioption) {
		this.questionDescrioption = questionDescrioption;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public int getAnswerOption() {
		return answerOption;
	}

	public void setAnswerOption(int answerOption) {
		this.answerOption = answerOption;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
}
