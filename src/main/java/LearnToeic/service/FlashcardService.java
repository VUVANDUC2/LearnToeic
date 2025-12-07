package LearnToeic.service;

import java.util.List;
import LearnToeic.entity.*;
import LearnToeic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


@Service
public class FlashcardService {
    @Autowired
    private FlashcardRepository flashcardRepository;
    @Autowired
    private UserRepository userRepository;

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

    public void updateFlashcardContentAndDescription(int flashcardId, String content, String description, int userId) {
        Flashcard flashcard = flashcardRepository.findById_UserId(flashcardId, userId)
            .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        flashcard.setContent(content);
        flashcard.setDescription(description);
        flashcardRepository.save(flashcard);
    }

    public void deleteByDeckName(Integer userId, String deckName) {
        flashcardRepository.deleteFlashcardByDeckName(userId, deckName);
    }


    public void deleteFlashcards(List<Integer> flashcardIds, Integer userId) {
        for (Integer flashcardId : flashcardIds) {
            Flashcard flashcard = flashcardRepository.findById_UserId(flashcardId, userId)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with ID: " + flashcardId));
            flashcardRepository.delete(flashcard);
        }
    }

    public void importFlashcardsFromCSV(String deckName, String csvData, Integer userId) {

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        List<Flashcard> flashcards = new ArrayList<>();

        String[] lines = csvData.split("\n");

        boolean isFirstLine = true;

        for (String line : lines) {

            if (line.isBlank()) continue;

            // Skip header
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            String[] parts = line.split(",", 2); // content + description

            String content = parts[0].trim();
            String description = parts.length > 1 ? parts[1].trim() : "";

            Flashcard card = new Flashcard();
            card.setDeck(deckName);
            card.setContent(content);
            card.setDescription(description);
            card.setUser(user);
            card.setFamiliarPoint(0);

            flashcards.add(card);
        }

        flashcardRepository.saveAll(flashcards);
    }

    
    public void createDeck(String deckName, Integer userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Flashcard card = new Flashcard();
        card.setDeck(deckName);
        card.setContent("Sample Content");
        card.setDescription("Sample Description");
        card.setUser(user);

        flashcardRepository.save(card);
    }

    public Flashcard createFlashcard(String deckName, String content, String description, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Flashcard card = new Flashcard();
        card.setContent(content);
        card.setDescription(description);
        card.setDeck(deckName);
        card.setUser(user);
        card.setFamiliarPoint(0);

        return flashcardRepository.save(card); // return saved entity (with auto-generated ID)
    }


}
