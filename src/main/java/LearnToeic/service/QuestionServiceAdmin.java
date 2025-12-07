package LearnToeic.service;

import LearnToeic.model.QuestionAdmin;
import LearnToeic.repository.QuestionRepositoryAdmin;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import com.opencsv.CSVReader;

@Service
public class QuestionServiceAdmin {

    @Autowired
    private QuestionRepositoryAdmin questionRepository;

    public List<QuestionAdmin> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<QuestionAdmin> getQuestionById(Integer testId, Integer questionNumber) {
        return questionRepository.findById(new LearnToeic.model.QuestionId(testId, questionNumber));
    }

    public List<QuestionAdmin> getQuestionsById(Integer testId){
        return questionRepository.findByTestId(testId);
    }

    public QuestionAdmin saveQuestion(QuestionAdmin question) {
        return questionRepository.save(question);
    }
    
    public void deleteQuestion(Integer testId, Integer questionNumber) {
        questionRepository.deleteById(new LearnToeic.model.QuestionId(testId, questionNumber));
    }

    public List<QuestionAdmin> getQuestionsByTestId(Integer testId) {
        return questionRepository.findByTestId(testId);
    }

    @Transactional
    public void importQuestionsFromCsv(String testName, MultipartFile[] csvFiles) {
        for (MultipartFile file : csvFiles) {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) continue;

            try (Reader reader = new InputStreamReader(file.getInputStream());
                CSVReader csvReader = new CSVReader(reader)) {

                csvReader.readNext(); // skip header
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    QuestionAdmin q = new QuestionAdmin();
                    q.setTestId(17);
                    q.setQuestionNumber(Integer.parseInt(line[1]));
                    q.setPart(Integer.parseInt(line[2]));
                    q.setQuestionText(line[3]);
                    q.setOptionA(line[4]);
                    q.setOptionB(line[5]);
                    q.setOptionC(line[6]);
                    q.setOptionD(line[7]);
                    q.setCorrectOption(line[8]);
                    q.setNote(line[9]);
                    questionRepository.save(q);
                }

            } catch (IOException e) {
                System.err.println("❌ IO error reading CSV: " + file.getOriginalFilename());
                e.printStackTrace();
            } catch (com.opencsv.exceptions.CsvValidationException e) {
                System.err.println("⚠️ Invalid CSV format in: " + file.getOriginalFilename());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Number format issue in: " + file.getOriginalFilename());
            }
        }
    }

}