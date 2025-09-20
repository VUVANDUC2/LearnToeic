package LearnToeic.service;

import LearnToeic.model.Question;
import LearnToeic.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Integer testId, Integer questionNumber) {
        // You would need to create a QuestionId object here
        // to use the JpaRepository's findById method.
        return questionRepository.findById(new LearnToeic.model.QuestionId(testId, questionNumber));
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }
    
    public void deleteQuestion(Integer testId, Integer questionNumber) {
        questionRepository.deleteById(new LearnToeic.model.QuestionId(testId, questionNumber));
    }

        public List<Question> getQuestionsByTestId(Integer testId) {
            return questionRepository.findByTestId(testId);
        }
}