package com.projects.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.projects.model.Question;
import com.projects.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{
	
	private final QuestionRepository questionRepository;

	@Override
	public Question createQuestion(Question question) {
	
		return questionRepository.save(question);
	}

	@Override
	public List<Question> getAllQuestion() {
	
		return questionRepository.findAll();
	}

	@Override
	public Optional<Question> getQuestionById(Long id) {
		
		return questionRepository.findById(id);
	}

	@Override
	public List<String> getAllSubjects() {
		
		return questionRepository.findDistinctSubject();
	}

	@Override
	public Question updateQuestion(Long id, Question question) {
		
		Optional<Question> theQuestion= this.getQuestionById(id);
		
		if(theQuestion.isPresent()) {
			Question updatedQuestion= theQuestion.get();
			updatedQuestion.setQuestion(question.getQuestion());
			updatedQuestion.setChoices(question.getChoices());
			updatedQuestion.setCorrectAnswers(question.getCorrectAnswers());
			
			return questionRepository.save(updatedQuestion);
			
		} else {
			System.out.println("Could Not Able to find Question with id Given!!");
		}
		return question;
	}

	@Override
	public void deleteQuestion(Long id) {
		questionRepository.deleteById(id);
	}

	@Override
	public List<Question> getQuestionForUser(Integer numOfQuestions, String subject) {
		
		Pageable pageable = PageRequest.of(0, numOfQuestions);
		
		return questionRepository.findBySubject(subject,pageable).getContent(); 
	
	}
	
	

}
