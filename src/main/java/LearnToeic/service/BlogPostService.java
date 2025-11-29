package LearnToeic.service;

import LearnToeic.entity.BlogPost;
import LearnToeic.entity.BlogProgram;
import LearnToeic.repository.BlogPostRepository;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final DocxBlogService docxBlogService;

    @Value("${uploads.base-path:uploads}")
    private String uploadsBasePath;

    public BlogPostService(BlogPostRepository blogPostRepository, DocxBlogService docxBlogService) {
        this.blogPostRepository = blogPostRepository;
        this.docxBlogService = docxBlogService;
    }

    @Transactional
    public BlogPost createFromDocx(MultipartFile file, String title, String category, BlogProgram program,
                                   String manualExcerpt, MultipartFile manualThumbnail) throws IOException, Docx4JException {
        String slug = generateUniqueSlug(slugify(title));

        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());

        Path uploadDir = Paths.get(uploadsBasePath, "blog", year, month, slug).toAbsolutePath().normalize();
        String publicUri = "/uploads/blog/" + year + "/" + month + "/" + slug;

        DocxBlogService.DocxResult result = docxBlogService.convertDocx(file, uploadDir, publicUri);

        BlogPost post = new BlogPost();
        post.setTitle(title);
        post.setSlug(slug);
        post.setCategory(category);
        post.setProgram(program);
        post.setContent(result.html());
        String excerpt = (manualExcerpt != null && !manualExcerpt.isBlank()) ? trimExcerpt(manualExcerpt) : result.excerpt();
        post.setExcerpt(excerpt);
        String thumbnail = result.thumbnail();
        if (manualThumbnail != null && !manualThumbnail.isEmpty()) {
            thumbnail = saveThumbnail(manualThumbnail, uploadDir, publicUri);
        }
        post.setThumbnail(thumbnail);
        post.setCreatedAt(result.createdAt());

        return blogPostRepository.save(post);
    }

    @Transactional
    public BlogPost createManual(String title, String category, BlogProgram program, String excerpt, MultipartFile thumbnailFile) throws IOException {
        String slug = generateUniqueSlug(slugify(title));

        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());

        Path uploadDir = Paths.get(uploadsBasePath, "blog", year, month, slug).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
        String publicUri = "/uploads/blog/" + year + "/" + month + "/" + slug;

        String thumbnail = saveThumbnail(thumbnailFile, uploadDir, publicUri);

        String safeExcerpt = trimExcerpt(excerpt);

        BlogPost post = new BlogPost();
        post.setTitle(title);
        post.setSlug(slug);
        post.setCategory(category);
        post.setProgram(program);
        post.setContent(safeExcerpt.isEmpty() ? "" : safeExcerpt);
        post.setExcerpt(safeExcerpt);
        post.setThumbnail(thumbnail);
        post.setCreatedAt(now);
        return blogPostRepository.save(post);
    }

    public Optional<BlogPost> findBySlug(String slug) {
        return blogPostRepository.findBySlug(slug);
    }

    public Page<BlogPost> listPage(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 6 : size;
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return blogPostRepository.findAll(pageable);
    }

    public List<BlogPost> listAll() {
        return blogPostRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional
    public void deleteById(Long id) {
        blogPostRepository.deleteById(id);
    }

    private String generateUniqueSlug(String base) {
        String candidate = base;
        int counter = 1;
        while (blogPostRepository.existsBySlug(candidate)) {
            candidate = base + "-" + counter;
            counter++;
        }
        return candidate;
    }

    private String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .toLowerCase()
                .trim();
        if (normalized.isEmpty()) {
            return "post";
        }
        return normalized;
    }

    private String trimExcerpt(String excerpt) {
        String safe = excerpt == null ? "" : excerpt.strip();
        if (safe.length() > 180) {
            safe = safe.substring(0, 180);
        }
        return safe;
    }

    private String saveThumbnail(MultipartFile file, Path uploadDir, String publicUri) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Files.createDirectories(uploadDir);
        String ext = resolveExt(file.getOriginalFilename());
        Path thumbTarget = uploadDir.resolve("thumb." + ext);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, thumbTarget, StandardCopyOption.REPLACE_EXISTING);
        }
        return publicUri + "/thumb." + ext;
    }

    private String resolveExt(String original) {
        if (original == null) {
            return "jpg";
        }
        int dot = original.lastIndexOf('.');
        if (dot == -1 || dot == original.length() - 1) {
            return "jpg";
        }
        return original.substring(dot + 1).toLowerCase();
    }
}
