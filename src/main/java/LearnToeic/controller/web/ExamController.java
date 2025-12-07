package LearnToeic.controller.web;

import LearnToeic.dto.Exam.PartView;
import LearnToeic.dto.Exam.TakeHomeVM;
import LearnToeic.dto.Exam.TakeReviewVM;
import LearnToeic.security.AuthUtils;
import LearnToeic.service.ExamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


@Controller
@RequestMapping("/tests")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final AuthUtils authUtils;

    @GetMapping("/{testId}/start")
    public String startTest(@PathVariable Integer testId) {

        Integer userId = authUtils.currentUserId();
        Integer takeId = examService.startOrResume(userId, testId); // tạo hoặc lấy take

        return "redirect:/tests/take/" + takeId;  // chuyển sang trang làm bài
    }

    @GetMapping("/take/{takeId}")
    public String examHome(@PathVariable Integer takeId, Model model,
                           @RequestParam(name="part", defaultValue="1") int part) {
        var view = examService.buildTakeView(takeId);
        var pv = examService.buildPartView(takeId, part);
        model.addAttribute("pv", pv);
        model.addAttribute("view", view);
        model.addAttribute("review", false);
        return "tests/take-home";
    }

    @GetMapping("/take/{takeId}/part/{p}")
public String loadPart(@PathVariable Integer takeId,
                       @PathVariable int p,
                       Model model) {

    var view = examService.buildTakeView(takeId);
    var pv   = examService.buildPartView(takeId, p);

    model.addAttribute("takeId", takeId);
    model.addAttribute("view", view);
    model.addAttribute("pv", pv);

    // nếu đang trong mode review thì lấy luôn dữ liệu từ VM review
    if ("FINISHED".equalsIgnoreCase(view.status())) {
        var reviewVm = examService.buildTakeViewWithResults(takeId, p);
        model.addAttribute("review", true);
        model.addAttribute("selectedMap", reviewVm.selectedMap());
        model.addAttribute("correctMap", reviewVm.correctMap());
        model.addAttribute("isCorrectMap", reviewVm.isCorrectMap());
    } else {
        model.addAttribute("review", false);
        model.addAttribute("selectedMap", view.selectedMap());
        model.addAttribute("correctMap", Collections.emptyMap());
        model.addAttribute("isCorrectMap", Collections.emptyMap());
    }

    return "fragments/take-parts :: partBlock(pv=${pv}, takeId=${takeId})";
}


    @PostMapping("/{takeId}/submit")
    @Transactional
    public String submitExam(@PathVariable Integer takeId,
            @RequestParam("answersJson") String answersJson
            ) throws Exception {

        // Parse JSON {"101":"B","102":"D",...}
        ObjectMapper om = new ObjectMapper();
        Map<String, String> raw;
        if (answersJson == null || answersJson.isBlank()) {
            raw = Collections.emptyMap(); // không có câu nào
        } else {
            raw = om.readValue(answersJson, new TypeReference<Map<String,String>>(){});
        }

        // Chuyển sang Map<Integer, Character>
        Map<Integer, Character> answers = new HashMap<>();
        for (Map.Entry<String, String> e : raw.entrySet()) {
            Integer qn = Integer.valueOf(e.getKey());
            Character sel = e.getValue() != null && !e.getValue().isEmpty() ? e.getValue().charAt(0) : null;
            if (sel != null) answers.put(qn, sel);
        }

        examService.saveAllAndGrade(takeId, answers);   // <— chỉ lưu khi submit
        return "redirect:/tests/" + takeId + "/review?part=1";
    }

@GetMapping("/{takeId}/review")
public String reviewTake(
        @PathVariable Integer takeId,
        @RequestParam(name = "part", defaultValue = "1") int part,
        @RequestParam(required = false) Integer qn,
        Model model) {

    // 1) Build view chung (test info, partMap, v.v.)
    TakeHomeVM view = examService.buildTakeView(takeId); // như bạn đang dùng
    PartView pv = examService.buildPartView(takeId, part);
    TakeReviewVM review = examService.buildTakeViewWithResults(takeId, part);


    // 2) Map từ user_answers
    Map<Integer, Character> selectedMap = examService.buildSelectedMap(takeId);
    Map<Integer, Boolean> correctFlagMap = examService.buildCorrectFlagMap(takeId);


    // 3) Đẩy xuống model
    model.addAttribute("view", view);
    model.addAttribute("pv", pv);
    model.addAttribute("review", true);              // flag review
    model.addAttribute("selectedMap", selectedMap);  // dùng cho tô màu
    model.addAttribute("correctFlagMap", correctFlagMap);
    model.addAttribute("correctMap", review.correctMap());
    model.addAttribute("score", review.score());


    return "tests/take-home";
}
}
