package LearnToeic.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class RefId implements Serializable {

    private Integer testId;

    private Integer refId;


    public RefId() {}
    public RefId(Integer testId, Integer refId) {
        this.testId = testId;
        this.refId = refId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefId)) return false;
        RefId that = (RefId) o;
        return Objects.equals(testId, that.testId)
            && Objects.equals(refId, that.refId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId, refId);
    }
}
