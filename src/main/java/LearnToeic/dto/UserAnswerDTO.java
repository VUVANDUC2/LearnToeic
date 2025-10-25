package LearnToeic.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * Đại diện cho 1 dòng trong bảng user_answers nhưng ở tầng DTO.
 * Dùng được ở cả 2 chế độ:
 *  - Khi đang làm bài: chỉ cần takeId, questionNumber, selectedOption (có thể null), isCorrect = null, correctOption = null
 *  - Khi xem kết quả: isCorrect đã được chấm; correctOption có thể được "reveal" ra cho UI
 */
public class UserAnswerDTO implements Serializable {

    /** Khóa ngoại tới bài làm (take) */
    private Long takeId;

    /** Số câu trong đề (1..N) – là phần còn lại của khóa chính tổng hợp */
    private Integer questionNumber;

    /** Đáp án thí sinh chọn: 'A' | 'B' | 'C' | 'D' | null (bỏ trống) */
    private Character selectedOption;

    /**
     * Kết quả chấm điểm cho câu này:
     *   - null  : chưa chấm (đang làm bài)
     *   - true  : chọn đúng
     *   - false : chọn sai hoặc bỏ trống
     */
    private Boolean isCorrect;

    /**
     * Đáp án đúng (theo answer sheet). Thường:
     *   - null khi đang làm bài (không lộ đáp án)
     *   - 'A'/'B'/'C'/'D' khi review/đã nộp
     */
    private Character correctOption;

    // -------------------- ctor --------------------

    public UserAnswerDTO() {}

    public UserAnswerDTO(Long takeId,
                         Integer questionNumber,
                         Character selectedOption,
                         Boolean isCorrect,
                         Character correctOption) {
        this.takeId = takeId;
        this.questionNumber = questionNumber;
        this.selectedOption = normalizeOpt(selectedOption);
        this.isCorrect = isCorrect;
        this.correctOption = normalizeOpt(correctOption);
    }

    // -------------------- helpers (tiện cho Thymeleaf/UI) --------------------

    /** Có chọn đáp án hay chưa */
    public boolean isAnswered() {
        return selectedOption != null;
    }

    /** Dùng cho UI khi cần hiển thị nhãn trạng thái */
    public AnswerStatus getStatus() {
        if (isCorrect == null) {
            return isAnswered() ? AnswerStatus.ANSWERED : AnswerStatus.UNANSWERED;
        }
        if (Boolean.TRUE.equals(isCorrect)) return AnswerStatus.CORRECT;
        return isAnswered() ? AnswerStatus.INCORRECT : AnswerStatus.BLANK_INCORRECT;
    }

    /** Tính isCorrect nếu đã biết đáp án đúng (không mutate) */
    public UserAnswerDTO gradedWith(Character answerKey) {
        Character key = normalizeOpt(answerKey);
        Boolean result = (selectedOption != null && key != null && selectedOption.equals(key));
        return new UserAnswerDTO(takeId, questionNumber, selectedOption, result, key);
    }

    /** Trả về "A"/"B"/"C"/"D" hoặc null – hữu ích khi render JSON */
    public String getSelectedOptionStr() {
        return selectedOption == null ? null : String.valueOf(selectedOption);
    }

    public String getCorrectOptionStr() {
        return correctOption == null ? null : String.valueOf(correctOption);
    }

    /** Chuẩn hoá ký tự đáp án về chữ hoa A..D; khác thì trả null */
    private static Character normalizeOpt(Character c) {
        if (c == null) return null;
        char up = Character.toUpperCase(c);
        return (up == 'A' || up == 'B' || up == 'C' || up == 'D') ? up : null;
    }

    // -------------------- getters / setters --------------------

    public Long getTakeId() { return takeId; }
    public void setTakeId(Long takeId) { this.takeId = takeId; }

    public Integer getQuestionNumber() { return questionNumber; }
    public void setQuestionNumber(Integer questionNumber) { this.questionNumber = questionNumber; }

    public Character getSelectedOption() { return selectedOption; }
    public void setSelectedOption(Character selectedOption) { this.selectedOption = normalizeOpt(selectedOption); }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean correct) { isCorrect = correct; }

    public Character getCorrectOption() { return correctOption; }
    public void setCorrectOption(Character correctOption) { this.correctOption = normalizeOpt(correctOption); }

    // -------------------- equals / hashCode theo khóa (takeId, questionNumber) --------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAnswerDTO)) return false;
        UserAnswerDTO that = (UserAnswerDTO) o;
        return Objects.equals(takeId, that.takeId)
            && Objects.equals(questionNumber, that.questionNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(takeId, questionNumber);
    }

    // -------------------- enum trạng thái phục vụ UI --------------------

    public enum AnswerStatus {
        UNANSWERED,        // chưa chọn, chưa chấm (đang làm)
        ANSWERED,          // đã chọn, chưa chấm
        CORRECT,           // đã chọn và đúng
        INCORRECT,         // đã chọn và sai
        BLANK_INCORRECT    // bỏ trống và bị chấm sai
    }
}
