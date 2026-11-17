package com.placify.resume;

import com.placify.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

@Component
public class ResumeFileValidator {

    private static final Set<String> EXTENSIONS = Set.of("pdf", "docx");
    private static final Set<String> MIME_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/zip",
            "",
            "application/octet-stream"
    );

    private final long maxFileSize;

    public ResumeFileValidator(@Value("${resume.max-file-size-bytes:10485760}") long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Resume file is required");
        }
        if (file.getSize() > maxFileSize) {
            throw new BadRequestException("Resume file must be smaller than " + (maxFileSize / 1024 / 1024) + " MB");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.contains(".")) {
            throw new BadRequestException("Only PDF and DOCX resumes are supported");
        }
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        if (!EXTENSIONS.contains(extension) || !MIME_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Only valid PDF and DOCX resumes are supported");
        }
        try {
            byte[] header = file.getBytes();
            boolean validHeader = "pdf".equals(extension)
                    ? new String(header, 0, Math.min(header.length, 5), java.nio.charset.StandardCharsets.US_ASCII).startsWith("%PDF-")
                    : header.length >= 4 && header[0] == 'P' && header[1] == 'K';
            if (!validHeader) {
                throw new BadRequestException("The uploaded file is not a valid " + extension.toUpperCase(Locale.ROOT) + " document");
            }
        } catch (IOException ex) {
            throw new BadRequestException("Could not read the uploaded resume");
        }
    }

    public String extension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
