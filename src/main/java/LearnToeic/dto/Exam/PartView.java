package LearnToeic.dto.Exam;

import java.util.List;

import LearnToeic.dto.QuestionForTakeDTO;

public class PartView {
    private int part;                     // Part đang xem (1..7)
    private String rangeLabel;            // Ví dụ: "Q14–Q23"
    private List<QuestionForTakeDTO> questions;   // Danh sách câu hỏi trong part
    private List<String> audioSources = List.of(); // Các audio áp dụng cho toàn part

    public PartView() {}

    public PartView(int part, String rangeLabel, List<QuestionForTakeDTO> questions) {
        this.part = part;
        this.rangeLabel = rangeLabel;
        this.questions = questions;
    }

    public int getPart() { return part; }
    public void setPart(int part) { this.part = part; }

    public String getRangeLabel() { return rangeLabel; }
    public void setRangeLabel(String rangeLabel) { this.rangeLabel = rangeLabel; }

    public List<QuestionForTakeDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionForTakeDTO> questions) {
        this.questions = questions;
    }

    public List<String> getAudioSources() {
        return audioSources;
    }

    public void setAudioSources(List<String> audioSources) {
        if (audioSources == null || audioSources.isEmpty()) {
            this.audioSources = List.of();
        } else {
            this.audioSources = List.copyOf(audioSources);
        }
    }

    public boolean hasAudio() {
        return audioSources != null && !audioSources.isEmpty();
    }
}
