package LearnToeic.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import LearnToeic.dto.TestDTO;
import LearnToeic.entity.Test;
import LearnToeic.repository.TestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestListService {

    private final TestRepository testRepo;

    public Page<TestDTO> getAllTestsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("testDate").descending());
        Page<Test> testPage = testRepo.findAll(pageable);

        return testPage.map(test -> new TestDTO(
                test.getTestId(),
                test.getTestName(),
                test.getTestDate(),
                test.getTotalQuestions(),
                test.getMaxScore()
        ));
    }
    // 🔍 Search + phân trang
    public Page<TestDTO> searchTests(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("testDate").descending());

        // nếu không nhập gì -> trả về tất cả như bình thường
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTestsPage(page, size);
        }

        Page<Test> testPage = testRepo
                .findByTestNameContainingIgnoreCaseOrderByTestDateDesc(keyword.trim(), pageable);

        return testPage.map(test -> new TestDTO(
                test.getTestId(),
                test.getTestName(),
                test.getTestDate(),
                test.getTotalQuestions(),
                test.getMaxScore()
        ));
    }
}
