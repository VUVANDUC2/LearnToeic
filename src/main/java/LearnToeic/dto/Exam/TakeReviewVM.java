package LearnToeic.dto.Exam;

import java.util.List;
import java.util.Map;

public record TakeReviewVM(
        Integer takeId,
        Integer testId,
        String testName,
        Integer totalQuestions,
        Integer correctCount,                  // số câu đúng
        Integer wrongCount,                    // số câu sai
        Integer score,                         // điểm (quy đổi theo maxScore)
        Integer maxScore,
        Integer currentPart,
        long remaining,                // part đang review
        Map<Integer, List<Integer>> partMap,   // Map<partNumber, List<questionNumber>>
        Map<Integer, Character> selectedMap,   // Map<questionNumber, selectedOption>
        Map<Integer, Character> correctMap,    // Map<questionNumber, correctOption>
        Map<Integer, Boolean> isCorrectMap     // Map<questionNumber, isCorrect?>
) {}
