package LearnToeic.controller;
import LearnToeic.model.Test;
import LearnToeic.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable int id) {
        Optional<Test> test = testService.getTestById(id);
        return test.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Test createTest(@RequestBody Test test) {
        return testService.saveTest(test);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable int id, @RequestBody Test testDetails) {
        Optional<Test> testOptional = testService.getTestById(id);
        if (testOptional.isPresent()) {
            Test test = testOptional.get();
            test.setTestName(testDetails.getTestName());
            test.setTestDate(testDetails.getTestDate());
            test.setTotalQuestions(testDetails.getTotalQuestions());
            test.setMaxScore(testDetails.getMaxScore());
            test.setDescription(testDetails.getDescription());
            return ResponseEntity.ok(testService.saveTest(test));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable int id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}
