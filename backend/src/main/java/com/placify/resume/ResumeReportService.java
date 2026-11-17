package com.placify.resume;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public final class ResumeReportService {

    private ResumeReportService() {
    }

    public static byte[] createPdf(ResumeAnalysis analysis) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 42, 42, 42, 42);
            PdfWriter.getInstance(document, output);
            document.open();
            JsonNode result = new ObjectMapper().readTree(analysis.getAnalysisJson());
            document.add(new Paragraph("Resume Analysis Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            document.add(new Paragraph(analysis.getOriginalFilename() + " | Overall score: " + analysis.getOverallScore() + "/100"));
            addSection(document, "Section scores", result.path("deterministicScore").path("sectionScores"));
            addSection(document, "Skills", result.path("skills"));
            addList(document, "Strengths", result.path("strengths"));
            addList(document, "Weaknesses", result.path("weaknesses"));
            addList(document, "Improvement suggestions", result.path("improvementSuggestions"));
            addList(document, "Experience suggestions", result.path("experienceSuggestions"));
            addSection(document, "ATS analysis", result.path("atsAnalysis"));
            addSection(document, "Job matching", result.path("jobMatch"));
            document.close();
            return output.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("Could not generate resume report", ex);
        }
    }

    private static void addSection(Document document, String title, JsonNode node) throws DocumentException {
        if (node == null || node.isMissingNode() || node.isNull()) return;
        document.add(new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));
        for (Map.Entry<String, JsonNode> field : node.properties()) {
            document.add(new Paragraph(field.getKey() + ": " + field.getValue().toString()));
        }
    }

    private static void addList(Document document, String title, JsonNode node) throws DocumentException {
        if (node == null || !node.isArray() || node.isEmpty()) return;
        document.add(new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));
        for (JsonNode item : node) document.add(new Paragraph("- " + item.asText()));
    }
}
