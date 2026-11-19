package com.placify.resume;

import java.util.List;
import java.util.Map;

public record ResumeAnalysisResult(
        int overallScore,
        Map<String, Integer> sectionScores,
        List<String> detectedSkills,
        List<String> missingSections,
        List<JobRoleMatch> predictedRoles,
        JobDescriptionMatch jobDescriptionMatch,
        AiFeedback aiFeedback,
        String privacyNotice) {

    public record JobRoleMatch(
            String role,
            int matchPercentage,
            List<String> matchingSkills,
            List<String> missingSkills) {} 

    public record JobDescriptionMatch(
            int matchPercentage,
            List<String> matchingSkills,
            List<String> missingSkills,
            List<String> recommendations) {}

    public record AiFeedback(
            String status,
            String summary,
            List<String> strengths,
            List<String> improvementSuggestions,
            String message) {}
}
