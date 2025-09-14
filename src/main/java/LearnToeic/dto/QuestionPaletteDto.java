package LearnToeic.dto;

public class QuestionPaletteDto {
    private boolean answered; // true nếu đã chọn đáp án
    private boolean flagged;  // true nếu cắm cờ

    public QuestionPaletteDto() {
    }

    public QuestionPaletteDto(boolean answered, boolean flagged) {
        this.answered = answered;
        this.flagged = flagged;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    @Override
    public String toString() {
        return "QuestionPaletteDto{" +
                "answered=" + answered +
                ", flagged=" + flagged +
                '}';
    }
}
