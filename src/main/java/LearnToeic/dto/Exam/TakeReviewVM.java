package LearnToeic.dto.Exam;

import java.util.List;
import java.util.Map;

public record TakeReviewVM(
        Integer takeId,
        Integer testId,
        String testName,
        Integer totalQuestions,
        Integer score,                         // lấy từ Take hoặc tính nếu null
        Integer currentPart,
        long remaining,                // part đang review
        Map<Integer, List<Integer>> partMap,   // Map<partNumber, List<questionNumber>>
        Map<Integer, Character> selectedMap,   // Map<questionNumber, selectedOption>
        Map<Integer, Character> correctMap,    // Map<questionNumber, correctOption>
        Map<Integer, Boolean> isCorrectMap     // Map<questionNumber, isCorrect?>
) {}
