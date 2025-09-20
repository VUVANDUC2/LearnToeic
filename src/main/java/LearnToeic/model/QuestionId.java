package LearnToeic.model;

import java.io.Serializable;
import java.util.Objects;

public class QuestionId implements Serializable {

    private Integer testId;
    private Integer questionNumber;

    // Default constructor is required by JPA
    public QuestionId() {}
    public QuestionId(Integer testId, Integer questionNumber) {
        this.testId = testId;
        this.questionNumber = questionNumber;
    }

    // Getters, setters, equals(), and hashCode()
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
