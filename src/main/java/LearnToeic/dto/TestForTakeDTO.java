package LearnToeic.dto;

import java.time.Instant;
import java.util.List;

import LearnToeic.entity.Take;
import LearnToeic.entity.Test;

public class TestForTakeDTO {
    private Long takeId;
    private Integer testId;
    private String testName;
    private Integer attemptNo;
    private Integer totalQuestions;
    private String status;
    private Instant startTime;
    private Instant endTime;
    private List<QuestionForTakeDTO> questions;

    // Constructor
    public TestForTakeDTO(Take take, Test test) {
        this.takeId = take.getTakeId();
        this.attemptNo = take.getAttemptNo();
        this.status = take.getStatus();
        this.startTime = take.getStartTime();
        this.endTime = take.getEndTime();

        if (test != null) {
            this.testId = test.getTestId();
            this.testName = test.getTestName();
            this.totalQuestions = test.getTotalQuestions();
        }
    }

    public TestForTakeDTO() {}

    // Getter & Setter
    public Long getTakeId() {
        return takeId;
    }
    public void setTakeId(Long takeId) {
        this.takeId = takeId;
    }
    public Integer getTestId() {
        return testId;
    }
    public void setTestId(Integer testId) {
        this.testId = testId;
    }
    public String getTestName() {
        return testName;
    }
    public Integer getAttemptNo() {
        return attemptNo;
    }
    public void setAttemptNo(Integer attemptNo) {
        this.attemptNo = attemptNo;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public Integer getTotalQuestions() {
        return totalQuestions;
    }
    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Instant getStartTime() {
        return startTime;
    }
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
    public Instant getEndTime() {
        return endTime;
    }
    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
    public List<QuestionForTakeDTO> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuestionForTakeDTO> questions) {
        this.questions = questions;
    }
}
