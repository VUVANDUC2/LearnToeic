package LearnToeic.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
@Embeddable
public class QuestionId implements Serializable {

    @Column(name = "test_id")
    private Integer testId;
    @Column(name = "question_number")
    private Integer questionNumber;

    // Default constructor is required by JPA
    public QuestionId() {}

    public QuestionId(Integer testId, Integer questionNumber) {
        this.testId = testId;
        this.questionNumber = questionNumber;
    }

    //  Getter & Setter
    public Integer getTestId() {
        return testId;
    }
    public void setTestId(Integer testId) {
        this.testId = testId;
    }
    public Integer getQuestionNumber() {
        return questionNumber;
    }
    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionId that = (QuestionId) o;
        return Objects.equals(testId, that.testId) &&
               Objects.equals(questionNumber, that.questionNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, questionNumber);
    }
}
