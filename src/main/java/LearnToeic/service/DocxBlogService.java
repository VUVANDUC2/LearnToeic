package LearnToeic.service;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DocxBlogService {

    private static final Pattern DATA_IMG_PATTERN = Pattern.compile("^data:image/([a-zA-Z0-9+]+);base64,(.*)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public DocxResult convertDocx(MultipartFile file, Path uploadDir, String publicBaseUri) throws IOException, Docx4JException {
        Files.createDirectories(uploadDir);

        Path tempDocx = Files.createTempFile("blog-docx-", ".docx");
        file.transferTo(tempDocx);

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(Files.newInputStream(tempDocx));
        HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
        htmlSettings.setWmlPackage(wordMLPackage);
        htmlSettings.setImageDirPath(uploadDir.toString());
        htmlSettings.setImageTargetUri(publicBaseUri + "/");

        String rawHtml;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Docx4J.toHTML(htmlSettings, out, Docx4J.FLAG_EXPORT_PREFER_XSL);
            rawHtml = out.toString(StandardCharsets.UTF_8);
        } finally {
            Files.deleteIfExists(tempDocx);
        }

        List<Path> savedImages = new ArrayList<>();
        Document document = Jsoup.parse(rawHtml);
        normalizeHeadings(document);
        int counter = 1;
        for (Element img : document.select("img")) {
            String src = img.attr("src");
            Matcher matcher = DATA_IMG_PATTERN.matcher(src);
            if (matcher.matches()) {
                String mime = matcher.group(1).toLowerCase();
                String base64Data = matcher.group(2);
                byte[] data = Base64.getDecoder().decode(base64Data);
                String ext = mapExt(mime);
                String fileName = "image_" + counter + "." + ext;
                Path target = uploadDir.resolve(fileName);
                Files.write(target, data);
                img.attr("src", publicBaseUri + "/" + fileName);
                savedImages.add(target);
                counter++;
            } else if (src.startsWith(publicBaseUri)) {
                String relative = src.substring(publicBaseUri.length());
                relative = relative.startsWith("/") ? relative.substring(1) : relative;
                Path target = uploadDir.resolve(relative);
                if (Files.exists(target)) {
                    savedImages.add(target);
                }
            }
        }

        String sanitized = sanitizeHtml(document.body().html());
        String excerpt = buildExcerpt(sanitized);

        String thumbnail = null;
        if (!savedImages.isEmpty()) {
            Path thumbTarget = uploadDir.resolve("thumb.jpg");
            Files.copy(savedImages.get(0), thumbTarget, StandardCopyOption.REPLACE_EXISTING);
            thumbnail = publicBaseUri + "/thumb.jpg";
        }

        return new DocxResult(sanitized, excerpt, thumbnail, LocalDateTime.now());
    }

    private String sanitizeHtml(String html) {
        Safelist safelist = Safelist.relaxed()
                .addTags("table", "thead", "tbody", "tr", "th", "td", "blockquote", "pre",
                        "h1", "h2", "h3", "h4", "h5", "h6")
                .addAttributes(":all", "style", "class", "id", "width", "height", "border", "cellspacing", "cellpadding", "align")
                .addAttributes("img", "src", "alt", "title");
        Document clean = Jsoup.parse(Jsoup.clean(html, safelist));
        return clean.body().html();
    }

    private String buildExcerpt(String sanitizedHtml) {
        String text = Jsoup.parse(sanitizedHtml).text();
        if (text.length() <= 180) {
            return text;
        }
        return text.substring(0, 180);
    }

    private String mapExt(String mime) {
        return switch (mime) {
            case "jpeg", "jpg" -> "jpg";
            case "gif" -> "gif";
            case "bmp" -> "bmp";
            case "webp" -> "webp";
            default -> "png";
        };
    }

    public record DocxResult(String html, String excerpt, String thumbnail, LocalDateTime createdAt) {
    }

    private void normalizeHeadings(Document doc) {
        // Xử lý các phần tử theo thứ tự từ cha đến con
        for (Element el : doc.select("h1, h2, h3, h4, h5, h6, p, div, span")) {
            String level = detectHeadingLevel(el);
            if (level != null && !el.tagName().equalsIgnoreCase(level)) {
                // Chỉ chuyển đổi nếu phần tử có nội dung text
                if (!el.text().trim().isEmpty()) {
                    el.tagName(level);
                    // Xóa các thẻ h2 trống bên trong nếu có
                    el.children().removeIf(child ->
                        child.tagName().matches("h[1-6]") && child.text().trim().isEmpty()
                    );
                }
            }
        }

        // Xóa các thẻ heading trống sau khi xử lý
        doc.select("h1, h2, h3, h4, h5, h6").removeIf(h -> h.text().trim().isEmpty());
    }

    private String detectHeadingLevel(Element el) {
        String tag = el.tagName().toLowerCase();

        // Nếu đã là heading, giữ nguyên
        if (tag.matches("h[1-6]")) {
            return tag;
        }

        // Kiểm tra text content trước
        String text = el.text().trim();
        if (text.isEmpty()) {
            return null;
        }

        // Kiểm tra style và class
        String style = el.attr("style").toLowerCase();
        String cls = el.className().toLowerCase();

        // Ưu tiên kiểm tra class name có chứa "heading" hoặc pattern h-number
        Matcher m = Pattern.compile("heading[\\s-_]?([1-6])|h[\\s-_]?([1-6])").matcher(cls);
        if (m.find()) {
            String level = m.group(1) != null ? m.group(1) : m.group(2);
            return "h" + level;
        }

        // Kiểm tra ARIA attributes
        String role = el.attr("role").toLowerCase();
        String aria = el.attr("aria-level");
        if ("heading".equals(role) && aria.matches("[1-6]")) {
            return "h" + aria;
        }

        // Kiểm tra font-weight trong style
        boolean isBold = style.contains("font-weight:bold") ||
                        style.contains("font-weight: bold") ||
                        style.matches(".*font-weight:\\s*[6-9]\\d{2}.*") || // 600-900
                        hasBoldChild(el);

        // Kiểm tra font-size để xác định level
        if (isBold) {
            // Kiểm tra font-size
            Matcher fontSizeMatcher = Pattern.compile("font-size:\\s*(\\d+(?:\\.\\d+)?)pt").matcher(style);
            if (fontSizeMatcher.find()) {
                double fontSize = Double.parseDouble(fontSizeMatcher.group(1));
                if (fontSize >= 16) {
                    return "h1";
                } else if (fontSize >= 14) {
                    return "h2";
                } else if (fontSize >= 12) {
                    return "h3";
                }
            }

            // Nếu không có font-size rõ ràng, mặc định là h2
            return "h2";
        }

        // Kiểm tra pattern số (1., 1.1., etc)
        if (text.matches("^\\d+(\\.\\d+)*\\.?\\s+.+")) {
            return "h2";
        }

        return null;
    }

    private boolean hasBoldChild(Element el) {
        // Kiểm tra chính element
        String style = el.attr("style").toLowerCase();
        if (style.contains("font-weight:bold") || style.contains("font-weight: bold") ||
            style.matches(".*font-weight:\\s*[6-9]\\d{2}.*")) {
            return true;
        }

        // Kiểm tra các thẻ con trực tiếp
        for (Element child : el.children()) {
            String childTag = child.tagName().toLowerCase();
            String childStyle = child.attr("style").toLowerCase();

            if ("strong".equals(childTag) || "b".equals(childTag)) {
                return true;
            }

            if (childStyle.contains("font-weight:bold") || childStyle.contains("font-weight: bold") ||
                childStyle.matches(".*font-weight:\\s*[6-9]\\d{2}.*")) {
                return true;
            }
        }

        // Kiểm tra đệ quy cho các span lồng nhau
        Element current = el;
        int depth = 0;
        while (current != null && current.children().size() == 1 && depth < 5) {
            Element child = current.child(0);
            String childTag = child.tagName().toLowerCase();
            String childStyle = child.attr("style").toLowerCase();

            if ("strong".equalsIgnoreCase(childTag) || "b".equalsIgnoreCase(childTag)) {
                return true;
            }

            if (childStyle.contains("font-weight:bold") || childStyle.contains("font-weight: bold") ||
                childStyle.matches(".*font-weight:\\s*[6-9]\\d{2}.*")) {
                return true;
            }

            current = child;
            depth++;
        }

        return false;
    }
}