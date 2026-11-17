package com.placify.resume;

import com.placify.exception.BadRequestException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResumeTextExtractorTest {
    private final ResumeTextExtractor extractor = new ResumeTextExtractor();

    @Test
    void extractsTextFromPdf() throws Exception {
        byte[] bytes;
        try (var document = new PDDocument(); var output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (var content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(25, 700);
                content.showText("Java engineer with Spring experience");
                content.endText();
            }
            document.save(output);
            bytes = output.toByteArray();
        }

        String text = extractor.extract(file("resume.pdf", bytes), "pdf");

        assertTrue(text.contains("Java engineer"));
    }

    @Test
    void extractsTextFromDocx() throws Exception {
        byte[] bytes;
        try (var document = new XWPFDocument(); var output = new ByteArrayOutputStream()) {
            document.createParagraph().createRun().setText("Java engineer with Spring experience");
            document.write(output);
            bytes = output.toByteArray();
        }

        String text = extractor.extract(file("resume.docx", bytes), "docx");

        assertTrue(text.contains("Java engineer"));
    }

    @Test
    void rejectsUnreadableScannedDocument() {
        var file = file("resume.pdf", new byte[]{1, 2, 3});

        assertThrows(BadRequestException.class, () -> extractor.extract(file, "pdf"));
    }

    private MockMultipartFile file(String name, byte[] bytes) {
        return new MockMultipartFile("file", name, "application/octet-stream", bytes);
    }
}
