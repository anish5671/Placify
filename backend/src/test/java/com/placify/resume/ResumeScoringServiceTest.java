package com.placify.resume;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResumeScoringServiceTest {
    private final ResumeScoringService service = new ResumeScoringService();

    @Test
    void scoreIsBoundedAndReportsMissingSections() {
        var result = service.score("""
                Contact: person@example.com
                Skills
                Java Spring Boot SQL
                Education
                University
                Projects
                Built an application.
                Experience
                Software developer
                Certifications
                Java certificate
                """.repeat(4));

        assertEquals(100, result.overallScore());
        assertTrue(result.missingSections().isEmpty());
    }

    @Test
    void shortResumeGetsLowScoreAndMissingSectionChecklist() {
        var result = service.score("Java developer");

        assertTrue(result.overallScore() < 30);
        assertTrue(result.missingSections().contains("projects"));
    }
}
