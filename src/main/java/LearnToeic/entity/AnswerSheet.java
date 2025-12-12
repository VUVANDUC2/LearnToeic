package LearnToeic.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_sheets")
public class AnswerSheet implements Serializable{

    @EmbeddedId
    private AnswerSheetId id;

    @Column(name = "correct_option", nullable = false)
    private String correctOption;

    protected AnswerSheet() {}

    public AnswerSheet(AnswerSheetId id,  String correctOption) {
        this.id = id;
        this.correctOption = correctOption;
    }

    public AnswerSheetId getId() {
        return id;
    }
    public void setId(AnswerSheetId id) {
        this.id = id;
    }

    public String getCorrectOption() {
        return correctOption;
    }
    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }
}
