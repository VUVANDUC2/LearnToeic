package LearnToeic.service;


import LearnToeic.model.TestAdmin;
import LearnToeic.repository.TestRepositoryAdmin;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Service
public class TestServiceAdmin {

    @Autowired
    private TestRepositoryAdmin testRepository;
    // Get all tests
    public List<TestAdmin> getAllTests() {
        return testRepository.findAll();
    }


    //Paging 
    public Page<TestAdmin> getPaginateTests(String searchTerm, int page, int size)
    {   
        Pageable pageable = PageRequest.of(page, size);
        if(searchTerm != null && !searchTerm.trim().isEmpty()){
            return testRepository.findByTestNameContainingIgnoreCase(searchTerm, pageable);
        }
        
        return testRepository.findAll(pageable);
    }


    //Get highest ID + 1
    public int getHighestTestId(){
        Integer maxId = testRepository.getMaxTestId()+1;
        return (maxId!= null)? maxId:0;
    }
    // Get test by ID
    public Optional<TestAdmin> getTestById(int id) {
        return testRepository.findById(id);
    }
    // Save test
    public TestAdmin saveTest(String testName, String status) {
        TestAdmin test = new TestAdmin();
        test.setTestId(getHighestTestId());
        test.setTestDate(LocalDate.now());
        test.setTotalQuestions(200);
        test.setMaxScore(990);
        test.setDescription("New test uploaded");
        test.setTestName(testName);
        test.setStatus(status);
        return testRepository.save(test);
    }

    //Delete Test (update status to archived)
    public void updateTestStatus(Integer testId) {
        testRepository.updateStatusToArchived(testId);
    }


}
