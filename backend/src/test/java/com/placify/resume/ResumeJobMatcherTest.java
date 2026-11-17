package com.placify.resume;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResumeJobMatcherTest {
    private final ResumeJobMatcher matcher = new ResumeJobMatcher();

    @Test
    void detectsSkillsWithoutMatchingSubstringsInsideOtherWords() {
        var skills = matcher.detectSkills("Experienced in JavaScript, React, and PostgreSQL. Strong communication.");

        assertTrue(skills.contains("javascript"));
        assertTrue(skills.contains("react"));
        assertTrue(skills.contains("postgresql"));
        assertFalse(skills.contains("java"));
    }

    @Test
    void ranksRolesAndMatchesOnlySkillsPresentInJobDescription() {
        var skills = matcher.detectSkills("Java Spring Boot REST API PostgreSQL Docker");
        var roles = matcher.predictRoles(skills);
        var jdMatch = matcher.matchJobDescription(
                "Java Spring Boot PostgreSQL",
                "We need Java and Spring Boot with Kubernetes experience");

        assertEquals("Backend Developer", roles.get(0).role());
        assertEquals(67, jdMatch.matchPercentage());
        assertEquals(1, jdMatch.missingSkills().size());
        assertEquals("kubernetes", jdMatch.missingSkills().get(0));
    }
}
