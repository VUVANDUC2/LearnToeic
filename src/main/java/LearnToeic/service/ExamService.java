package LearnToeic.service;

import java.time.Duration;
import java.time.Instant;
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

        // 4) Thời gian còn lại (mặc định 120 phút; nếu bạn có cột duration thì thay ở đây)
        int durationMinutes = 120;
        Integer remaining = computeRemaining(take.getStartTime(), durationMinutes);

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

    // ---- helper ----
    private Integer computeRemaining(Instant startTime, int durationMinutes) {

        Instant start = (startTime != null) ? startTime : Instant.now();
        Instant endExpected = start.plus(Duration.ofMinutes(durationMinutes));
        Duration remain = Duration.between(Instant.now(), endExpected);

        if (remain.isNegative()) {
            return 0;   // đã hết thời gian
        }

        // trả về số GIÂY còn lại
        return (int) remain.getSeconds();
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
        // 1) thử tìm take STARTED gần nhất của user cho test này
        Optional<Take> existing = takeRepo
                .findFirstByUser_UserIdAndTest_TestIdAndStatusOrderByAttemptNoDesc(
                        userId, testId, "STARTED");

        Take take;
        if (existing.isPresent()) {
            // resume
            take = existing.get();
        } else {
            // 2) tính attempt kế tiếp
            int nextAttempt = takeRepo
                    .findTopByUser_UserIdAndTest_TestIdOrderByAttemptNoDesc(userId, testId)
                    .map(t -> t.getAttemptNo() + 1)
                    .orElse(1);

            // 3) tạo take mới
            take = new Take();
            take.setUser(userRepo.getReferenceById(userId));
            take.setTest(testRepo.getReferenceById(testId));
            take.setAttemptNo(nextAttempt);
            take.setStatus("STARTED");
            take.setStartTime(Instant.now());
            take.setElapsedSeconds(0);

            take = takeRepo.save(take);
        }
        return take.getTakeId();
    }
    @Transactional
    public void saveAllAndGrade(Integer takeId, Map<Integer, Character> answers) {
        // Lấy proxy Take (không load DB)
        Take takeRef = takeRepo.getReferenceById(takeId);
        // 1) Lưu tất cả đáp án vào DB (xóa cũ hay upsert tùy bạn; ở đây upsert)
        List<UserAnswer> existing = userAnswerRepo.findByTake_TakeId(takeId);
        Map<Integer, UserAnswer> existedByQn = existing.stream()
                .collect(Collectors.toMap(UserAnswer::getQuestionNumber, ua -> ua, (a,b)->a));

        List<UserAnswer> toSave = new ArrayList<>();
        for (Map.Entry<Integer, Character> e : answers.entrySet()) {
            int qn = e.getKey();
            char sel = Character.toUpperCase(e.getValue());

            UserAnswer ua = existedByQn.get(qn);
            if (ua == null) {
                // ⬇⬇ dùng constructor khớp với entity của bạn
                ua = new UserAnswer(takeRef, qn, sel, false);
            } else {
                ua.setSelectedOption(sel);
                ua.setIsCorrect(false); // sẽ chấm lại bên dưới
            }
            toSave.add(ua);
        }
        if (!toSave.isEmpty()) userAnswerRepo.saveAll(toSave);

        // 2) Chấm điểm
        Take take = takeRepo.findById(takeId).orElseThrow();
        Integer testId = take.getTest().getTestId();

        Map<Integer, Character> correctMap = questionRepo
        .findById_TestIdOrderById_QuestionNumber(testId)
        .stream()
        .collect(Collectors.toMap(
                q -> q.getId().getQuestionNumber(),
                q -> q.getCorrectOption() != null && !q.getCorrectOption().isEmpty()
                        ? q.getCorrectOption().charAt(0)
                        : 'X' // hoặc bạn có thể dùng 'x' nếu muốn tránh null
        ));

        List<UserAnswer> all = userAnswerRepo.findByTake_TakeId(takeId);
        int correct = 0;
        for (UserAnswer ua : all) {
            Character key = correctMap.get(ua.getQuestionNumber());
            boolean ok = (key != null && ua.getSelectedOption() != null
                          && Character.toUpperCase(ua.getSelectedOption()) == Character.toUpperCase(key));
            ua.setIsCorrect(ok);
            if (ok) correct++;
        }
        userAnswerRepo.saveAll(all);

        // 5) Ghi vào test_results (mỗi take một result)
        TestResult result = testResultRepo.findByTake_TakeId(takeId)
            .orElseGet(() -> {
                TestResult r = new TestResult();
                r.setTake(take);     // gắn take lần đầu để insert
                return r;
            });

        result.setScore(correct);
        result.setTake(take);
        result.setTakenOn(Instant.now());
        testResultRepo.save(result);
    }
    @Transactional(readOnly = true)
    public TakeReviewVM buildTakeViewWithResults(Integer takeId, int part) {
        // 1) Lấy take + test
        Take take = takeRepo.findById(takeId).orElseThrow();
        Integer testId = take.getTest().getTestId();
        String testName = take.getTest().getTestName(); // hoặc getName() tùy entity của bạn

        // 2) Lấy toàn bộ câu hỏi của test để build partMap + correctMap + total
        List<Question> questions = questionRepo.findById_TestIdOrderById_QuestionNumber(testId);

        int totalQuestions = questions.size();

        // partMap: Map<partNumber, List<questionNumber>>
        Map<Integer, List<Integer>> partMap = questions.stream()
                .collect(Collectors.groupingBy(
                        Question::getPart,
                        LinkedHashMap::new,
                        Collectors.mapping(q -> q.getId().getQuestionNumber(), Collectors.toList())
                ));

        // correctMap: Map<questionNumber, correctOptionChar>
        Map<Integer, Character> correctMap = questions.stream()
        .collect(Collectors.toMap(
                q -> q.getId().getQuestionNumber(),
                q -> {
                    String opt = q.getCorrectOption();      // String "A"/"B"/"C"/"D" hoặc null
                    if (opt == null || opt.isBlank()) return 'X'; // ký hiệu chưa có đáp án
                    return Character.toUpperCase(opt.trim().charAt(0));
                },
                (a, b) -> a,                  // nếu trùng questionNumber, giữ giá trị đầu
                LinkedHashMap::new            // giữ thứ tự theo stream (đã sort trước đó)
        ));

        // 3) Lấy đáp án người dùng đã chọn
        Map<Integer, Character> selectedMap = userAnswerRepo.findByTake_TakeId(takeId)
                .stream()
                .collect(Collectors.toMap(
                        UserAnswer::getQuestionNumber,
                        UserAnswer::getSelectedOption,
                        (oldV, newV) -> newV,          // nếu trùng key, lấy bản mới nhất
                        LinkedHashMap::new
                ));

        // 4) Tính đúng/sai
        Map<Integer, Boolean> isCorrectMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Character> e : correctMap.entrySet()) {
            int qn = e.getKey();
            Character sel = selectedMap.get(qn);
            boolean ok = (sel != null) && (Character.toUpperCase(sel) == e.getValue());
            isCorrectMap.put(qn, ok);
        }

        // 5) Score: ưu tiên dùng trong DB; nếu null thì tự tính
       Integer score = testResultRepo.findByTake_TakeId(takeId)
        .map(TestResult::getScore)  // Nếu có, lấy score
        .orElse(0);                 // Nếu không có, mặc định 0

        if (score == null) {
            score = (int) isCorrectMap.values().stream().filter(Boolean::booleanValue).count();
        }

        // 6) Trả về ViewModel cho view
        return new TakeReviewVM(
                takeId,
                testId,
                testName,
                totalQuestions,
                score,
                part,           // currentPart để View biết đang ở Part nào
                partMap,
                selectedMap,
                correctMap,
                isCorrectMap
        );
    }
}
