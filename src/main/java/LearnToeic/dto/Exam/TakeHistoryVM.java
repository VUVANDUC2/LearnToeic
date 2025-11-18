package LearnToeic.dto.Exam;

import java.time.LocalDateTime;

public record TakeHistoryVM(
        Integer takeId,
        Integer testId,
        String testName,
        Integer attemptNo,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer score
) {}
