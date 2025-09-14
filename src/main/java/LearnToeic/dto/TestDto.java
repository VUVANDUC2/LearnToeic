package LearnToeic.dto;

public class TestDto {
    private Long testId;          // ID của bài test
    private String testName;      // Tên bài test
    private int totalQuestions;   // Tổng số câu hỏi

    public TestDto() {
    }

    public TestDto(Long testId, String testName, int totalQuestions) {
        this.testId = testId;
        this.testName = testName;
        this.totalQuestions = totalQuestions;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    @Override
    public String toString() {
        return "TestDto{" +
                "testId=" + testId +
                ", testName='" + testName + '\'' +
                ", totalQuestions=" + totalQuestions +
                '}';
    }
}
