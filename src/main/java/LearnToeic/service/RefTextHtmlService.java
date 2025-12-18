package LearnToeic.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;

@Service
public class RefTextHtmlService {

    private final Path uploadsRoot;

    public RefTextHtmlService(@Value("${uploads.base-path:uploads}") String uploadsBasePath) {
        this.uploadsRoot = Paths.get(uploadsBasePath).toAbsolutePath().normalize();
    }

    public Optional<String> loadHtml(String refPath) {
        Optional<Path> file = resolveUploadsFile(refPath);
        if (file.isEmpty()) {
            return Optional.empty();
        }
        Path path = file.get();
        if (!Files.isRegularFile(path)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Files.readString(path, StandardCharsets.UTF_8));
        } catch (IOException ignored) {
            return Optional.empty();
        }
    }

    private Optional<Path> resolveUploadsFile(String refPath) {
        if (refPath == null) {
            return Optional.empty();
        }
        String path = refPath.trim();
        if (path.isEmpty()) {
            return Optional.empty();
        }

        path = path.replace('\\', '/');
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return Optional.empty();
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.startsWith("uploads/")) {
            path = path.substring("uploads/".length());
        }
        if (path.isEmpty()) {
            return Optional.empty();
        }

        Path candidate = uploadsRoot.resolve(path).normalize();
        if (!candidate.startsWith(uploadsRoot)) {
            return Optional.empty();
        }

        String filename = candidate.getFileName() != null ? candidate.getFileName().toString() : "";
        String lower = filename.toLowerCase(Locale.ROOT);
        if (!(lower.endsWith(".txt") || lower.endsWith(".html") || lower.endsWith(".htm"))) {
            return Optional.empty();
        }

        return Optional.of(candidate);
    }
}

