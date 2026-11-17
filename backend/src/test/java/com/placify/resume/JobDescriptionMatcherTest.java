package com.placify.resume;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JobDescriptionMatcherTest {

    private final JobDescriptionMatcher matcher = new JobDescriptionMatcher();

    @Test
    void returnsMatchingAndMissingSkillsWithoutInventingResumeSkills() {
        Map<String, Object> result = matcher.match("Built a Java Spring REST API with PostgreSQL", "Java Spring React AWS communication");

        assertEquals(40, result.get("matchPercentage"));
        assertTrue(((java.util.List<?>) result.get("matchingSkills")).contains("java"));
        assertTrue(((java.util.List<?>) result.get("missingSkills")).contains("react"));
    }

    @Test
    void omitsMatchWhenJobDescriptionIsBlank() {
        assertNull(matcher.match("Java", " "));
    }
}
