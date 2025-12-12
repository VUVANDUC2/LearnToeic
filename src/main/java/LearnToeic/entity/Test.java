package LearnToeic.entity;

import java.util.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Integer testId;

    @Column(name = "test_name", nullable = false, length = 255)
    private String testName;

    @Temporal(TemporalType.DATE)
    @Column(name = "test_date")
    private Date testDate;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "max_score")
    private Integer maxScore;


    @Column(name = "status", length = 50)
    private String status; 

    
    public Test() {}

    public Test(String testName, Date testDate, Integer totalQuestions, Integer maxScore) {
        this.testName = testName;
        this.testDate = testDate;
        this.totalQuestions = totalQuestions;
        this.maxScore = maxScore;
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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
