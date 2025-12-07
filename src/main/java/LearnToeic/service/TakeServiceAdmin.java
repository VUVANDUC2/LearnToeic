package LearnToeic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import LearnToeic.repository.TakeRepositoryAdmin;
import LearnToeic.model.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
@Service
public class TakeServiceAdmin {
    @Autowired TakeRepositoryAdmin takeRepository;

    public List<TakeAdmin> getAllTakes(){
        return takeRepository.findAll();
    }

    public List<TakeAdmin> getAllTakesByTestID(Integer testId)
    {
        return takeRepository.findAllByTestId(testId);
    }

    public Map<Integer, Double> getAverageScorePerTest() {
        List<Object[]> results = takeRepository.findAverageScorePerTest();

        // Convert List<Object[]> to Map<testId, avgScore>
        Map<Integer, Double> averageScores = new LinkedHashMap<>();
        for (Object[] row : results) {
            Integer testId = (Integer) row[0];
            Double avgScore = ((Number) row[1]).doubleValue(); // AVG returns Number
            averageScores.put(testId, avgScore);
        }

        return averageScores;
    }

    public Map<Integer, Long> getTop5TestTakers(){
        List<Object[]> results = takeRepository.findUserAttemptsOrdered();

        Map<Integer,Long> top5 = new LinkedHashMap<>();
        for(Object[] row: results){
            Integer userId = (Integer) row[0];
            Long numberOfAttemps = (Long) row[1];
            top5.put(userId, numberOfAttemps);
        }

        return top5;
    }

    public Map<Integer, Long> getTop10MostTakenTests(){
        List<Object[]> results = takeRepository.findTop10MostTakenTest();

        Map<Integer,Long> top10 = new LinkedHashMap<>();
        for(Object[] row: results){
            Integer testId = (Integer) row[0];
            Long numberOfAttemps = (Long) row[1];
            top10.put(testId, numberOfAttemps);
        }

        return top10;
    }

    public Map<Integer, Long> getTestsTakenByMonth(){
    // It's likely you meant to call a different method here, but using the provided one for now.
    List<Object[]> results = takeRepository.findTestsTakenByMonth(); // Renamed for clarity

    // 💡 CHANGE: Use TreeMap instead of LinkedHashMap
    // TreeMap sorts keys automatically by natural order (ascending for Integers)
    Map<Integer, Long> list = new TreeMap<>(); 
    
    for(Object[] row: results){
        Integer month = (Integer) row[0];
        Long numberOfAttemps = (Long) row[1];
        list.put(month, numberOfAttemps);
    }

    return list;
}
    public int getDistinctUserCount(){
        return takeRepository.countDistinctUsers();
    }
}
