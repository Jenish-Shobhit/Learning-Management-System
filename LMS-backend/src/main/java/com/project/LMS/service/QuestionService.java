package com.project.LMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.LMS.Repository.QuestionRepository;
import com.project.LMS.Repository.QuizRepository;
import com.project.LMS.model.Question;
import com.project.LMS.model.Quiz;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public Question saveQuestion(Question question) {
        Question savedQuestion = questionRepository.save(question);

        // Update the Quiz questions field with the new question ID
        if (savedQuestion != null && savedQuestion.getQuiz() != null) {
            Long quizId = savedQuestion.getQuiz().getQuizId();
            Quiz quiz = quizRepository.findById(quizId).orElse(null);
            if (quiz != null) {
                String existingQuestions = quiz.getQuestions();
                String newQuestionIdStr = savedQuestion.getQuestionId().toString();
                if (existingQuestions == null || existingQuestions.trim().isEmpty()) {
                    quiz.setQuestions(newQuestionIdStr);
                } else {
                    // Append new question ID if not already present
                    String[] questionIds = existingQuestions.split(",");
                    boolean alreadyPresent = false;
                    for (String qid : questionIds) {
                        if (qid.trim().equals(newQuestionIdStr)) {
                            alreadyPresent = true;
                            break;
                        }
                    }
                    if (!alreadyPresent) {
                        quiz.setQuestions(existingQuestions + "," + newQuestionIdStr);
                    }
                }
                quizRepository.save(quiz);
            }
        }

        return savedQuestion;
    }

    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question != null) {
            List<Quiz> quizzes = quizRepository.findAll();
            for (Quiz quiz : quizzes) {
                if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
                    String[] questionIds = quiz.getQuestions().split(",");
                    List<String> updatedIds = new java.util.ArrayList<>();
                    for (String qId : questionIds) {
                        if (!qId.equals(id.toString())) {
                            updatedIds.add(qId);
                        }
                    }
                    String updatedQuestionsStr = String.join(",", updatedIds);
                    quiz.setQuestions(updatedQuestionsStr);
                    quizRepository.save(quiz);
                }
            }

            // Delete the question
            questionRepository.deleteById(id);
        }
    }
}
