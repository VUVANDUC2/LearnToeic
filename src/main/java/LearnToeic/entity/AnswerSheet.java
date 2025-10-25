package LearnToeic.entity;

import java.io.Serializable;

import jakarta.persistence.*;
@Entity
@Table(name = "answer_sheets")
public class AnswerSheet implements Serializable{
    @EmbeddedId
    private AnswerSheetId id;

    // Nếu bạn có entity Test với @Id Integer testId:
    @MapsId("testId") // map khóa con testId của AnswerSheetId sang khóa chính Test
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(name = "correct_option", length = 255)
    private String correctOption; // có thể null theo DDL

    protected AnswerSheet() {} // JPA cần

    public AnswerSheet(AnswerSheetId id, Test test, String correctOption) {
        this.id = id;
        this.test = test;
        this.correctOption = correctOption;
    }

    public AnswerSheetId getId() { return id; }
    public void setId(AnswerSheetId id) { this.id = id; }

    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }

    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }
}
