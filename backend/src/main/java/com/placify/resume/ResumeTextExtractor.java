package com.placify.resume;

import com.placify.exception.BadRequestException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Component
public class ResumeTextExtractor {

    public String extract(MultipartFile file, String extension) {
        try {
            String text = "pdf".equals(extension)
                    ? extractPdf(file.getBytes())
                    : extractDocx(file.getBytes());
            text = text == null ? "" : text.replace("\u0000", "").trim();
            if (text.isBlank()) {
                throw new BadRequestException("The resume has no readable text. Scanned-image PDFs are not supported yet");
            }
            return text;
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("The resume could not be read. Upload a valid, uncorrupted PDF or DOCX file");
        }
    }

    private String extractPdf(byte[] bytes) throws Exception {
        try (var document = Loader.loadPDF(bytes)) {
            return new PDFTextStripper().getText(document);
        }
    }

    private String extractDocx(byte[] bytes) throws Exception {
        try (var document = new XWPFDocument(new ByteArrayInputStream(bytes));
             var extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }
}
