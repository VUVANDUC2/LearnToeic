package LearnToeic.controller;

import LearnToeic.dto.TestDto;
import LearnToeic.dto.QuestionDto;
import LearnToeic.dto.QuestionPaletteDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @GetMapping
    public String showExamDirect(Model model) {
        int takeId = 1;
        int index = 0;

        // BẮT BUỘC: test & question để template không null
        TestDto test = new TestDto(1L, "TOEIC Demo Test", 5);
        QuestionDto question = new QuestionDto(
                index + 1,
                "Câu hỏi demo số " + (index + 1) + ": Nội dung hiển thị ở đây.",
                "Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"
        );

        // Palette demo
        List<QuestionPaletteDto> palette = new ArrayList<>();
        for (int i = 0; i < test.getTotalQuestions(); i++) {
            palette.add(new QuestionPaletteDto(false, false));
        }

        // GÁN MODEL — các key này khớp với exam.html
        model.addAttribute("test", test);
        model.addAttribute("takeId", takeId);

        model.addAttribute("question", question);
        model.addAttribute("selectedOption", null);
        model.addAttribute("questionPalette", palette);

        model.addAttribute("answeredCount", 0);
        model.addAttribute("flaggedCount", 0);

        model.addAttribute("currentIndex", index);
        model.addAttribute("prevIndex", Math.max(0, index - 1));
        model.addAttribute("nextIndex", Math.min(test.getTotalQuestions() - 1, index + 1));
        model.addAttribute("hasPrev", index > 0);
        model.addAttribute("hasNext", index < test.getTotalQuestions() - 1);

        model.addAttribute("currentPart", 1);
        model.addAttribute("timeRemainingSeconds", 300); // 5 phút

        return "exam"; // src/main/resources/templates/exam.html
    }
}
