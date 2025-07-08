package com.projects.service;

import java.util.List;
import java.util.Optional;

import com.projects.model.Question;

public interface IQuestionService {
	
	Question createQuestion(Question question);
	
	List<Question> getAllQuestion();
	
	Optional<Question> getQuestionById(Long id);
	
	List<String> getAllSubjects();
	
	Question updateQuestion(Long id ,Question question);
	
	void deleteQuestion(Long id);
	 
	List<Question> getQuestionForUser(Integer numOfQuestions,String subject);
	
	

}
