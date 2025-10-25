package LearnToeic.mapper;

import LearnToeic.dto.QuestionForTakeDTO;
import LearnToeic.entity.Question;
import LearnToeic.dto.QuestionType;

public final class QuestionMapper {
    private QuestionMapper() {}

    public static QuestionForTakeDTO toDTO(Question q) {
        QuestionForTakeDTO dto = new QuestionForTakeDTO();
        // dto.setId(q.getId() != null ? q.getId().getQuestionId() : null); // tùy composite id của bạn
        dto.setPart(q.getPart());
        dto.setQuestionType(resolveType(q.getPart()));

        dto.setQuestionNumber(q.getId() != null ? q.getId().getQuestionNumber() : null); // nếu bạn có
        dto.setQuestionText(q.getQuestionText());
        dto.setOptionA(q.getOptionA());
        dto.setOptionB(q.getOptionB());
        dto.setOptionC(q.getOptionC());
        dto.setOptionD(q.getOptionD());
        // map các field media/passage nếu có nơi khác

        return dto;
    }

    private static QuestionType resolveType(Integer part) {
        if (part == null) return null; // hoặc throw nếu bạn muốn cứng rắn
        return switch (part) {
            case 1 -> QuestionType.IMAGE_CHOICE;
            case 2 -> QuestionType.AUDIO_ONLY;
            case 3, 4 -> QuestionType.AUDIO_PASSAGE;
            case 5 -> QuestionType.TEXT_BLANK;
            case 6 -> QuestionType.PASSAGE_BLANK;
            case 7 -> QuestionType.READING_COMP;
            default -> throw new IllegalStateException("Invalid part: " + part);
        };
    }
}
