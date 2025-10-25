package LearnToeic.controller.web;

import LearnToeic.dto.QuestionForTakeDTO;
import LearnToeic.entity.Take;
import LearnToeic.service.ExamService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tests")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping("/{takeId}")
    public String examTake(@PathVariable Long takeId,
                          @RequestParam(defaultValue = "1") int part,
                          Model model) {

        Take take = examService.getTestTake(takeId);//lấy thông tin lượt làm bài
        // 2) Nếu lần đầu thì set endTime
        if (take.getEndTime() == null) {
            take.setEndTime(Instant.now().plusSeconds(120 * 60));
            //takeService.save(take);
        }
        List<QuestionForTakeDTO> allQuestions = examService.getAllQuestions(takeId); // lấy tất cả câu hỏi của mỗi lần làm bài

        // Lọc câu hỏi theo Part
        List<QuestionForTakeDTO> partQuestions = allQuestions.stream()
            .filter(q -> q.getPart() == part)
            .collect(Collectors.toList());

        // Nhóm câu hỏi theo groupId và đánh dấu câu đầu tiên
        // Map<Integer, List<QuestionForTakeDTO>> grouped = partQuestions.stream()
        //     .filter(q -> q.getGroupId() != null)
        //     .collect(Collectors.groupingBy(QuestionForTakeDTO::getGroupId));

        // grouped.forEach((gid, list) -> {
        //     if (!list.isEmpty()) {
        //         list.sort(Comparator.comparing(QuestionForTakeDTO::getQuestionNumber));
        //         list.get(0).setIsFirstInGroup(true);
        //     }
        // });

        // Đánh dấu câu không thuộc nhóm là first (Part 1, 2, 5)
        // partQuestions.stream()
        //     .filter(q -> q.getGroupId() == null)
        //     .forEach(q -> q.setIsFirstInGroup(true));

        // Load đáp án đã chọn
        Map<Integer, Character> selectedMap = examService.getSelectedAnswers(takeId);
        partQuestions.forEach(q ->
            q.setSelectedOption(selectedMap.get(q.getQuestionNumber()))
        );

        // Tạo partMap cho sidebar (7 parts)
        Map<Integer, List<QuestionForTakeDTO>> partMap = allQuestions.stream()
            .collect(Collectors.groupingBy(QuestionForTakeDTO::getPart));

        model.addAttribute("view", take);
        model.addAttribute("currentPart", part);
        model.addAttribute("partQuestions", partQuestions);
        model.addAttribute("partMap", partMap);
        model.addAttribute("selectedMap", selectedMap);
        model.addAttribute("review", false);

        return "tests/text";
    }
    @PostMapping("/{takeId}/submit")
    public ResponseEntity<?> submitTest(@PathVariable Long takeId,
                                       @RequestBody Map<String, Object> payload) {

        try {
            // 1. Lấy danh sách đáp án
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> answers =
                (List<Map<String, Object>>) payload.get("answers");

            boolean finalSubmit = Boolean.TRUE.equals(payload.get("submit"));

            // 2. Lưu đáp án vào DB
            examService.saveAnswers(takeId, answers, finalSubmit);

            // 3. Nếu là submit cuối cùng, chấm điểm
            if (finalSubmit) {
                examService.gradeTest(takeId);
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Nộp bài thành công!"
                ));
            }

            return ResponseEntity.ok(Map.of(
                "status", "saved",
                "message", "Lưu đáp án thành công"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

}
