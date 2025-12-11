package LearnToeic.service;

import LearnToeic.entity.Test;
import LearnToeic.repository.TestRepositoryAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TestServiceAdmin {

    @Autowired
    private TestRepositoryAdmin testRepository;

    // Get all tests
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    // Paging with optional search term
    public Page<Test> getPaginateTests(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return testRepository.findByTestNameContainingIgnoreCase(searchTerm, pageable);
        }
        return testRepository.findAll(pageable);
    }

    // Get highest test ID + 1
    public int getHighestTestId() {
        return testRepository.getMaxTestId() + 1;
    }

    // Get test by ID
    public Test getTestById(int id) {
        return testRepository.findById(id).orElse(null);
    }

    

    // Save test
    public Test saveTest(String testName, String status) {
        Test test = new Test();
        test.setTestDate(new Date());
        test.setTotalQuestions(200);
        test.setMaxScore(990);
        test.setTestName(testName);
        test.setStatus(status);
        return testRepository.save(test);
    }

    // Delete test (update status to archived)
    public void updateTestStatus(Integer testId) {
        testRepository.updateStatusToArchived(testId);
    }
}
