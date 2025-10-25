package LearnToeic.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "user_answers")
public class UserAnswer implements Serializable {

    @EmbeddedId
    private UserAnswerId id;

    @MapsId("takeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "take_id", nullable = false)
    private Take take;

    @Column(name = "selected_option")
    private Character selectedOption;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Constructor
     public UserAnswer() {}

    public UserAnswer(Take take, Integer questionNumber, Character selectedOption, Boolean isCorrect) {
        this.take = take;
        this.id = new UserAnswerId(take.getTakeId(), questionNumber);
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
    }

    //  Getter & Setter
    public UserAnswerId getId() {
        return id;
    }
    public void setId(UserAnswerId id) {
        this.id = id;
    }
    public Take getTake() {
        return take;
    }

    public void setTake(Take take) {
        this.take = take;
    }
    public Character getSelectedOption() {
        return selectedOption;
    }
    public void setSelectedOption(Character selectedOption) {
        this.selectedOption = selectedOption;
    }
    public Boolean getIsCorrect() {
        return isCorrect;
    }
    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
