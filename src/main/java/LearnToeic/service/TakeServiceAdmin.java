package LearnToeic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import LearnToeic.repository.TakeRepositoryAdmin;
import LearnToeic.entity.Take;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class TakeServiceAdmin {

    @Autowired
    private TakeRepositoryAdmin takeRepository;

    public List<Take> getAllTakes() {
        return takeRepository.findAll();
    }

    public List<Take> getAllTakesByTestID(Integer testId) {
        return takeRepository.findAllByTestId(testId);
    }

    // public Map<Integer, Double> getAverageScorePerTest() {
    //     List<Object[]> results = takeRepository.findAverageScorePerTest();

    //     Map<Integer, Double> averageScores = new LinkedHashMap<>();
    //     for (Object[] row : results) {
    //         Integer testId = ((Number) row[0]).intValue();
    //         Double avgScore = ((Number) row[1]).doubleValue();
    //         averageScores.put(testId, avgScore);
    //     }

    //     return averageScores;
    // }

    public Map<Integer, Long> getTop5TestTakers() {
        List<Object[]> results = takeRepository.findUserAttemptsOrdered();

        Map<Integer, Long> top5 = new LinkedHashMap<>();
        for (Object[] row : results) {
            Integer userId = ((Number) row[0]).intValue();
            Long attempts = ((Number) row[1]).longValue();
            top5.put(userId, attempts);
        }

        return top5;
    }

    public Map<Integer, Long> getTop10MostTakenTests() {
        List<Object[]> results = takeRepository.findTop10MostTakenTest();

        Map<Integer, Long> top10 = new LinkedHashMap<>();
        for (Object[] row : results) {
            Integer testId = ((Number) row[0]).intValue();
            Long attempts = ((Number) row[1]).longValue();
            top10.put(testId, attempts);
        }

        return top10;
    }

    public Map<Integer, Long> getTestsTakenByMonth() {
        List<Object[]> results = takeRepository.findTestsTakenByMonth();

        Map<Integer, Long> monthly = new TreeMap<>();
        for (Object[] row : results) {
            Integer month = ((Number) row[0]).intValue();
            Long count = ((Number) row[1]).longValue();
            monthly.put(month, count);
        }

        return monthly;
    }

    public int getDistinctUserCount() {
        return takeRepository.countDistinctUsers();
    }
}
