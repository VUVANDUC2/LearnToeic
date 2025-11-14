package LearnToeic.dto;

import java.util.Date;

public class TestDTO {

    private Integer testId;
    private String testName;
    private Date testDate;
    private Integer totalQuestions;
    private Integer maxscore;


    // Constructor
    public TestDTO(Integer testId, String testName, Date testDate, Integer totalQuestions, Integer maxscore) {
        this.testId = testId;
        this.testName = testName;
        this.testDate = testDate;
        this.totalQuestions = totalQuestions;
        this.maxscore = maxscore;
    }


    // Getter & Setter
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

    public Integer getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(Integer maxscore) {
        this.maxscore = maxscore;
    }
}
