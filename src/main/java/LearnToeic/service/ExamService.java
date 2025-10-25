package LearnToeic.service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import LearnToeic.dto.QuestionForTakeDTO;
import LearnToeic.entity.Question;
import LearnToeic.entity.Take;
import LearnToeic.entity.UserAnswer;
import LearnToeic.entity.UserAnswerId;
import LearnToeic.repository.QuestionRepository;
import LearnToeic.repository.TakeRepository;
import LearnToeic.repository.UserAnswerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExamService {

    private final  TakeRepository takeRepo;
    private final  QuestionRepository questionRepo;
    private final  UserAnswerRepository userAnswerRepo;

    public List<QuestionForTakeDTO> getAllQuestions(Long takeId) {
        Take t = getTestTake(takeId);

        List<Question> qs = questionRepo
            .findById_TestIdOrderById_QuestionNumber(t.getTest().getTestId());
        List<UserAnswer> answers = userAnswerRepo.findByTake_TakeId(takeId);
        Map<Integer, UserAnswer> uaMap = new LinkedHashMap<>();
        for (UserAnswer ua : answers) {
            uaMap.put(ua.getId().getQuestionNumber(), ua);
        }


        return qs.stream()
            .map(q -> {
                UserAnswer ua = uaMap.get(q.getId());
                QuestionForTakeDTO dto = new QuestionForTakeDTO();

                dto.setPart(q.getPart());
                dto.setQuestionNumber(q.getId().getQuestionNumber());
                dto.setQuestionText(q.getQuestionText());
                dto.setOptionA(q.getOptionA());
                dto.setOptionB(q.getOptionB());
                dto.setOptionC(q.getOptionC());
                dto.setOptionD(q.getOptionD());
                dto.setSelectedOption(ua != null ? ua.getSelectedOption() : null);

                return dto;
            })
            .toList();
    }

    public Take getTestTake(Long takeId)
    {
        Take take = takeRepo.findById(takeId)
            .orElseThrow(() -> new RuntimeException("Take not found or not yours"));
        return  take;
    }
    public Map<Integer, Character> getSelectedAnswers(Long takeId)
    {
        // Map<QuestionId, UserAnswer> uaMap = userAnswerRepo.findByTake_TakeId(takeId);

        // return uaMap.entrySet().stream()
        //     .collect(Collectors.toMap(
        //         entry -> entry.getKey().getQuestionNumber(),       // key: số thứ tự câu hỏi
        //         entry -> entry.getValue().getSelectedOption()      // value: ký tự đáp án (A/B/C/D)
        //     ));
        // Lấy toàn bộ câu trả lời của người dùng trong lượt làm này
        List<UserAnswer> answers = userAnswerRepo.findByTake_TakeId(takeId);

        // Tạo Map<questionNumber, selectedOption>
        Map<Integer, Character> selectedMap = new LinkedHashMap<>();
        for (UserAnswer ua : answers) {
            selectedMap.put(ua.getId().getQuestionNumber(), ua.getSelectedOption());
        }

        return selectedMap;

    }
    /** Lưu/Update đáp án: nếu đã có bản ghi (takeId, questionId) → UPDATE, ngược lại INSERT. */
    @Transactional
    public void saveOrUpdateAnswer(Long takeId, Integer questionNumber, char selected) {

        // 1) UPDATE trước (nếu đã tồn tại)
        int updated = userAnswerRepo.updateAnswer(
                takeId,
                questionNumber,
                selected,
                null // isCorrect chưa tính ở đây
        );
        if (updated > 0) return;

        // 2) Nếu chưa có thì INSERT mới
        Take take = getTestTake(takeId); // bạn đã có hàm này rồi

        // Tạo composite key
        UserAnswerId uaId = new UserAnswerId(takeId, questionNumber);

        UserAnswer ua = new UserAnswer();
        ua.setId(uaId);
        ua.setTake(take);
        ua.setSelectedOption(selected);
        ua.setIsCorrect(null); // hoặc tính correct sau khi submit

        userAnswerRepo.save(ua);
    }
    @Transactional
    public void gradeTest(Long takeId) {

        Take take = getTestTake(takeId);
        Integer testId = take.getTest().getTestId();

        //Lấy correct option theo questionNumber
        Map<Integer, Character> correctMap = questionRepo
            .findById_TestIdOrderById_QuestionNumber(testId)
            .stream()
            .collect(Collectors.toMap(
                    Question::getQuestionNumber,
                    Question::getCorrectOption,                 // <-- Character
                    (a, b) -> a,                                 // nếu trùng key (phòng ngừa)
                    LinkedHashMap::new
            ));

        // Lấy các UserAnswer của attempt này
        List<UserAnswer> uaList =
                userAnswerRepo.findById_TakeIdOrderById_QuestionNumberAsc(takeId);

        int correctCount = 0;

        for (UserAnswer ua : uaList) {
            Character correct = correctMap.get(ua.getId().getQuestionNumber());
            boolean isCorrect = Objects.equals(ua.getSelectedOption(), correct);
            ua.setIsCorrect(isCorrect);
            if (isCorrect) correctCount++;
        }

        userAnswerRepo.saveAll(uaList);

        // Lưu kết quả vào Take
        // take.setScore(correctCount);
        // take.setSubmitted(true);
        // take.setSubmittedAt(Instant.now());
        // takeRepo.save(take);
    }
    @Transactional
    public void saveAnswers(Long takeId,
                            List<Map<String,Object>> answers,
                            boolean finalSubmit) {

        for (Map<String,Object> item : answers) {
            Integer qNo = (Integer) item.get("questionNumber");
            String sel  = (String)  item.get("selected");

            if (qNo == null || sel == null) continue;

            saveOrUpdateAnswer(takeId, qNo, sel.toUpperCase().charAt(0));
        }

        // Nếu chưa submit thì không làm gì thêm
        if (finalSubmit) {
            gradeTest(takeId);
        }
    }


}
