package LearnToeic.dto.Exam;

import java.time.LocalDateTime;
import java.util.*;

public record  TakeHomeVM (
    Integer takeId,
    Integer testId,
    String testName,
    Integer totalQuestions,
    Integer selectedCount,
    String status,
    LocalDateTime startTime,
    LocalDateTime endTime,
    long remaining,
    Map<Integer, Character> selectedMap,
    Map<Integer, List<Integer>> partMap
){}
