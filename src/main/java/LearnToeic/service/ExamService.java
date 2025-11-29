package LearnToeic.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import LearnToeic.dto.*;
import LearnToeic.dto.Exam.*;
import LearnToeic.entity.*;
import LearnToeic.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExamService {

    private final  TakeRepository takeRepo;
    private final  QuestionRepository questionRepo;
    private final UserAnswerRepository userAnswerRepo;
    private final TestRepository testRepo;
    private final UserRepository userRepo;
    private final TestResultRepository testResultRepo;
    private final AnswerSheetRepository answerSheetRepo;

    public PartView buildPartView(Integer takeId, int part) {

        // 1) lấy testId từ takeId
        Integer testId = takeRepo.findTestIdByTakeId(takeId);

        // 2) LẤY CÂU HỎI TỪ BẢNG questions THEO testId + part
        List<Question> qs  =
                questionRepo.findById_TestIdAndPartOrderById_QuestionNumber(testId, part);

        List<QuestionForTakeDTO> dtos = qs.stream()
                                                .map(q -> new QuestionForTakeDTO(
                                                        q.getPart(),
                                                        q.getId().getQuestionNumber(),
                                                        q.getQuestionText(),
                                                        q.getOptionA(),
                                                        q.getOptionB(),
                                                        q.getOptionC(),
                                                        q.getOptionD()
                                                    ))
                                                .toList();

        // 3) lấy câu trả lời đã chọn (nếu có) để check lại radio
        Map<Integer, Character> selectedMap = userAnswerRepo
                .findByTake_TakeId(takeId)
                .stream()
                .collect(Collectors.toMap(
                UserAnswer::getQuestionNumber,
                UserAnswer::getSelectedOption,
                (a, b) -> b,                    // nếu trùng key, lấy giá trị mới
                LinkedHashMap::new
            ));
        for (QuestionForTakeDTO q : dtos) {
            Character sel = selectedMap.get(q.getQuestionNumber());
            q.setSelectedOption(sel); // field này phải có trong DTO
        }
        // 4) map sang ViewModel để đẩy sang view
        PartView pv = new PartView();
        pv.setPart(part);
        pv.setQuestions(dtos);
        pv.setRangeLabel(makeRangeLabel(dtos)); // "Qxx–Qyy"
        return pv;
    }
    private String makeRangeLabel(List<QuestionForTakeDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return "";
        int first = dtos.get(0).getQuestionNumber();
        int last  = dtos.get(dtos.size()-1).getQuestionNumber();
        return "Q" + first + "–Q" + last;
    }

    public TakeHomeVM buildTakeView(Integer takeId) {
        // 1) Lấy Take (kèm Test)
        Take take = takeRepo.findById(takeId)
                .orElseThrow(() -> new RuntimeException("Take not found: " + takeId));

        Test test = take.getTest();
        Integer testId = test.getTestId();
        String testName = Optional.ofNullable(test.getTestName()).orElse("Untitled");
        String status = Optional.ofNullable(take.getStatus()).orElse("STARTED");

        // 2) Tổng số câu (ưu tiên lấy từ cột total_questions, fallback đếm từ questions)
        Integer totalQuestions = Optional.ofNullable(test.getTotalQuestions())
                .orElseGet(() -> questionRepo.countById_TestId(testId));

        // 3) Số câu đã chọn (tiến độ)
        int selectedCount = userAnswerRepo
                .countByTake_TakeIdAndSelectedOptionIsNotNull(takeId);

        // 4) Thời gian còn lại (mặc định 120 phút)
        // LocalDateTime now = LocalDateTime.now();
        // long remaining = Duration.between(now, take.getStartTime()).getSeconds();
        // if (remaining < 0) remaining = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = take.getStartTime();
        LocalDateTime end = start.plusMinutes(120);

        long remaining = Duration.between(now, end).getSeconds();
        if (remaining < 0) remaining = 0; // hết giờ thì = 0



        Map<Integer, List<Integer>> partMap = buildPartMap(testId);

        Map<Integer, Character> selectedMap = userAnswerRepo
        .findByTake_TakeId(takeId)
        .stream()
        .collect(Collectors.toMap(
            UserAnswer::getQuestionNumber,
            UserAnswer::getSelectedOption
        ));


        return new TakeHomeVM(
                take.getTakeId(),
                testId,
                testName,
                totalQuestions,
                selectedCount,
                status,
                take.getStartTime(),
                take.getEndTime(),
                remaining,
                selectedMap,
                partMap
        );
    }

    private Map<Integer, List<Integer>> buildPartMap(Integer testId) {
        // Lấy toàn bộ câu hỏi của test, đã sort theo questionNumber
        var all = questionRepo.findById_TestIdOrderById_QuestionNumber(testId);

        // group theo part -> list số câu
        Map<Integer, List<Integer>> grouped =
            all.stream()
            .collect(Collectors.groupingBy(
                Question::getPart,
                LinkedHashMap::new, // giữ thứ tự part nếu cần
                Collectors.mapping(
                    q -> q.getId().getQuestionNumber(),
                    Collectors.collectingAndThen(Collectors.toList(), list -> {
                        list.sort(Integer::compareTo); // chắc chắn tăng dần
                        return list;
                    })
                )
            ));

        // đảm bảo có đủ key 1..7 (nếu test bạn có 7 part). Có thể bỏ nếu không cần.
        for (int p = 1; p <= 7; p++) {
            grouped.computeIfAbsent(p, k -> new ArrayList<>());
        }
        return grouped;
    }

    @Transactional
    public Integer startOrResume(Integer userId, Integer testId) {

        // 1) Tìm take STARTED gần nhất của user cho test này
        Optional<Take> existingOpt = takeRepo
                .findFirstByUser_UserIdAndTest_TestIdAndStatusOrderByAttemptNoDesc(
                        userId, testId, "STARTED");

        Take take;

        if (existingOpt.isPresent()) {
            Take ex = existingOpt.get();

            // ⚡ THÊM ĐIỀU KIỆN QUAN TRỌNG: còn thời gian hay không?
            if (ex.getEndTime() != null && ex.getEndTime().isAfter(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))) {
                // Còn thời gian → Resume
                return ex.getTakeId();
            }
            // Hết thời gian → KHÔNG resume → tạo attempt mới
        }

        // ---- Nếu tới đây nghĩa là không có bài STARTED hoặc hết thời gian ----

        // 2) Tính attempt kế tiếp
        int nextAttempt = takeRepo
                .findTopByUser_UserIdAndTest_TestIdOrderByAttemptNoDesc(userId, testId)
                .map(t -> t.getAttemptNo() + 1)
                .orElse(1);

        // 3) Tạo take mới
        LocalDateTime start = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime end   = start.plusMinutes(120); // 120 phút TOEIC

        take = new Take();
        take.setUser(userRepo.getReferenceById(userId));
        take.setTest(testRepo.getReferenceById(testId));
        take.setAttemptNo(nextAttempt);
        take.setStatus("STARTED");
        take.setStartTime(start);
        take.setEndTime(end);

        take = takeRepo.save(take);

        return take.getTakeId();
    }

    @Transactional
public void saveAllAndGrade(Integer takeId, Map<Integer, Character> answers) {

    // 0) Lấy Take + Test
    Take take = takeRepo.findById(takeId).orElseThrow();
    Integer testId = take.getTest().getTestId();

    // 1) Lấy đáp án đúng từ answer_sheets
    Map<Integer, Character> correctMap = answerSheetRepo
            .findById_TestIdOrderById_Sequence(testId)
            .stream()
            .collect(Collectors.toMap(
                    a -> a.getId().getSequence(), // sequence = số câu
                    a -> a.getCorrectOption() != null && !a.getCorrectOption().isEmpty()
                            ? Character.toUpperCase(a.getCorrectOption().charAt(0))
                            : 'X'
            ));

    // 2) Lấy các user_answers đã có cho take này (nếu có)
    List<UserAnswer> existing = userAnswerRepo.findByTake_TakeId(takeId);
    Map<Integer, UserAnswer> existedByQn = existing.stream()
            .collect(Collectors.toMap(
                    UserAnswer::getQuestionNumber,
                    ua -> ua,
                    (a, b) -> a
            ));

    // 3) Xử lý từng câu user gửi lên: lưu selected_option + is_correct
    int correctCount = 0;
    List<UserAnswer> toSave = new ArrayList<>();

    for (Map.Entry<Integer, Character> e : answers.entrySet()) {
        int qn = e.getKey();
        char sel = Character.toUpperCase(e.getValue());

        // Đáp án đúng của câu đó
        Character key = correctMap.get(qn);
        boolean ok = (key != null && sel == key);

        UserAnswer ua = existedByQn.get(qn);
        if (ua == null) {
            // tạo mới
            ua = new UserAnswer(take, qn, sel, ok); // nhớ constructor khớp entity
        } else {
            // update bản ghi cũ
            ua.setSelectedOption(sel);
            ua.setIsCorrect(ok);
        }

        toSave.add(ua);
        if (ok) correctCount++;
    }

    if (!toSave.isEmpty()) {
        userAnswerRepo.saveAll(toSave);
    }

    // 4) Ghi vào test_results (mỗi take một result)
    TestResult result = testResultRepo.findByTake_TakeId(takeId)
            .orElseGet(() -> {
                TestResult r = new TestResult();
                r.setTake(take);
                return r;
            });

    result.setScore(correctCount);
    result.setTakenOn(LocalDateTime.now());
    testResultRepo.save(result);

    // 5) Cập nhật trạng thái Take
    take.setStatus("FINISHED");
    take.setEndTime(LocalDateTime.now());
    takeRepo.save(take);
}

    // @Transactional(readOnly = true)
    // public TakeReviewVM buildTakeViewWithResults(Integer takeId, int part) {
    //     // 1) Lấy take + test
    //     Take take = takeRepo.findById(takeId).orElseThrow();
    //     Integer testId = take.getTest().getTestId();
    //     String testName = take.getTest().getTestName(); // hoặc getName() tùy entity của bạn

    //     // 2) Lấy toàn bộ câu hỏi của test để build partMap + correctMap + total
    //     List<Question> questions = questionRepo.findById_TestIdOrderById_QuestionNumber(testId);

    //     int totalQuestions = questions.size();

    //     // partMap: Map<partNumber, List<questionNumber>>
    //     Map<Integer, List<Integer>> partMap = questions.stream()
    //             .collect(Collectors.groupingBy(
    //                     Question::getPart,
    //                     LinkedHashMap::new,
    //                     Collectors.mapping(q -> q.getId().getQuestionNumber(), Collectors.toList())
    //             ));

    //     // correctMap: Map<questionNumber, correctOptionChar>
    //     Map<Integer, Character> correctMap = questions.stream()
    //     .collect(Collectors.toMap(
    //             q -> q.getId().getQuestionNumber(),
    //             q -> {
    //                 String opt = q.getCorrectOption();      // String "A"/"B"/"C"/"D" hoặc null
    //                 if (opt == null || opt.isBlank()) return 'X'; // ký hiệu chưa có đáp án
    //                 return Character.toUpperCase(opt.trim().charAt(0));
    //             },
    //             (a, b) -> a,                  // nếu trùng questionNumber, giữ giá trị đầu
    //             LinkedHashMap::new            // giữ thứ tự theo stream (đã sort trước đó)
    //     ));

    //     // 3) Lấy đáp án người dùng đã chọn
    //     Map<Integer, Character> selectedMap = userAnswerRepo.findByTake_TakeId(takeId)
    //             .stream()
    //             .collect(Collectors.toMap(
    //                     UserAnswer::getQuestionNumber,
    //                     UserAnswer::getSelectedOption,
    //                     (oldV, newV) -> newV,          // nếu trùng key, lấy bản mới nhất
    //                     LinkedHashMap::new
    //             ));

    //     // 4) Tính đúng/sai
    //     Map<Integer, Boolean> isCorrectMap = new LinkedHashMap<>();
    //     for (Map.Entry<Integer, Character> e : correctMap.entrySet()) {
    //         int qn = e.getKey();
    //         Character sel = selectedMap.get(qn);
    //         boolean ok = (sel != null) && (Character.toUpperCase(sel) == e.getValue());
    //         isCorrectMap.put(qn, ok);
    //     }

    //     // 5) Score: ưu tiên dùng trong DB; nếu null thì tự tính
    //    Integer score = testResultRepo.findByTake_TakeId(takeId)
    //     .map(TestResult::getScore)  // Nếu có, lấy score
    //     .orElse(0);                 // Nếu không có, mặc định 0

    //     if (score == null) {
    //         score = (int) isCorrectMap.values().stream().filter(Boolean::booleanValue).count();
    //     }
    //     long remaining = 0;

    //     // 6) Trả về ViewModel cho view
    //     return new TakeReviewVM(
    //             takeId,
    //             testId,
    //             testName,
    //             totalQuestions,
    //             score,
    //             part,
    //             remaining,
    //             partMap,
    //             selectedMap,
    //             correctMap,
    //             isCorrectMap
    //     );
    // }
    @Transactional(readOnly = true)
public TakeReviewVM buildTakeViewWithResults(Integer takeId, int part) {
    // 1) Take + Test
    Take take = takeRepo.findById(takeId).orElseThrow();
    Integer testId = take.getTest().getTestId();
    String testName = take.getTest().getTestName();

    // 2) Lấy toàn bộ câu hỏi để build partMap, correctMap, total
    List<Question> questions =
            questionRepo.findById_TestIdOrderById_QuestionNumber(testId);
    int totalQuestions = questions.size();

    Map<Integer, List<Integer>> partMap = questions.stream()
            .collect(Collectors.groupingBy(
                    Question::getPart,
                    LinkedHashMap::new,
                    Collectors.mapping(q -> q.getId().getQuestionNumber(),
                                       Collectors.toList())
            ));
// Lấy toàn bộ dòng answer_sheets của test này
List<AnswerSheet> sheets = answerSheetRepo.findById_TestId(testId);

// Map<questionNumber/sequence, correctOptionChar>
Map<Integer, Character> correctMap = answerSheetRepo.findById_TestId(testId)
    .stream()
    .collect(Collectors.toMap(
        sheet -> sheet.getId().getSequence(), // 🔥 lấy sequence từ embedded key
        sheet -> {
            String opt = sheet.getCorrectOption();
            if (opt == null || opt.isBlank()) return 'X';
            return Character.toUpperCase(opt.trim().charAt(0));
        },
        (a, b) -> a,
        LinkedHashMap::new
    ));



    // 3) Lấy toàn bộ user_answers của take này
    List<UserAnswer> userAnswers = userAnswerRepo.findByTake_TakeId(takeId);

    // selectedMap: câu → user chọn gì
    Map<Integer, Character> selectedMap = userAnswers.stream()
            .collect(Collectors.toMap(
                    UserAnswer::getQuestionNumber,
                    UserAnswer::getSelectedOption,
                    (o, n) -> n,
                    LinkedHashMap::new
            ));

    // isCorrectMap: câu → true/false (lấy luôn từ DB, không tính lại)
    Map<Integer, Boolean> isCorrectMap = userAnswers.stream()
            .collect(Collectors.toMap(
                    UserAnswer::getQuestionNumber,
                    ua -> ua.getIsCorrect() != null && ua.getIsCorrect(),
                    (o, n) -> n,
                    LinkedHashMap::new
            ));

    // 4) Score: ưu tiên lấy từ test_results
    Integer score = testResultRepo.findByTake_TakeId(takeId)
            .map(TestResult::getScore)
            .orElse(null);

    if (score == null) {
        // fallback: đếm từ isCorrectMap
        score = (int) isCorrectMap.values().stream()
                .filter(Boolean::booleanValue)
                .count();
    }

    long remaining = 0; // review thì 0

    return new TakeReviewVM(
            takeId,
            testId,
            testName,
            totalQuestions,
            score,
            part,
            remaining,
            partMap,
            selectedMap,
            correctMap,
            isCorrectMap
    );
}
    public Map<Integer, Character> buildSelectedMap(Integer takeId) {
        List<UserAnswer> list = userAnswerRepo.findByTake_TakeId(takeId);

        Map<Integer, Character> selectedMap = new HashMap<>();
        for (UserAnswer ua : list) {
            selectedMap.put(ua.getQuestionNumber(), ua.getSelectedOption());
        }
        return selectedMap;
    }

    public Map<Integer, Boolean> buildCorrectFlagMap(Integer takeId) {
        List<UserAnswer> list = userAnswerRepo.findByTake_TakeId(takeId);

        Map<Integer, Boolean> correctFlagMap = new HashMap<>();
        for (UserAnswer ua : list) {
            correctFlagMap.put(ua.getQuestionNumber(), ua.getIsCorrect());
        }
        return correctFlagMap;
    }

    @Transactional(readOnly = true)
    public List<TakeHistoryVM> buildTakeHistory(Integer userId) {
        if (userId == null) {
            return List.of();
        }

        List<Take> takes = takeRepo.findByUser_UserIdAndStatusOrderByStartTimeDesc(userId, "FINISHED");

        return takes.stream()
                .map(take -> {
                    Integer score = testResultRepo.findByTake_TakeId(take.getTakeId())
                            .map(TestResult::getScore)
                            .orElse(null);

                    Test test = take.getTest();
                    String testName = (test != null && test.getTestName() != null)
                            ? test.getTestName()
                            : "Bài thi";
                    Integer testId = test != null ? test.getTestId() : null;

                    return new TakeHistoryVM(
                            take.getTakeId(),
                            testId,
                            testName,
                            take.getAttemptNo(),
                            take.getStatus(),
                            take.getStartTime(),
                            take.getEndTime(),
                            score
                    );
                })
                .collect(Collectors.toList());
    }

}
