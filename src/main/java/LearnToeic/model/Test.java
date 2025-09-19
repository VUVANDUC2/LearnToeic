package LearnToeic.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "tests")
public class Test {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int testId;

    private String testName;

    private Date testDate;

    private int totalQuestions;

    private Integer maxScore;

    private String description;

    // Constructors, Getters, and Setters
    public Test() {
    }

    public Test(String testName, Date testDate, int totalQuestions, Integer maxScore, String description) {
        this.testName = testName;
        this.testDate = testDate;
        this.totalQuestions = totalQuestions;
        this.maxScore = maxScore;
        this.description = description;
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

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
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
}