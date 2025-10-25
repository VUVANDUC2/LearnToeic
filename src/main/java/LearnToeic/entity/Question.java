package LearnToeic.entity;


import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question implements Serializable{

    @EmbeddedId
    private QuestionId id;

    @Column(name = "part")
    private Integer part;

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "option_a")
    private String optionA;

    @Column(name = "option_b")
    private String optionB;

    @Column(name = "option_c")
    private String optionC;

    @Column(name = "option_d")
    private String optionD;

    // @Column(name = "correct_option", length = 1)
    // private String correctOption;

    // @Column(name = "note", columnDefinition = "TEXT")
    // private String note;

    //  Getter & Setter
    public QuestionId getId() {
    return id;
    }

   public void setId(QuestionId id) {
    this.id = id;
   }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    // public String getCorrectOption() {
    //     return correctOption;
    // }

    // public void setCorrectOption(String correctOption) {
    //     this.correctOption = correctOption;
    // }

    // public String getNote() {
    //     return note;
    // }

    // public void setNote(String note) {
    //     this.note = note;
    // }
    // @Transient
    // public QuestionType getQuestionType() {
    //     return switch (part) {
    //     case 1 -> QuestionType.IMAGE_CHOICE;
    //     case 2 -> QuestionType.AUDIO_ONLY;
    //     case 3,4 -> QuestionType.AUDIO_PASSAGE;
    //     case 5 -> QuestionType.TEXT_BLANK;
    //     case 6 -> QuestionType.PASSAGE_BLANK;
    //     case 7 -> QuestionType.READING_COMP;
    //     default -> throw new IllegalStateException("Invalid part: " + part);
    //     };
    // }
}
