package LearnToeic.controller.web;

import LearnToeic.security.AuthUtils;
import LearnToeic.service.ExamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
    public String loadPart(@PathVariable Integer takeId, @PathVariable int p, Model model) {
        var pv = examService.buildPartView(takeId, p); // lấy câu hỏi part p
        var view = examService.buildTakeView(takeId);
        model.addAttribute("takeId", takeId);
        model.addAttribute("view", view);
        model.addAttribute("pv", pv);
        model.addAttribute("review", false);
        model.addAttribute("selectedMap", view.selectedMap());
        return "fragments/take-parts :: partBlock(pv=${pv}, takeId=${takeId})";
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String onError(Exception ex){
        return "<div class='alert alert-danger'>Lỗi máy chủ: "
            + (ex.getClass().getSimpleName()) + "</div>";
    }

    @PostMapping("/{takeId}/submit")
    @Transactional
    public String submitExam(@PathVariable Integer takeId,
            @RequestParam("answersJson") String answersJson
            ) throws Exception {

        // Parse JSON {"101":"B","102":"D",...}
        ObjectMapper om = new ObjectMapper();
        Map<String, String> raw = om.readValue(answersJson, new TypeReference<Map<String,String>>(){});

        // Chuyển sang Map<Integer, Character>
        Map<Integer, Character> answers = new HashMap<>();
        for (Map.Entry<String, String> e : raw.entrySet()) {
            Integer qn = Integer.valueOf(e.getKey());
            Character sel = e.getValue() != null && !e.getValue().isEmpty() ? e.getValue().charAt(0) : null;
            if (sel != null) answers.put(qn, sel);
        }

        examService.saveAllAndGrade(takeId, answers);   // <— chỉ lưu khi submit
        return "redirect:/tests/take/" + takeId + "/review";
    }
    @GetMapping("/take/{takeId}/review")
    public String reviewTake(
            @PathVariable Integer takeId,
            @RequestParam(name="part", defaultValue="1") int part,
            Model model) {

        var vm = examService.buildTakeViewWithResults(takeId, part); // có selectedMap, correctMap, isCorrectMap, score
        var pv = examService.buildPartView(takeId, part);

        model.addAttribute("view", vm);     // gồm: testId, testName, totalQuestions, score, partMap, selectedMap, correctMap, isCorrectMap, takeId
        model.addAttribute("pv", pv); // PartView hiện tại
        model.addAttribute("review", true); // bật chế độ review

        return "tests/take-home";
    }
}
