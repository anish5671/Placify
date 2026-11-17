package com.placify.resume;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class ResumeScoringService {
    private static final List<Section> SECTIONS = List.of(
            new Section("skills", 18, List.of("skills", "technical skills", "core competencies")),
            new Section("experience", 22, List.of("experience", "work experience", "employment history", "professional experience")),
            new Section("projects", 18, List.of("projects", "personal projects", "academic projects")),
            new Section("education", 14, List.of("education", "academic background")),
            new Section("contactInformation", 9, List.of("contact", "email", "phone")),
            new Section("certifications", 9, List.of("certifications", "certificates", "licenses"))
    );

    public Score score(String resumeText) {
        String lowerText = resumeText.toLowerCase(Locale.ROOT);
        Map<String, Integer> scores = new LinkedHashMap<>();
        int overall = 0;
        for (Section section : SECTIONS) {
            boolean present = section.markers().stream().anyMatch(lowerText::contains);
            int points = present ? section.weight() : 0;
            scores.put(section.name(), points);
            overall += points;
        }

        if (resumeText.length() < 500) {
            overall -= 10;
            scores.put("contentDepth", 0);
        } else {
            scores.put("contentDepth", 10);
            overall += 10;
        }

        List<String> missing = SECTIONS.stream()
                .filter(section -> !section.name().equals("contactInformation"))
                .filter(section -> section.markers().stream().noneMatch(lowerText::contains))
                .map(Section::name)
                .toList();
        return new Score(Math.max(0, Math.min(100, overall)), Map.copyOf(scores), missing);
    }

    public record Score(int overallScore, Map<String, Integer> sectionScores, List<String> missingSections) {}

    private record Section(String name, int weight, List<String> markers) {}
}
