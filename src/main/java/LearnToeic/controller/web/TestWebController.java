package LearnToeic.controller.web;

import LearnToeic.dto.TestDTO;
import LearnToeic.service.TestListService;
import LearnToeic.service.UserQueryService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TestWebController {

    private final TestListService testService;
    private final UserQueryService userQueryService;

    // Danh sách đề (phân trang)
    @GetMapping("/tests")
    public String listTests(Model model,
                            @RequestParam(defaultValue = "0") int page) {
        int pageSize = 8; // chỉ 8 đề mỗi trang
        Page<TestDTO> testPage = testService.getAllTestsPage(page, pageSize);

        model.addAttribute("tests", testPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", testPage.getTotalPages());
        model.addAttribute("pageTitle", "Thư viện đề thi • LEARN TOEIC");

        return "tests/list";
    }
    // 🔍 Tìm kiếm đề + phân trang
    @GetMapping("/tests/search")
    public String searchTests(Model model,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "0") int page) {

        int pageSize = 8;
        Page<TestDTO> testPage = testService.searchTests(keyword, page, pageSize);

        model.addAttribute("tests", testPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", testPage.getTotalPages());
        model.addAttribute("keyword", keyword); // để giữ lại text trong ô search
        model.addAttribute("pageTitle", "Tìm kiếm đề thi • LEARN TOEIC");

        return "tests/list";
    }
}
