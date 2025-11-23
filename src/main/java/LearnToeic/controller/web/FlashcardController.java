package LearnToeic.controller.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import LearnToeic.entity.Flashcard;
import LearnToeic.service.FlashcardService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;



@Controller
public class FlashcardController {
    private final FlashcardService flashcardService;
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping("/flashcards")
    public String FlashcardsPage(Model model) {
        Map <String, Integer> deckCounts = flashcardService.getDeckandCounts(3);
        List<String> deckNames = new ArrayList<>(deckCounts.keySet());
        List<Integer> counts = new ArrayList<>(deckCounts.values());
        model.addAttribute("deckNames", deckNames);
        model.addAttribute("counts", counts);
        return "/flashcard/flashcards";
    }

    @GetMapping("/flashcards/start")
    public String FlashcardsStartPage(@RequestParam String name, Model model) {
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDecksNameAndId(name, 3);
        
        List<Flashcard> newWords = newWord(flashcards, 10);
        List<Flashcard> reviewWords = reviewWord(flashcards, 30);
        model.addAttribute("name", name);
        model.addAttribute("newWords", newWords);
        model.addAttribute("newWordsCount", newWords.size());
        model.addAttribute("reviewWords", reviewWords);
        model.addAttribute("reviewWordsCount", reviewWords.size());
        return "/flashcard/start";
    }

    //update familiar point
    @PostMapping("/flashcards/update")
    public ResponseEntity<String> updateFamiliarPoint(
            @RequestParam int flashcardId,
            @RequestParam String difficulty
    ) {
        try {
            flashcardService.updateFamiliarPoint(flashcardId, difficulty, 3);
            return ResponseEntity.ok("Updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    //rename decks
    @PostMapping("/decks/rename")
    public String renameDeck(@RequestParam String oldName,
                            @RequestParam String newName) {
        flashcardService.updateDeckName(oldName, newName, 3);
        
        return "redirect:/flashcards";
    }
    //share decks
    @GetMapping("/decks/{name}/share")
    public void shareDeck(@PathVariable String name, HttpServletResponse response) throws IOException {
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDecksNameAndId(name, 3);

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8"); // important for non-ASCII
        String fileName = URLEncoder.encode(name.replaceAll("\\s+", "_"), StandardCharsets.UTF_8.toString()) + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Content,Description"); // header
            for (Flashcard fc : flashcards) {
                String content = fc.getContent().replace("\"", "\"\"");
                String description = fc.getDescription().replace("\"", "\"\"");
                writer.println("\"" + content + "\",\"" + description + "\"");
            }
        }
    }
    //delete decks
    @DeleteMapping("/decks/{id}")
    public ResponseEntity<String> deleteDeck(@PathVariable Long id) {
        try {
            deckRepository.deleteById(id);
            return ResponseEntity.ok("Deck deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: " + e.getMessage());
        }
    }


    // New word
    private List<Flashcard> newWord(List<Flashcard> flashcards, int numberOfWords) {

        return flashcards.stream()
                .filter(fc -> fc.getFamiliarPoint() == 0)   // only unfamiliar words
                .limit(numberOfWords)                       // take first N
                .toList();                                  // return as List
    }
    // Review word
    private List<Flashcard> reviewWord(List<Flashcard> flashcards, int numberOfWords) {

        return flashcards.stream()
                .filter(fc -> fc.getFamiliarPoint() != 0 && fc.getFamiliarPoint() != 100)   // only unfamiliar words
                .limit(numberOfWords)                       // take first N
                .toList();                                  // return as List
    }
}
