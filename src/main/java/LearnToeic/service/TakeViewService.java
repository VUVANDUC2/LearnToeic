package LearnToeic.service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import LearnToeic.dto.QuestionForTakeDTO;
import LearnToeic.dto.TestForTakeDTO;
import LearnToeic.dto.UserAnswerDTO;
import LearnToeic.entity.*;
import LearnToeic.repository.*;

@Service
@RequiredArgsConstructor
public class TakeViewService {

  private final TestRepository testRepo;
  private final TakeRepository takeRepo;
  private final QuestionRepository qRepo;
  private final UserAnswerRepository uaRepo;
  private final UserRepository userRepo;
  private final AnswerSheetRepository answerSheetRepo;

  /** Dùng cho Controller: không cần check owner (đã có endpoint kiểm soát) */
  @Transactional(readOnly = true)
  public TestForTakeDTO getView(Long takeId) {
    Take take = takeRepo.findById(takeId).orElseThrow();
    return buildView(take);
  }

  @Transactional(readOnly = true)
  public TestForTakeDTO loadView(Integer userId, Long takeId) {
    Take take = takeRepo.findByTakeIdAndUser_UserId(takeId, userId)
        .orElseThrow(() -> new RuntimeException("Take not found or not yours"));
    return buildView(take);
  }
  /** Tạo attempt mới hoặc tiếp tục attempt STARTED gần nhất */
  // @Transactional
  // public TestForTakeDTO startOrResume(Integer userId, Integer testId) {
  //   Test test = testRepo.findById(testId).orElseThrow();

  //   // Tìm attempt STARTED chưa nộp
  //   Optional<Take> existing = takeRepo.findFirstByUser_UserIdAndTest_TestIdAndStatusOrderByAttemptNoDesc(
  //       userId, testId, "STARTED");

  //   Take take;
  //   if (existing.isPresent()) {
  //     take = existing.get();
  //   } else {
  //     // Tạo attempt mới
  //     int nextAttempt = takeRepo
  //         .findTopByUser_UserIdAndTest_TestIdOrderByAttemptNoDesc(userId, testId)
  //         .map(t -> t.getAttemptNo() + 1).orElse(1);

  //     take = new Take();
  //     take.setUser(userRepo.getReferenceById(userId));
  //     take.setTest(test);
  //     take.setAttemptNo(nextAttempt);
  //     take.setStatus("STARTED");
  //     take.setStartTime(Instant.now());
  //     take = takeRepo.save(take);
  //   }

  //   return buildView(take);
  // }

  @Transactional
  public TestForTakeDTO startOrResume(Integer userId, Integer testId) {
      // 1️⃣ Lấy test; nếu không có thì throw
      Test test = testRepo.findById(testId)
          .orElseThrow(() -> new RuntimeException("Không tìm thấy bài test " + testId));

      // 2️⃣ Kiểm tra xem user có bài làm STARTED chưa
      Optional<Take> existing = takeRepo
          .findFirstByUser_UserIdAndTest_TestIdAndStatusOrderByAttemptNoDesc(userId, testId, "STARTED");

      Take take;
      if (existing.isPresent()) {
          // ✅ Resume bài đang làm
          take = existing.get();
      } else {
          // 3️⃣ Tạo attempt mới → tăng attempt_no dựa trên bản ghi gần nhất
          int nextAttempt = takeRepo
              .findTopByUser_UserIdAndTest_TestIdOrderByAttemptNoDesc(userId, testId)
              .map(t -> t.getAttemptNo() + 1)
              .orElse(1);

          // 4️⃣ Khởi tạo bản ghi mới
          take = new Take();
          take.setUser(userRepo.getReferenceById(userId));
          take.setTest(test);
          take.setAttemptNo(nextAttempt);
          take.setStatus("STARTED");
          take.setStartTime(Instant.now());
          take.setElapsedSeconds(0); // nếu DB có cột này
          take = takeRepo.save(take);
      }

      // 5️⃣ Trả về DTO hoặc xử lý tiếp
      return new TestForTakeDTO(take, test);
  }

  // ---- helpers ----

  private TestForTakeDTO buildView(Take take) {
    Integer testId = take.getTest().getTestId();

    // Lấy danh sách câu hỏi theo thứ tự
    List<Question> qs = qRepo.findById_TestIdOrderById_QuestionNumber(testId);

    // Map các câu đã chọn của user
    Map<Integer, Character> selectedMap = uaRepo.findById_TakeId(take.getTakeId())
        .stream()
        .collect(Collectors.toMap(
            a -> a.getId().getQuestionNumber(),
            UserAnswer::getSelectedOption
        ));

    // Build DTO; KHÔNG đưa correctOption
    List<QuestionForTakeDTO> qDtos = new ArrayList<>(qs.size());
    for (Question q : qs) {
      QuestionForTakeDTO d = new QuestionForTakeDTO();
      d.setQuestionNumber(q.getId().getQuestionNumber());
      d.setQuestionText(q.getQuestionText());
      d.setOptionA(q.getOptionA());
      d.setOptionB(q.getOptionB());
      d.setOptionC(q.getOptionC());
      d.setOptionD(q.getOptionD());
      d.setSelectedOption(selectedMap.get(d.getQuestionNumber())); // có thể null
      d.setPart(q.getPart());
      qDtos.add(d);
    }

    TestForTakeDTO dto = new TestForTakeDTO();
    dto.setTakeId(take.getTakeId());
    dto.setTestId(testId);
    dto.setTestName(take.getTest().getTestName());
    dto.setTotalQuestions(qDtos.size());
    dto.setStatus(take.getStatus());
    dto.setStartTime(take.getStartTime());
    dto.setEndTime(take.getEndTime());
    dto.setQuestions(qDtos);
    return dto;
  }
    @Transactional
    public int saveAllAnswersAndSubmit(Long takeId, List<UserAnswerDTO> clientAnswers) {
    Take take = takeRepo.findById(takeId).orElseThrow();

    // Chỉ cho SUBMIT khi đang STARTED (hoặc tùy nghiệp vụ của bạn)
    String st = Optional.ofNullable(take.getStatus()).orElse("STARTED");
    if (!"STARTED".equalsIgnoreCase(st)) {
        throw new IllegalStateException("Bài thi không ở trạng thái STARTED.");
    }

    Integer testId = take.getTest().getTestId();

    // Map đáp án client gửi lên: qn -> 'A'|'B'|'C'|'D'|null
    Map<Integer, Character> selectedByQn = new HashMap<>();
    if (clientAnswers != null) {
        for (UserAnswerDTO dto : clientAnswers) {
            if (dto == null) continue;
            Integer qn = dto.getQuestionNumber();
            if (qn == null) continue;

            Character v = dto.getSelectedOption();
            if (v != null) {
                char up = Character.toUpperCase(v);
                v = (up=='A'||up=='B'||up=='C'||up=='D') ? up : null;
            }
            // Cho phép null -> HashMap OK, nhưng toMap() thì không
            selectedByQn.put(qn, v);
        }
    }


    // Lấy list 200 câu từ đề thi
    List<Question> allQuestions = qRepo.findById_TestIdOrderById_QuestionNumber(testId);

    // Lấy đáp án đúng (answer_sheets) -> qn -> correct
    // giả sử AnswerSheet(sequence: 1..200) và correctOption (A..D)
    List<AnswerSheet> key = answerSheetRepo.findById_TestIdOrderById_Sequence(testId);

    // ---- correctByQn: sequence(=question_number) -> 'A'|'B'|'C'|'D'|null
    Map<Integer, Character> correctByQn = new LinkedHashMap<>();
    for (AnswerSheet a : key) {
        if (a == null || a.getId() == null) continue;
        int seq = a.getId().getSequence();

        Character c = null;
        String s = a.getCorrectOption();
        if (s != null && !s.isBlank()) {
            char up = Character.toUpperCase(s.charAt(0));
            if (up=='A'||up=='B'||up=='C'||up=='D') c = up;
        }
        // Cho phép null
        correctByQn.put(seq, c);
    }


    int correctCount = 0;

    for (Question q : allQuestions) {
        int qn = q.getId().getQuestionNumber();

        // Tạo khóa tổng hợp
        UserAnswerId id = new UserAnswerId(takeId, qn);

        // Upsert 1 dòng cho mỗi câu
        UserAnswer ua = uaRepo.findById(id).orElseGet(() -> {
            UserAnswer x = new UserAnswer();
            x.setId(id);
            x.setTake(take); // cần cho @MapsId
            return x;
        });

        // Lấy đáp án người dùng chọn (null nếu chưa chọn)
        Character selected = selectedByQn.getOrDefault(qn, null);

        // ⚠️ Nếu không chọn → gán ký tự đặc biệt để tránh lỗi NOT NULL
        if (selected == null) {
            selected = 'X'; // hoặc 'X' nếu bạn thích
        }
        ua.setSelectedOption(selected);

        // Lấy đáp án đúng
        Character correct = correctByQn.get(qn);

        // Đánh giá đúng sai (bỏ qua câu trống '_')
        boolean isCorrect = (selected != null && selected != 'X' && correct != null && selected.equals(correct));
        ua.setIsCorrect(isCorrect);  // *** BỎ TRỐNG = SAI ***

        if (isCorrect) correctCount++;

        uaRepo.save(ua); // đảm bảo sau submit, user_answers có ĐỦ 200 dòng

    }

    // Cập nhật trạng thái bài thi
    take.setStatus("SUBMITTED");
    take.setEndTime(Instant.now());
    takeRepo.save(take);

    return correctCount;
}


    @Transactional(readOnly = true)
    public String getTimeRemaining(Long takeId) {
    Take take = takeRepo.findById(takeId).orElseThrow();

    Integer durationMinutes = 120;

    Instant start = Optional.ofNullable(take.getStartTime()).orElse(Instant.now());
    Instant endExpected = start.plus(Duration.ofMinutes(durationMinutes));
    Duration remain = Duration.between(Instant.now(), endExpected);

    if (remain.isNegative()) remain = Duration.ZERO; // hết giờ thì trả 00:00:00

    long seconds = remain.getSeconds();
    long hh = seconds / 3600;
    long mm = (seconds % 3600) / 60;
    long ss = seconds % 60;
    return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    @Transactional
    public int submit(Long takeId) {
    Take take = takeRepo.findById(takeId).orElseThrow();

    // Nếu đã COMPLETED rồi, tuỳ nghiệp vụ: có thể return điểm cũ hoặc tính lại.
    if ("COMPLETED".equalsIgnoreCase(
            Optional.ofNullable(take.getStatus()).orElse(""))) {
        // Ở đây mình tính lại để đơn giản (giữ nguyên flow bên dưới).
    }

    Integer testId = take.getTest().getTestId();

    // 1) Lấy đáp án đúng từ answer_sheets theo thứ tự sequence (== question_number)
    List<AnswerSheet> sheets = answerSheetRepo.findById_TestIdOrderById_Sequence(testId);
    Map<Integer, Character> correctMap = new HashMap<>(sheets.size());
    for (AnswerSheet s : sheets) {
        String co = s.getCorrectOption();
        // TOEIC mỗi câu 1 đáp án A/B/C/D → lấy ký tự đầu tiên (trim + toUpperCase)
        Character c = (co != null && !co.isBlank())
                ? Character.toUpperCase(co.trim().charAt(0))
                : null;
        correctMap.put(s.getId().getSequence(), c); // sequence == question_number
    }

    // 2) Lấy đáp án người dùng theo takeId
    List<UserAnswer> answers = uaRepo.findById_TakeId(takeId);

    // 3) Tính điểm
    int score = 0;
    for (UserAnswer a : answers) {
        Integer qn = a.getId().getQuestionNumber();
        Character selected = a.getSelectedOption();
        Character correct = correctMap.get(qn);
        if (selected != null && correct != null && Character.toUpperCase(selected) == correct) {
            score++;
        }
    }

    // 4) Cập nhật trạng thái bài làm
    take.setEndTime(Instant.now());

    // nếu DB có cột elapsed_seconds
    if (take.getStartTime() != null) {
        long elapsed = Duration.between(take.getStartTime(), take.getEndTime()).getSeconds();
        try {
            take.setElapsedSeconds((int) Math.max(0, Math.min(elapsed, Integer.MAX_VALUE)));
        } catch (Throwable ignore) {}
    }

    // nếu DB có cột status
    try { take.setStatus("COMPLETED"); } catch (Throwable ignore) {}

    takeRepo.save(take);

    // (tuỳ chọn) nếu có cột score trong takes thì lưu luôn:
    // try { take.setScore(score); takeRepo.save(take); } catch (Throwable ignore) {}

    return score;
    }
    @Transactional(readOnly = true)
    public Map<Integer, Character> getSelectedMap(Long takeId) {
        return uaRepo.findById_TakeId(takeId).stream()
                .collect(Collectors.toMap(
                    a -> a.getId().getQuestionNumber(),
                    UserAnswer::getSelectedOption
                ));
    }

    @Transactional(readOnly = true)
    public Map<Integer, Character> getCorrectMap(Integer testId) {
        return answerSheetRepo.findById_TestIdOrderById_Sequence(testId).stream()
                .collect(Collectors.toMap(
                    a -> a.getId().getSequence(),
                    a -> {
                        String opt = a.getCorrectOption();
                        if (opt == null || opt.isBlank()) return null;
                        char c = Character.toUpperCase(opt.charAt(0));
                        return (c == 'A' || c == 'B' || c == 'C' || c == 'D') ? c : null;
                    }
                ));
    }

    public Map<Integer, Boolean> getIsCorrectMap(Map<Integer, Character> selectedMap,
                                                Map<Integer, Character> correctMap) {
        Map<Integer, Boolean> map = new HashMap<>();
        for (Integer qn : correctMap.keySet()) {
            Character s = selectedMap.get(qn);
            Character c = correctMap.get(qn);
            map.put(qn, (s != null && c != null) ? s.equals(c) : false);
        }
        return map;
    }

}
