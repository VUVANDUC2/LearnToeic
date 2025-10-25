package LearnToeic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserAnswerId implements Serializable {

    @Column(name = "take_id", nullable = false)
    private Long takeId;

    @Column(name = "question_number", nullable = false)
    private Integer questionNumber;

    //  Getter & Setter
    public Long getTakeId() {
        return takeId;
    }
    public void setTakeId(Long takeId) {
        this.takeId = takeId;
    }
    public Integer getQuestionNumber() {
        return questionNumber;
    }
    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }
    // Constructor
    public UserAnswerId() {}

    public UserAnswerId(Long takeId, Integer questionNumber) {
        this.takeId = takeId;
        this.questionNumber = questionNumber;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAnswerId)) return false;
        UserAnswerId that = (UserAnswerId) o;
        return Objects.equals(takeId, that.takeId)
            && Objects.equals(questionNumber, that.questionNumber);
    }

    @Override public int hashCode() { return Objects.hash(takeId, questionNumber); }
}

