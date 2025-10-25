package LearnToeic.dto;

import java.util.List;

public class SubmitPayload {

    private List<UserAnswerDTO> answers;
    private Boolean submit;

    public SubmitPayload() {}

    public List<UserAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<UserAnswerDTO> answers) {
        this.answers = answers;
    }

    public Boolean getSubmit() {
        return submit;
    }

    public void setSubmit(Boolean submit) {
        this.submit = submit;
    }
}
