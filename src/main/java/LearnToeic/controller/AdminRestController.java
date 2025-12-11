package LearnToeic.controller;

import LearnToeic.entity.*;
import LearnToeic.service.TestServiceAdmin;
import LearnToeic.service.QuestionServiceAdmin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final TestServiceAdmin testService;
    private final QuestionServiceAdmin questionService;

    public AdminRestController(TestServiceAdmin testService, QuestionServiceAdmin questionService) {
        this.testService = testService;
        this.questionService = questionService;
    }

    @PutMapping("/tests/{id}/status")
    public ResponseEntity<Void> updateTestStatus(@PathVariable("id") Integer testId) {
        try {
            testService.updateTestStatus(testId);
            return ResponseEntity.ok().build(); // HTTP 200 OK
        } catch (Exception e) {
            // Log the error
            return ResponseEntity.badRequest().build(); // HTTP 400 Bad Request or 500 Internal Server Error
        }
    }

    @GetMapping("/tests/{testId}")
    public ResponseEntity<?> getTestDetails(@PathVariable int testId) {

        Test test = testService.getTestById(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }

        List<Question> questions = questionService.getQuestionsByTestId(testId);

        Map<String, Object> response = new HashMap<>();
        response.put("test", test);
        response.put("questions", questions);

        return ResponseEntity.ok(response);
    }



}
