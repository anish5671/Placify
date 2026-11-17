package com.placify.resume;

import com.placify.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Component
public class ResumeFileValidator {
    private final long maxFileSize;

    public ResumeFileValidator(
            @Value("${resume.max-file-size-bytes:10485760}") long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Choose a non-empty PDF or DOCX resume");
        }
        if (file.getSize() > maxFileSize) {
            throw new BadRequestException("The resume must be smaller than 10 MB");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            throw new BadRequestException("The uploaded resume must have a PDF or DOCX filename");
        }
        String lowercase = filename.toLowerCase(Locale.ROOT);
        if (lowercase.endsWith(".pdf")) {
            return "pdf";
        }
        if (lowercase.endsWith(".docx")) {
            return "docx";
        }
        throw new BadRequestException("Only PDF and DOCX resumes are supported");
    }
}
