package LearnToeic.dto.Profile;

import LearnToeic.dto.Exam.TakeHistoryVM;

public record ProfileDashboardVM(
        int totalTests,
        int completedWithScore,
        Double averageScore,
        Integer bestScore,
        TakeHistoryVM latestTake
) {}
