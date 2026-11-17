package com.placify.resume;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResumeScoringServiceTest {

    private final ResumeScoringService service = new ResumeScoringService();

    @Test
    void scoresPresentSectionsAndReportsMissingSections() {
        Map<String, Object> result = service.score("Jane Doe\nSkills\nJava Spring\nEducation\nB.Tech\nProjects\nAPI");

        assertTrue((Integer) result.get("overallScore") > 0);
        assertTrue(service.missingSections("Skills\nEducation").contains("experience"));
        assertFalse(service.missingSections("Skills\nEducation").contains("skills"));
    }

    @Test
    void handlesEmptyResumeWithoutThrowing() {
        assertEquals(0, service.score("").get("overallScore"));
    }
}
