package LearnToeic.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "take_id", nullable = false, unique = true)
    private Take take;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "taken_on", nullable = false)
    private LocalDateTime takenOn;

    // getter/setter
    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Take getTake() {
        return take;
    }

    public void setTake(Take take) {
        this.take = take;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getTakenOn() {
        return takenOn;
    }

    public void setTakenOn(LocalDateTime takenOn) {
        this.takenOn = takenOn;
    }
}
