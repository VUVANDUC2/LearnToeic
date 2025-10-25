package LearnToeic.controller.web;

import LearnToeic.dto.TestForTakeDTO;
import LearnToeic.service.TakeViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestWebController {

    private final LearnToeic.repository.TestRepository testRepo;
    private final LearnToeic.service.UserQueryService userQueryService; // service tiện ích để lấy userId hiện tại
    private final TakeViewService takeViewService;

    // Danh sách đề thi (ai cũng xem được)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("tests", testRepo.findAllByOrderByTestDateDesc());
        return "tests/list";
    }

    // BẮT ĐẦU / RESUME bài thi → cần đăng nhập
    // Dùng GET để Security "ghi nhớ" SavedRequest, login xong quay lại URL này.
    @GetMapping("/{testId}/start")
    public String start(@PathVariable Integer testId, Authentication auth) {
        // Lấy userId từ SecurityContext (tránh tự set session)
        Integer userId = userQueryService.getCurrentUserId(auth);
        // Khởi tạo hoặc resume bài làm cho user + test
        TestForTakeDTO dto = takeViewService.startOrResume(userId, testId);

        // Điều hướng sang trang làm bài của bạn
        // (Bạn đang hiển thị làm bài ở /tests/{takeId}?q=1 → giữ nguyên để không phải sửa view)
        return "redirect:/tests/" + dto.getTakeId() + "?q=1";
    }
}
