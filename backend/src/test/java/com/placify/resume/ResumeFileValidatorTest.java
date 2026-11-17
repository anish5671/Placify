package com.placify.resume;

import com.placify.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ResumeFileValidatorTest {

    private final ResumeFileValidator validator = new ResumeFileValidator(1024);

    @Test
    void rejectsUnsupportedExtension() {
        var file = new MockMultipartFile("file", "resume.txt", "text/plain", "resume".getBytes());
        assertThrows(BadRequestException.class, () -> validator.validate(file));
    }

    @Test
    void rejectsOversizedFiles() {
        var file = new MockMultipartFile("file", "resume.pdf", "application/pdf", new byte[2048]);
        assertThrows(BadRequestException.class, () -> validator.validate(file));
    }
}
