package LearnToeic.service;

import LearnToeic.entity.Question;
import LearnToeic.entity.QuestionId;
import LearnToeic.repository.QuestionRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class QuestionServiceAdmin {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Integer testId, Integer questionNumber) {
        return questionRepository.findById(new QuestionId(testId, questionNumber)).orElse(null);
    }

    public List<Question> getQuestionsByTestId(Integer testId){
        return questionRepository.findById_TestIdOrderById_QuestionNumber(testId);
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Integer testId, Integer questionNumber) {
        questionRepository.deleteById(new QuestionId(testId, questionNumber));
    }

    @Transactional
    public void importQuestionsFromCsv(Integer testId, MultipartFile[] csvFiles) {
        for (MultipartFile file : csvFiles) {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) continue;

            try (Reader reader = new InputStreamReader(file.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                csvReader.readNext(); // skip header
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    Question q = new Question();
                    q.setId(new QuestionId(testId, Integer.parseInt(line[0])));
                    q.setPart(Integer.parseInt(line[1]));
                    q.setQuestionText(line[2]);
                    q.setOptionA(line[3]);
                    q.setOptionB(line[4]);
                    q.setOptionC(line[5]);
                    q.setOptionD(line[6]);
                    questionRepository.save(q);
                }

            } catch (Exception e) {
                System.err.println("Error importing CSV: " + file.getOriginalFilename());
                e.printStackTrace();
            }
        }
    }
}
