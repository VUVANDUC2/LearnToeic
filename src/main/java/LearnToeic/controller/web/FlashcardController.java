package LearnToeic.controller.web;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import LearnToeic.entity.Flashcard;
import LearnToeic.service.FlashcardService;
import LearnToeic.service.UserQueryService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.net.URLEncoder;


@Controller
public class FlashcardController {
    private final FlashcardService flashcardService;
    private final UserQueryService userQueryService;
    private Integer userId = 3; //default userId for testing
    public FlashcardController(FlashcardService flashcardService, UserQueryService userQueryService) {
        this.flashcardService = flashcardService;
        this.userQueryService = userQueryService;
        
    }

    @GetMapping("/flashcards")
    public String FlashcardsPage(Model model) {
        
        this.userId = userQueryService.getCurrentUserId(
            SecurityContextHolder.getContext().getAuthentication()
        );
        
        Map <String, Integer> deckCounts = flashcardService.getDeckandCounts(userId);
        List<String> deckNames = new ArrayList<>(deckCounts.keySet());
        List<Integer> counts = new ArrayList<>(deckCounts.values());
        model.addAttribute("deckNames", deckNames);
        model.addAttribute("counts", counts);
        model.addAttribute("title", "");
        return "/flashcard/flashcards";
    }

    @GetMapping("/flashcards/start")
    public String FlashcardsStartPage(@RequestParam String name, Model model) {

        List<Flashcard> flashcards = flashcardService.getFlashcardsByDecksNameAndId(name, userId);
        
        List<Flashcard> newWords = newWord(flashcards, 10);
        List<Flashcard> reviewWords = reviewWord(flashcards, 30);
        model.addAttribute("name", name);
        model.addAttribute("allCards", flashcards);
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
            flashcardService.updateFamiliarPoint(flashcardId, difficulty, userId);
            return ResponseEntity.ok("Updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
    //Update content and description flashcard
    @PostMapping("/flashcards/edit")
    @ResponseBody
    public String updateFlashcard(@RequestParam int flashcardId,
                                  @RequestParam String content,
                                  @RequestParam String description) {
        flashcardService.updateFlashcardContentAndDescription(flashcardId, content, description, userId);
        return "OK";
    }

    @PostMapping("/flashcards/delete-selected")
    @ResponseBody
    public String deleteSelected(@RequestBody List<Integer> flashcardIds) {
        flashcardService.deleteFlashcards(flashcardIds, userId);
        return "OK";
    }

    //rename decks
    @PostMapping("/decks/rename")
    public String renameDeck(@RequestParam String oldName,
                            @RequestParam String newName) {
        flashcardService.updateDeckName(oldName, newName, userId);
        
        return "redirect:/flashcards";
    }
    //share decks
    @GetMapping("/decks/{name}/share")
    public void shareDeck(@PathVariable String name, HttpServletResponse response) throws IOException {
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDecksNameAndId(name, userId);

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

    @PostMapping("/decks/upload-csv")
    @ResponseBody
    public String uploadDeckFromCSV(
            @RequestParam String deckName,
            @RequestParam("file") MultipartFile file) {

        try {
            String csvData = new String(file.getBytes());
            flashcardService.importFlashcardsFromCSV(deckName, csvData, userId);
            return "OK";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/decks/create")
    @ResponseBody
    public String createDeck(@RequestBody Map<String, String> body) {
        String deckName = body.get("deckName");

        if (deckName == null || deckName.trim().isEmpty()) {
            return "Error: deckName is missing";
        }

        flashcardService.createDeck(deckName, userId);
        return "OK";
    }

    @PostMapping("/flashcards/create")
    @ResponseBody
    public Map<String, Object> createFlashcard(@RequestBody Map<String, String> body) {
        try {
            String content = body.get("content");
            String description = body.get("description");
            String deckName = body.get("name");

            Flashcard newCard = flashcardService.createFlashcard(deckName, content, description, userId);

            return Map.of(
                "status", "OK",
                "id", newCard.getFlashcardId(),
                "content", newCard.getContent(),
                "description", newCard.getDescription(),
                "deckName", newCard.getDeck()
            );

        } catch (Exception e) {
            return Map.of(
                "status", "Error",
                "message", e.getMessage()
            );
        }
    }


    //Delete decks
    @DeleteMapping("/decks/delete")
    @ResponseBody
    public String deleteDeck(@RequestParam String deckName) {
        flashcardService.deleteByDeckName(userId, deckName);
        return "OK"; 
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
