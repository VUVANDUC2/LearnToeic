package LearnToeic.entity;

import java.util.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testId;

    private String testName;

    private Date testDate;

    private Integer totalQuestions;

    private Integer maxScore;

    private String description;

    // Constructor
    public Test() {
    }

    public Test(String testName, Date testDate, int totalQuestions, Integer maxScore, String description) {
        this.testName = testName;
        this.testDate = testDate;
        this.totalQuestions = totalQuestions;
        this.maxScore = maxScore;
        this.description = description;
    }

    //  Getter & Setter
    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
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

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
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
