package com.placify.resume;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ResumeScoringService {

    private static final List<String> SECTIONS = List.of("education", "experience", "projects", "skills", "certifications", "achievements");

    public Map<String, Object> score(String text) {
        if (text == null || text.isBlank()) {
            return Map.of("overallScore", 0, "sectionScores", Map.of(),
                    "methodology", "Scores combine section presence, content depth, readable formatting, and ATS-safe headings. AI feedback explains each deduction.");
        }
        String lower = text.toLowerCase(Locale.ROOT);
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("skills", sectionScore(lower, "skills", 18));
        scores.put("education", sectionScore(lower, "education", 14));
        scores.put("projects", sectionScore(lower, "projects", 18));
        scores.put("workExperience", sectionScore(lower, "experience", 20));
        scores.put("formattingAndReadability", formattingScore(text));
        scores.put("atsCompatibility", atsScore(lower));
        int total = (int) Math.round(scores.values().stream().mapToInt(Integer::intValue).sum() * 100.0 / 100);
        return Map.of("overallScore", Math.min(100, total), "sectionScores", scores,
                "methodology", "Scores combine section presence, content depth, readable formatting, and ATS-safe headings. AI feedback explains each deduction.");
    }

    public List<String> missingSections(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        return SECTIONS.stream().filter(section -> !lower.contains(section)).toList();
    }

    private int sectionScore(String text, String section, int weight) {
        if (!text.contains(section)) return 0;
        return Math.min(weight, weight - 2 + Math.min(2, text.split(section, -1).length - 1));
    }

    private int formattingScore(String text) {
        int score = 12;
        if (text.length() < 500) score -= 4;
        if (text.split("\\R").length < 8) score -= 3;
        return Math.max(0, score);
    }

    private int atsScore(String text) {
        int score = 10;
        if (!text.contains("email") && !text.contains("@")) score -= 3;
        if (!text.contains("phone") && !text.matches("(?s).*\\b\\+?[0-9][0-9 ()-]{7,}.*")) score -= 2;
        if (text.contains("education")) score += 1;
        if (text.contains("experience")) score += 1;
        return Math.min(12, score);
    }
}
