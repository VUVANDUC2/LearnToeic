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
    private final RefRepository refRepo;
    private final RefTextHtmlService refTextHtmlService;

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
        enrichQuestionsWithRefs(testId, dtos, pv);
        return pv;
    }
    private String makeRangeLabel(List<QuestionForTakeDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return "";
        int first = dtos.get(0).getQuestionNumber();
        int last  = dtos.get(dtos.size()-1).getQuestionNumber();
        return "Q" + first + "–Q" + last;
    }

    private void enrichQuestionsWithRefs(Integer testId,
                                         List<QuestionForTakeDTO> questions,
                                         PartView pv) {
        if (pv == null) return;
        if (testId == null || questions == null || questions.isEmpty()) {
            pv.setAudioSources(List.of());
            return;
        }

        List<RefDTO> refs = refRepo.findByTest_TestIdOrderByStartAsc(testId)
                .stream()
                .map(RefDTO::fromEntity)
                .toList();
        if (refs.isEmpty()) {
            pv.setAudioSources(List.of());
            return;
        }

        QuestionRange range = resolveRange(questions);
        Map<Integer, List<String>> imageMap =
                buildQuestionResourceMap(refs, questions, RefType.IMAGE, range, false);
        Map<Integer, List<String>> audioMap =
                buildQuestionResourceMap(refs, questions, RefType.AUDIO, range, true);
        Map<Integer, List<String>> textMap =
                buildQuestionResourceMap(refs, questions, RefType.TEXT, range, true);
        Map<Integer, String> textTitleMap =
                buildTextTitleMap(refs, questions);

        Map<String, Optional<String>> textHtmlCache = new HashMap<>();

        for (QuestionForTakeDTO q : questions) {
            Integer qn = q.getQuestionNumber();
            q.setImageUrls(imageMap.getOrDefault(qn, List.of()));
            q.setAudioUrls(audioMap.getOrDefault(qn, List.of()));

            List<String> htmlBlocks = textMap.getOrDefault(qn, List.of())
                    .stream()
                    .map(path -> textHtmlCache.computeIfAbsent(path, refTextHtmlService::loadHtml))
                    .flatMap(Optional::stream)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            q.setTextHtmlBlocks(htmlBlocks);
            q.setTextHtmlTitle(textTitleMap.get(qn));
        }

        pv.setAudioSources(buildPartAudioSources(refs, range));
    }

    private Map<Integer, String> buildTextTitleMap(List<RefDTO> refs, List<QuestionForTakeDTO> questions) {
        Map<Integer, String> result = new HashMap<>();
        if (refs == null || refs.isEmpty() || questions == null || questions.isEmpty()) {
            return result;
        }

        for (RefDTO ref : refs) {
            if (ref.getRefType() != RefType.TEXT) continue;
            Integer start = findFirstQuestionInRange(ref, questions);
            Integer end = findLastQuestionInRange(ref, questions);
            if (start == null || end == null) continue;
            result.putIfAbsent(start, buildQuestionRangeTitle(start, end));
        }

        return result;
    }

    private Integer findLastQuestionInRange(RefDTO ref, List<QuestionForTakeDTO> questions) {
        if (ref == null || questions == null || questions.isEmpty()) {
            return null;
        }
        return questions.stream()
                .map(QuestionForTakeDTO::getQuestionNumber)
                .filter(Objects::nonNull)
                .filter(ref::appliesTo)
                .max(Integer::compareTo)
                .orElse(null);
    }

    private String buildQuestionRangeTitle(int start, int end) {
        if (start <= 0) {
            return null;
        }
        if (end <= 0 || end == start) {
            return "Câu hỏi " + start;
        }
        return "Câu hỏi " + start + "-" + end;
    }

    private QuestionRange resolveRange(List<QuestionForTakeDTO> questions) {
        if (questions == null || questions.isEmpty()) {
            return null;
        }
        Integer min = null;
        Integer max = null;
        for (QuestionForTakeDTO q : questions) {
            Integer qn = q.getQuestionNumber();
            if (qn == null) continue;
            if (min == null || qn < min) {
                min = qn;
            }
            if (max == null || qn > max) {
                max = qn;
            }
        }
        if (min == null || max == null) {
            return null;
        }
        return new QuestionRange(min, max);
    }

    private Map<Integer, List<String>> buildQuestionResourceMap(
            List<RefDTO> refs,
            List<QuestionForTakeDTO> questions,
            RefType refType,
            QuestionRange range,
            boolean attachAtRangeStartOnly) {

        Map<Integer, List<String>> result = new HashMap<>();
        if (refs == null || refs.isEmpty() || questions == null || questions.isEmpty()) {
            return result;
        }

        for (RefDTO ref : refs) {
            if (ref.getRefType() != refType) continue;
            if (refType == RefType.AUDIO && range != null && coversWholeRange(ref, range)) {
                continue; // sẽ render ở cấp Part
            }

            if (attachAtRangeStartOnly) {
                Integer targetQn = findFirstQuestionInRange(ref, questions);
                addResource(result, targetQn, normalizeRefPath(ref.getPath()));
            } else {
                for (QuestionForTakeDTO q : questions) {
                    Integer qn = q.getQuestionNumber();
                    if (qn != null && ref.appliesTo(qn)) {
                        addResource(result, qn, normalizeRefPath(ref.getPath()));
                    }
                }
            }
        }

        return result;
    }

    private void addResource(Map<Integer, List<String>> target, Integer qn, String path) {
        if (target == null || qn == null || path == null) {
            return;
        }
        target.computeIfAbsent(qn, k -> new ArrayList<>());
        List<String> list = target.get(qn);
        if (!list.contains(path)) {
            list.add(path);
        }
    }

    private Integer findFirstQuestionInRange(RefDTO ref, List<QuestionForTakeDTO> questions) {
        if (ref == null || questions == null || questions.isEmpty()) {
            return null;
        }
        return questions.stream()
                .map(QuestionForTakeDTO::getQuestionNumber)
                .filter(Objects::nonNull)
                .filter(ref::appliesTo)
                .min(Integer::compareTo)
                .orElse(null);
    }

    private List<String> buildPartAudioSources(List<RefDTO> refs, QuestionRange range) {
        if (refs == null || refs.isEmpty() || range == null) {
            return List.of();
        }
        return refs.stream()
                .filter(r -> r.getRefType() == RefType.AUDIO)
                .filter(r -> coversWholeRange(r, range))
                .map(RefDTO::getPath)
                .map(this::normalizeRefPath)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private boolean coversWholeRange(RefDTO ref, QuestionRange range) {
        if (ref == null || range == null) return false;
        int refStart = ref.getStartQ() != null ? ref.getStartQ() : Integer.MIN_VALUE;
        int refEnd = ref.getEndQ() != null ? ref.getEndQ() : Integer.MAX_VALUE;
        return refStart <= range.min && refEnd >= range.max;
    }

    private String normalizeRefPath(String rawPath) {
        if (rawPath == null) return null;
        String path = rawPath.trim();
        if (path.isEmpty()) return null;
        path = path.replace('\\', '/');
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        while (path.contains("//")) {
            path = path.replace("//", "/");
        }
        return path;
    }

    private static final class QuestionRange {
        final int min;
        final int max;
        QuestionRange(int min, int max) {
            this.min = min;
            this.max = max;
        }
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
        Integer maxScore = take.getTest().getMaxScore();

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
        }

        if (!toSave.isEmpty()) {
            userAnswerRepo.saveAll(toSave);
        }

        int correctCount = userAnswerRepo.countByTake_TakeIdAndIsCorrectTrue(takeId);
        int totalQuestions = Optional.ofNullable(take.getTest().getTotalQuestions())
                .orElseGet(() -> Optional.ofNullable(questionRepo.countById_TestId(testId))
                        .orElse(correctMap.size()));
        int score = computeScaledScore(correctCount, totalQuestions, maxScore);

        // 4) Ghi vào test_results (mỗi take một result)
        TestResult result = testResultRepo.findByTake_TakeId(takeId)
                .orElseGet(() -> {
                    TestResult r = new TestResult();
                    r.setTake(take);
                    return r;
                });

        result.setScore(score);
        result.setTakenOn(LocalDateTime.now());
        testResultRepo.save(result);

        // 5) Cập nhật trạng thái Take
        take.setStatus("FINISHED");
        take.setEndTime(LocalDateTime.now());
        takeRepo.save(take);
    }

    private int computeScaledScore(int correctCount, int totalQuestions, Integer maxScore) {
        if (totalQuestions <= 0) {
            return Math.max(0, correctCount);
        }

        int effectiveMax = (maxScore != null && maxScore > 0) ? maxScore : totalQuestions;
        int boundedCorrect = Math.min(Math.max(correctCount, 0), totalQuestions);
        int scaled = (int) Math.round(((double) boundedCorrect * effectiveMax) / totalQuestions);
        scaled = Math.min(Math.max(scaled, 0), effectiveMax);
        return scaled;
    }

    @Transactional(readOnly = true)
public TakeReviewVM buildTakeViewWithResults(Integer takeId, int part) {
    // 1) Take + Test
    Take take = takeRepo.findById(takeId).orElseThrow();
    Integer testId = take.getTest().getTestId();
    String testName = take.getTest().getTestName();
    Integer maxScore = take.getTest().getMaxScore();

    // 2) Lấy toàn bộ câu hỏi để build partMap, correctMap, total
    List<Question> questions =
            questionRepo.findById_TestIdOrderById_QuestionNumber(testId);
    int totalQuestions = Optional.ofNullable(take.getTest().getTotalQuestions())
            .orElse(questions.size());

    Map<Integer, List<Integer>> partMap = questions.stream()
            .collect(Collectors.groupingBy(
                    Question::getPart,
                    LinkedHashMap::new,
                    Collectors.mapping(q -> q.getId().getQuestionNumber(),
                                       Collectors.toList())
            ));
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

    int correctCount = (int) userAnswers.stream()
            .filter(ua -> Boolean.TRUE.equals(ua.getIsCorrect()))
            .count();
    int wrongCount = Math.max(0, totalQuestions - correctCount);

    if (score == null) {
        score = computeScaledScore(correctCount, totalQuestions, maxScore);
    }

    long remaining = 0; // review thì 0

    return new TakeReviewVM(
            takeId,
            testId,
                testName,
                totalQuestions,
                correctCount,
                wrongCount,
                score,
                maxScore,
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
