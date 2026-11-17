package com.placify.resume;

import com.placify.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResumeFileValidatorTest {
    private final ResumeFileValidator validator = new ResumeFileValidator(1024);

    @Test
    void acceptsPdfAndDocxByExtension() {
        assertEquals("pdf", validator.validate(file("cv.PDF", new byte[]{1})));
        assertEquals("docx", validator.validate(file("cv.docx", new byte[]{1})));
    }

    @Test
    void rejectsUnsupportedEmptyAndOversizedFiles() {
        assertThrows(BadRequestException.class, () -> validator.validate(file("cv.txt", new byte[]{1})));
        assertThrows(BadRequestException.class, () -> validator.validate(file("cv.pdf", new byte[0])));
        assertThrows(BadRequestException.class, () -> validator.validate(file("cv.pdf", new byte[1025])));
    }

    private MockMultipartFile file(String name, byte[] bytes) {
        return new MockMultipartFile("file", name, "application/octet-stream", bytes);
    }
}
