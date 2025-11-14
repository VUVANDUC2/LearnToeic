package LearnToeic.dto.Exam;

import java.time.Instant;
import java.util.*;

public record  TakeHomeVM (
    Integer takeId,
    Integer testId,
    String testName,
    Integer totalQuestions,
    Integer selectedCount,
    String status,
    Instant startTime,
    Instant endTime,
    Integer timeRemaining,
    Map<Integer, Character> selectedMap,
    Map<Integer, List<Integer>> partMap
){}
