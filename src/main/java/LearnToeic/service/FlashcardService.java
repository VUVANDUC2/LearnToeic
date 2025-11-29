package LearnToeic.service;

import java.util.List;
import LearnToeic.entity.*;
import LearnToeic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;


@Service
public class FlashcardService {
    @Autowired
    private FlashcardRepository flashcardRepository;

    public List<Flashcard> getAllFlashcards() {
        return flashcardRepository.findAll();
    }

    public List<Flashcard> getFlashcardsByDeck(Integer userId,String deckName)
    {
        return flashcardRepository.findByDeck(userId,deckName);
    }

    public Map<String, Integer> getDeckandCounts(Integer userId) {
        List<Object[]> deckData = flashcardRepository.findDeckNamesAndCountsByUserId(userId);
        Map<String, Integer> deckCounts = new HashMap<>();
        for (Object[] row : deckData) {
            String deckName = (String) row[0];
            Long count = (Long) row[1];
            deckCounts.put(deckName, count.intValue());
        }
        return deckCounts;
    }

    public List<Flashcard> getFlashcardsByDecksNameAndId(String deckName, Integer userId) {
        return flashcardRepository.findByDeckNameAndId(userId, deckName);
    }

    public void updateFamiliarPoint(int flashcardId, String difficulty, int userId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
            .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        int pointChange = switch (difficulty.toUpperCase()) {
            case "EASY" -> 20;
            case "NORMAL" -> 10;
            case "HARD" -> 5;
            default -> 0;
        };

        int newPoint = Math.min(100, Math.max(0, 
            (flashcard.getFamiliarPoint() != null ? flashcard.getFamiliarPoint() : 0) + pointChange));
        flashcard.setFamiliarPoint(newPoint);

        flashcardRepository.save(flashcard);
    }

    public void updateDeckName(String oldName, String newName, Integer userId) {
        List<Flashcard> flashcards = flashcardRepository.findByDeck(userId, oldName);
        for (Flashcard flashcard : flashcards) {
            flashcard.setDeck(newName);
        }
        flashcardRepository.saveAll(flashcards);
    }

}
