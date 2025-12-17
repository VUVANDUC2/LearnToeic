package LearnToeic.controller;

import LearnToeic.entity.*;
import LearnToeic.service.TestServiceAdmin;
import LearnToeic.service.QuestionServiceAdmin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private static final String UPLOAD_ROOT = System.getProperty("user.dir") + "/uploads/toeic";
    @Value("${uploads.base-path:uploads}")
    private String uploadsBasePath;
    private final TestServiceAdmin testService;
    private final QuestionServiceAdmin questionService;

    public AdminRestController(TestServiceAdmin testService, QuestionServiceAdmin questionService) {
        this.testService = testService;
        this.questionService = questionService;
    }

    @PutMapping("/tests/{id}/status")
    public ResponseEntity<Void> updateTestStatus(@PathVariable("id") Integer testId) {
        try {
            testService.updateTestStatus(testId);
            return ResponseEntity.ok().build(); // HTTP 200 OK
        } catch (Exception e) {
            // Log the error
            return ResponseEntity.badRequest().build(); // HTTP 400 Bad Request or 500 Internal Server Error
        }
    }

    @PutMapping("/tests/{testId}/questions/{questionNumber}")
    public Question updateQuestion(
            @PathVariable Integer testId,
            @PathVariable Integer questionNumber,
            @RequestBody Question dto
    ) {
        return questionService.updateQuestion(testId, questionNumber, dto);
    }


    @GetMapping("/tests/{testId}")
    public ResponseEntity<?> getTestDetails(@PathVariable int testId) {
        Test test = testService.getTestById(testId);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }

        List<Question> questions = questionService.getQuestionsByTestId(testId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("test", test);
        response.put("questions", questions);
        System.out.println("ROOT PATH = " + System.getProperty("user.dir"));

        response.put("images", getImagesForTest(testId));
        response.put("audios", getAudiosForTest(testId));
        return ResponseEntity.ok(response);
    }

    @PutMapping(value="/update/tests/{testId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTestDetail(@PathVariable int testId, @RequestPart("general") String generalJSON){
       try{
        testService.updateGeneralInfo(testId, generalJSON);
        return ResponseEntity.ok().build();
       } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Failed to update test: " + e.getMessage());
        }
    }


    private Map<String, Object> getImagesForTest(int testId) {
        String root = System.getProperty("user.dir");

        Path readingPath = Paths.get(root, "uploads", "toeic", String.valueOf(testId), "reading");
        Path listeningPath = Paths.get(root, "uploads", "toeic", String.valueOf(testId), "listening");

        Map<String, Object> images = new HashMap<>();
        images.put("reading", listImages(readingPath));
        images.put("listening", listImages(listeningPath));

        return images;
    }

    private Map<String, Object> getAudiosForTest(int testId) {
        String root = System.getProperty("user.dir");

        Path listeningPath = Paths.get(root, "uploads", "toeic", String.valueOf(testId), "listening");

        Map<String, Object> audios = new HashMap<>();
        audios.put("listening", listAudios(listeningPath));

        return audios;
    }

    // Helper method to list only image files
    private List<String> listImages(Path dir) {
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return Collections.emptyList();
        }

        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(f -> f.toLowerCase().matches(".*\\.(png|jpg|jpeg|gif)$")) // only images
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<String> listAudios(Path dir){
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return Collections.emptyList();
        }

        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(f -> f.toLowerCase().matches(".*\\.(mp3|wav|ogg)$")) // only audio files
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @PostMapping("/delete-image")
    public ResponseEntity<?> deleteImage(@RequestBody Map<String, String> payload) {
        String url = payload.get("url"); // full URL sent from frontend
        if (url == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No URL provided"));
        }

        try {
            // Extract path after /uploads/
            String path = url.substring(url.indexOf("/uploads/") + "/uploads/".length());
            Path filePath = Paths.get(uploadsBasePath, path).toAbsolutePath().normalize();

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return ResponseEntity.ok(Map.of("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "File not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error deleting file"));
        }
    }
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder,
            @RequestParam("testId") int testId,
            @RequestParam(name = "existingFile", required = false) String existingFile
    ) {
        System.out.println("🟦 Upload request received");
        System.out.println("   TestId: " + testId);
        System.out.println("   Folder: " + folder);
        System.out.println("   Uploaded file: " + file.getOriginalFilename());
        System.out.println("   Existing file: " + existingFile);

        if (file.isEmpty()) {
            System.out.println("❌ No file selected");
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No file selected"));
        }

        try {
            Path folderPath = Paths.get(UPLOAD_ROOT, String.valueOf(testId), folder);
            Files.createDirectories(folderPath);
            System.out.println("🟦 Folder ensured at: " + folderPath);

            Path targetPath = folderPath.resolve(file.getOriginalFilename());
            System.out.println("🟦 Target file path: " + targetPath);

            // Replace existing file if any
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ File saved successfully");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "filename", file.getOriginalFilename(),
                    "path", targetPath.toString()
            ));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

}
