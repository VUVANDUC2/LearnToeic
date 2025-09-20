package LearnToeic.controller;

import LearnToeic.model.Test;
import LearnToeic.model.Question;
import LearnToeic.service.TestService;
import LearnToeic.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    @Autowired
    private TestService testService;

    @Autowired
    private QuestionService questionService;


    @GetMapping("/tests")
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/tests/{testId}")
    public ResponseEntity<Test> getTestById(@PathVariable Integer testId) {
        Optional<Test> test = testService.getTestById(testId);
        return test.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/questions/test/{testId}")
    public List<Question> getQuestionsByTestId(@PathVariable Integer testId) {
        return questionService.getQuestionsByTestId(testId);
    }

    @PutMapping("/questions/{testId}/{questionNumber}")
    public ResponseEntity<?> updateQuestion(
        @PathVariable Integer testId,
        @PathVariable Integer questionNumber,
        @RequestBody Question updatedQuestion
    ) {
        Optional<Question> existing = questionService.getQuestionById(testId, questionNumber);
        if (existing.isPresent()) {
            updatedQuestion.setTestId(testId);
            updatedQuestion.setQuestionNumber(questionNumber);
            questionService.saveQuestion(updatedQuestion);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
}
