package LearnToeic.controller;

import LearnToeic.model.TestAdmin;
import LearnToeic.model.QuestionAdmin;
import LearnToeic.service.TestServiceAdmin;
import LearnToeic.service.QuestionServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final TestServiceAdmin testService;

    public AdminRestController(TestServiceAdmin testService) {
        this.testService = testService;
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
}
