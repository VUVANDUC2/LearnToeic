package LearnToeic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.springframework.data.domain.Page;
import LearnToeic.entity.*;
import LearnToeic.repository.*;
import LearnToeic.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream; // Correct import for NIO/Collection stream operations
@Controller
public class AdminController {


    private static final int[] IMAGE_SEQUENCE = {
    1,2,3,4,5,6,62,65,68,92,95,98,131,135,139,143,
    147,149,151,153,155,158,161,165,168,172,176,
    181,186,191,196
    };


    private static final List<String> IMAGE_EXT = List.of("jpg", "jpeg", "png");
    @Value("${file.upload-dir}") 
    private String uploadDir;
    private final TestServiceAdmin testService;
    private final BlogServiceAdmin blogService;
    private final UserServiceAdmin userService;
    private final QuestionServiceAdmin questionService;
    private final TakeServiceAdmin takeService;
    private static final String UPLOAD_DIR = "src/main/resources/static/data/";
    public static record ResourceItem(String title, String path, String type, long fileCount) {}
    public AdminController(TestServiceAdmin testService, BlogServiceAdmin blogService, UserServiceAdmin userService, QuestionServiceAdmin questionService, TakeServiceAdmin takeService) {
        this.testService = testService;
        this.blogService = blogService;
        this.userService = userService;
        this.questionService = questionService;
        this.takeService = takeService;
    }
    //Page routings
    @GetMapping("/admin")
    public String dashboardPage(Model model) {
        model.addAttribute("title", "Admin - Dashboard");
        return "admin/dashboard";
    }
    
    //Test List
    @GetMapping("/admin/tests")
    public String adminMainPage(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(value = "searchTerm", required = false) String searchTerm
    ) {
        Page<Test> testPage = testService.getPaginateTests(searchTerm, page, size);
        System.out.println(searchTerm);
        model.addAttribute("tests", testPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", testPage.getTotalPages());
        model.addAttribute("title","Admin - Tests List");
        return "admin/index";
    }
    //Test Preview Page
    @GetMapping("/admin/tests/test_page")
    public String testPage(@RequestParam int testId,Model model) {
        List <Question> list = questionService.getQuestionsByTestId(testId);
        model.addAttribute("questions", list);
        model.addAttribute("title", "Admin - Test Page");
        return "admin/test_page";
    }
    // Blog List Page
    @GetMapping("/admin/blogs")
    public String getBlogsPage(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(value = "searchTerm", required = false) String searchTerm
    ) {
        Page<BlogPost> blogPage = blogService.getPaginateBlogPosts(searchTerm, page, size);
        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("title", "Admin - Blogs List");
        return "admin/blogs";
    }
    // User List Page
    @GetMapping("/admin/users")
    public String usersPage(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "searchTerm", required = false) String searchTerm
        ){
        Page<LearnToeic.entity.User> userPage = userService.getPaginateUsers(searchTerm, page, size);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        
        model.addAttribute("title", "Admin - Users List");
        return "admin/users";
    }

    
    // Edit Blog
    @GetMapping("/admin/blogs/edit_blog")
    public String editBlogPage(@RequestParam int blogId ,Model model) {
        BlogPost blog = blogService.getBlogPostById(Long.valueOf(blogId));
        
        
        String content = blog.getContent();
        content = content.replace("\n", "<br/>");
        content = content.replaceAll(
            "(?i)\\[Ảnh:\\s*([\\s\\S]*?)\\s*\\]",  // [\\s\\S]*? để match tất cả ký tự, bao gồm xuống dòng
            "<img src='/data/blogs/" + blogId + "/$1' alt='$1' "
            + "style='max-width:100%; border-radius:8px; margin:10px 0;'/>"
        );


        model.addAttribute("content", content);
        model.addAttribute("blog", blog);
        model.addAttribute("title", "Admin - Edit Blog");
        return "admin/edit_blog";
    }
    // Create Blog
    @GetMapping("/admin/create_blog")
    public String createBlogPage(Model model) {
        model.addAttribute("title", "Admin - Create Blog");
        return "admin/create_blog";
    }
    // Create Test
    @GetMapping("/admin/create_test")
    public String createTestPage(Model model) {
        model.addAttribute("title", "Admin - Create Test");
        return "admin/create_test";
    }
    
    // @GetMapping("/admin/ranking")
    // public String rankingPage(Model model){
    //     Map<Integer, Double> avgScore = takeService.getAverageScorePerTest();
    //     Map<Integer, Long> testTaker = takeService.getTop5TestTakers();
    //     Map<Integer, Long> mostDoTest = takeService.getTop10MostTakenTests();
    //     Map<Integer, Long> testDoByMonth = takeService.getTestsTakenByMonth();
    //     // AVG of test score
    //     List<Integer> label1 = new ArrayList<>(avgScore.keySet());
    //     List<Double> data1 = new ArrayList<>(avgScore.values());
    //     //Number of user that take the test
    //     int data2a = takeService.getDistinctUserCount();
    //     int data2b = userService.getDistinctUserCount();
    //     List <Integer> data2 = new ArrayList<Integer>();
    //     data2.add(data2a);
    //     data2.add(data2b);
    //     //Top 5 test takers
    //     List<Integer> data3a = new ArrayList<>(testTaker.keySet());
    //     List<String> label3 = new ArrayList<>();

    //     for (int i : data3a) {
    //         User user = userService.findUserById(i)
    //                             .orElseThrow(() -> new RuntimeException("User not found: " + i));
            
    //         label3.add(user.getFullName());
    //     }

    //     for(int i: data3a){
    //         System.out.println(i);
    //     }

    //     for(String i: label3){
    //         System.out.println(i);
    //     }

        
        
    //     List<Long> data3= new ArrayList<>(testTaker.values());

    //     //Most do test
    //     List<Integer> label4 = new ArrayList<>(mostDoTest.keySet());
    //     List<Long> data4 = new ArrayList<>(mostDoTest.values());
        

    //     //Test taken by months
    //     List<Integer> label5 = new ArrayList<>(testDoByMonth.keySet());

    //     List<Long> data5 = new ArrayList<>(testDoByMonth.values());

    //     model.addAttribute("chart1_label", label1);
    //     model.addAttribute("chart1_data", data1);

    //     model.addAttribute("chart2_data",data2);

    //     model.addAttribute("chart3_label", label3);
    //     model.addAttribute("chart3_data", data3);

    //     model.addAttribute("chart4_label", label4);
    //     model.addAttribute("chart4_data", data4);

    //     model.addAttribute("chart5_label", label5);
    //     model.addAttribute("chart5_data", data5);

    //     model.addAttribute("title","Admin -  User ranking");
    //     return "admin/ranking";
    // }



    
    @GetMapping("admin/upload_resource")
    public String uploadResource(Model model){
        model.addAttribute("title", "Uploading new Resources");
        return "admin/upload_resource";
    }


    @PostMapping("/admin/upload/resouces")
    public String uploadResouces(
        @RequestParam("resourceName") String resourceName, // Matches input 'name="testName"'
        @RequestParam("files") MultipartFile[] files, // Matches input 'name="files"'
        RedirectAttributes redirectAttributes // Optional: for showing success/error messages
    ) {
        // --- 1. Validate Input ---
        if (resourceName.isBlank() || files.length == 0 || files[0].isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resource name and at least one file are required.");
            return "redirect:/admin/upload";
        }

        try {
            // --- 2. Construct and Create Target Directory ---
            // The full path will be: UPLOAD_DIR + resourceName
            Path targetDirPath = Paths.get(UPLOAD_DIR, resourceName);
            
            // Create the directory (and any necessary parent directories)
            Files.createDirectories(targetDirPath);
            
            int filesSaved = 0;
            
            // --- 3. Process and Save Files ---
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // Skip empty files
                }
                
                // Get the original filename (safety check for null/empty name)
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isBlank()) {
                    continue; 
                }
                
                // Construct the final path for the file
                Path finalPath = targetDirPath.resolve(originalFilename);
                
                // Save the file using standard Java NIO utility
                Files.copy(file.getInputStream(), finalPath);
                
                filesSaved++;
            }

            // --- 4. Return Success ---
            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully uploaded " + filesSaved + " files to resource folder: " + resourceName);
            
            return "redirect:/admin/resources"; 

        } catch (IOException e) {
            // Handle file saving errors (e.g., directory creation failure, disk full)
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload files due to: " + e.getMessage());
            return "redirect:/admin/upload_resource"; 
        }
    }

    @PostMapping("/admin/upload")
    public String uploadTest(
            @RequestParam("testName") String testName,
            @RequestParam("status") String status,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "audios", required = false) MultipartFile[] audios,
            @RequestParam(value = "csv", required = false) MultipartFile csv
    ) throws IOException {

        Test savedTest = testService.saveTest(testName, status);
        String folderName = String.valueOf(savedTest.getTestId());

        Path testDirPath = Paths.get(uploadDir, folderName);
        Files.createDirectories(testDirPath);

        // Save images
        saveImages(testDirPath, images);

        // Save audios to Listening
        saveAudios(testDirPath, audios);

        // Save CSV
        saveSingleFile(testDirPath, csv);

        return "redirect:/admin";
    }



    @PostMapping("/admin/create/blog")
    public String createBlog(
        @RequestParam("blogTitle") String title,
        @RequestParam("content") String content,
        RedirectAttributes redirectAttributes
    ) 
    {
        
        try{
            blogService.saveBlogPost(title, content);
            System.out.println("Blog created successfully");
        } catch (Exception e) {
            
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create blog.");
        }
        return "redirect:/admin/blogs";
    }

    @PostMapping("/admin/upload/blog")
    public String uploadBlog(
        @RequestParam("blogId") int blogId,
        @RequestParam("blogTitle") String title,
        @RequestParam("content") String content,
        RedirectAttributes redirectAttributes) 
    {
        try {
            blogService.updateBlogPostContent(title, content, Long.valueOf(blogId));
            System.out.println("Success");
        } catch (Exception e) {
            
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update blog.");
        }
        return "redirect:/admin/blogs";
    }
    
    private void saveFiles(Path baseDir, String subDirName, MultipartFile[] files) throws IOException {
        if (files != null && files.length > 0) {

            Path subDirPath = baseDir.resolve(subDirName);
            Files.createDirectories(subDirPath);

            int index = 0;

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {

                    String originalName = file.getOriginalFilename();
                    String extension = "";

                    int dotIndex = originalName.lastIndexOf(".");
                    if (dotIndex != -1) {
                        extension = originalName.substring(dotIndex);
                    }

                    String newFilename;

                    // Apply renaming ONLY for images
                    if ("images".equals(subDirName)) {
                        newFilename = IMAGE_SEQUENCE[index] + extension;
                        index++;

                        if (index >= IMAGE_SEQUENCE.length) break;
                    } else {
                        // Audios keep original filename
                        newFilename = originalName;
                    }

                    Path filePath = subDirPath.resolve(newFilename);
                    file.transferTo(filePath);
                }
            }
        }
    }




    private void saveSingleFile(Path baseDir, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Path filePath = baseDir.resolve(file.getOriginalFilename());
            file.transferTo(filePath);
        }
    }

    private void saveImages(Path baseDir, MultipartFile[] images) throws IOException {
        if (images == null || images.length == 0) return;

        Path listeningPath = baseDir.resolve("Listening");
        Path readingPath = baseDir.resolve("Reading");

        Files.createDirectories(listeningPath);
        Files.createDirectories(readingPath);

        int index = 0;

        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            String extension = "";

            int dot = originalName.lastIndexOf(".");
            if (dot != -1) extension = originalName.substring(dot);

            int newNumber = IMAGE_SEQUENCE[index];

            Path target;

            if (newNumber < 100) {
                // 📌 Listening folder
                target = listeningPath.resolve(newNumber + extension);
            } else {
                // 📌 Reading folder
                target = readingPath.resolve(newNumber + extension);
            }

            file.transferTo(target);

            index++;
            if (index >= IMAGE_SEQUENCE.length) break;
        }
    }

    private void saveAudios(Path baseDir, MultipartFile[] audios) throws IOException {
        if (audios == null || audios.length == 0) return;

        Path listeningPath = baseDir.resolve("Listening");
        Files.createDirectories(listeningPath);

        for (MultipartFile file : audios) {
            if (file.isEmpty()) continue;

            Path target = listeningPath.resolve(file.getOriginalFilename());
            file.transferTo(target);
        }
    }

}


