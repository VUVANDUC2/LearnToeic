package LearnToeic.entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class AnswerSheetId implements Serializable{

    @Column(name = "test_id", nullable = false)
    private Integer testId;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    protected AnswerSheetId() {}

    public AnswerSheetId(Integer testId, Integer sequence) {
        this.testId = testId;
        this.sequence = sequence;
    }

    public Integer getTestId() {
        return testId;
    }
    public void setTestId(Integer testId) {
         this.testId = testId;
    }

    public Integer getSequence() {
         return sequence;
    }
    public void setSequence(Integer sequence) {
         this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerSheetId)) return false;
        AnswerSheetId that = (AnswerSheetId) o;
        return Objects.equals(testId, that.testId)
            && Objects.equals(sequence, that.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, sequence);
    }
}
