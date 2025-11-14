package LearnToeic.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "user_answers")
public class UserAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "take_id", nullable = false)
    private Take take;

    @Column(name = "question_number", nullable = false)
    private Integer questionNumber;

    @Column(name = "selected_option", length = 1)
    private Character selectedOption;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    protected UserAnswer() {}

    public UserAnswer(Take take, Integer questionNumber, Character selectedOption, Boolean isCorrect) {
        this.take = take;
        this.questionNumber = questionNumber;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
    }


    //  Getter & Setter
    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Take getTake() {
        return take;
    }

    public void setTake(Take take) {
        this.take = take;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
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
