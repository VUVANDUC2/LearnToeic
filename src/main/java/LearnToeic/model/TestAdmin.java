package LearnToeic.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tests")
public class TestAdmin {
    
    @Id
    private int testId;

    private String testName;

    private LocalDate testDate; 

    private int totalQuestions;

    private Integer maxScore;

    private String description;

    private String status;

    
    // Constructors, Getters, and Setters
    public TestAdmin() {
    }

    public TestAdmin(String testName, LocalDate testDate, int totalQuestions, Integer maxScore, String description, String status) {
        this.testName = testName;
        this.testDate = testDate;
        this.totalQuestions = totalQuestions;
        this.maxScore = maxScore;
        this.description = description;
        this.status = status;        
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}