package LearnToeic.dto;

import java.util.Collections;
import java.util.List;

public class QuestionForTakeDTO {
    private Integer part;
    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    private Integer questionNumber;
    private QuestionType questionType;
    public QuestionType getQuestionType() {
        if (questionType == null && part != null) {
        return switch (part) {
            case 1 -> QuestionType.IMAGE_CHOICE;
            case 2 -> QuestionType.AUDIO_ONLY;
            case 3,4 -> QuestionType.AUDIO_PASSAGE;
            case 5 -> QuestionType.TEXT_BLANK;
            case 6 -> QuestionType.PASSAGE_BLANK;
            case 7 -> QuestionType.READING_COMP;
            default -> null;
        };
    }
    return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Character selectedOption;
    private List<String> imageUrls = Collections.emptyList();
    private List<String> audioUrls = Collections.emptyList();
    private List<String> textHtmlBlocks = Collections.emptyList();
    private String textHtmlTitle;

    public Character getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Character selectedOption) {
        this.selectedOption = selectedOption;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            this.imageUrls = Collections.emptyList();
        } else {
            this.imageUrls = List.copyOf(imageUrls);
        }
    }

    public boolean hasImage() {
        return imageUrls != null && !imageUrls.isEmpty();
    }

    public List<String> getAudioUrls() {
        return audioUrls;
    }

    public void setAudioUrls(List<String> audioUrls) {
        if (audioUrls == null || audioUrls.isEmpty()) {
            this.audioUrls = Collections.emptyList();
        } else {
            this.audioUrls = List.copyOf(audioUrls);
        }
    }

    public boolean hasAudio() {
        return audioUrls != null && !audioUrls.isEmpty();
    }

    public List<String> getTextHtmlBlocks() {
        return textHtmlBlocks;
    }

    public void setTextHtmlBlocks(List<String> textHtmlBlocks) {
        if (textHtmlBlocks == null || textHtmlBlocks.isEmpty()) {
            this.textHtmlBlocks = Collections.emptyList();
        } else {
            this.textHtmlBlocks = List.copyOf(textHtmlBlocks);
        }
    }

    public boolean hasTextHtml() {
        return textHtmlBlocks != null && !textHtmlBlocks.isEmpty();
    }

    public String getTextHtmlTitle() {
        return textHtmlTitle;
    }

    public void setTextHtmlTitle(String textHtmlTitle) {
        this.textHtmlTitle = textHtmlTitle;
    }

    public QuestionForTakeDTO() {
    }

    public QuestionForTakeDTO(int part,int questionNumber, String questionText,
                       String optionA, String optionB,
                       String optionC, String optionD) {
        this.part = part;
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
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

    @Override
    public String toString() {
        return "QuestionDto{" +
                "questionNumber=" + questionNumber +
                ", questionText='" + questionText + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                '}';
    }
}
