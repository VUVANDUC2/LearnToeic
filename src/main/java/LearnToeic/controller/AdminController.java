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

import org.springframework.data.domain.Page;
import LearnToeic.model.*;
import LearnToeic.repository.*;
import LearnToeic.service.*;

import java.nio.file.Path;
import java.util.List;
@Controller
public class AdminController {
    @Value("${file.upload-dir}") 
    private String uploadDir;
    private final TestService testService;
    private final BlogService blogService;
    private final UserService userService;
    private final QuestionService questionService;
    public AdminController(TestService testService, BlogService blogService, UserService userService, QuestionService questionService) {
        this.testService = testService;
        this.blogService = blogService;
        this.userService = userService;
        this.questionService = questionService;
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
    
    @GetMapping("/admin/blogs")
    public String getBlogsPage(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(value = "searchTerm", required = false) String searchTerm
    ) {
        Page<Blog> blogPage = blogService.getPaginateBlogs(searchTerm, page, size);
        model.addAttribute("blogs", blogPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogPage.getTotalPages());
        model.addAttribute("title", "Admin - Blogs List");
        return "admin/blogs";
    }

    @GetMapping("/admin/users")
    public String usersPage(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "searchTerm", required = false) String searchTerm
        ){
        Page<LearnToeic.model.User> userPage = userService.getPaginateUsers(searchTerm, page, size);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        
        model.addAttribute("title", "Admin - Users List");
        return "admin/users";
    }

    

    @GetMapping("/admin/blogs/edit_blog")
    public String editBlogPage(@RequestParam int blogId ,Model model) {
        Blog blog = blogService.getBlogById(blogId);
        
        
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

    @GetMapping("/admin/create_blog")
    public String createBlogPage(Model model) {
        model.addAttribute("title", "Admin - Create Blog");
        return "admin/create_blog";
    }

    @GetMapping("/admin/create_test")
    public String createTestPage(Model model) {
        model.addAttribute("title", "Admin - Create Test");
        return "admin/create_test";
    }
    
    @PostMapping("/admin/upload")
    public String uploadTest(
        @RequestParam("testName") String testName,
        @RequestParam("status") String status,
        @RequestParam(value = "images", required = false) MultipartFile[] images,
        @RequestParam(value = "audios", required = false) MultipartFile[] audios,
        @RequestParam(value = "csv", required = false) MultipartFile csv
        ) throws IOException
    {
        System.out.println(status);
        Test savedTest = testService.saveTest(testName, status);
        String uniqueFolderName = String.valueOf(savedTest.getTestId());

        
        Path testDirPath = Paths.get(uploadDir, uniqueFolderName);

        Files.createDirectories(testDirPath);


        saveFiles(testDirPath, "images", images);
        saveFiles(testDirPath, "audios", audios);
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
            blogService.saveBlog(title, content);
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
            blogService.updateBlogContent(title, content, blogId);
            System.out.println("Success");
        } catch (Exception e) {
            
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update blog.");
        }
        return "redirect:/admin/blogs";
    }
    
    private void saveFiles(Path baseDir, String subDirName, MultipartFile[] files) throws IOException {
        if (files != null && files.length > 0) {
            Path subDirPath = baseDir.resolve(subDirName);
            Files.createDirectories(subDirPath); // Create "images" or "audios" folder
            
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    Path filePath = subDirPath.resolve(file.getOriginalFilename());
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

    
}
