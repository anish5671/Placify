package com.placify.resume;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class JobDescriptionMatcher {

    private static final List<String> KNOWN_TERMS = List.of(
            "java", "python", "javascript", "typescript", "react", "angular", "spring", "spring boot",
            "node.js", "sql", "postgresql", "mysql", "mongodb", "docker", "kubernetes", "aws", "azure",
            "git", "rest", "graphql", "microservices", "redis", "kafka", "junit", "linux", "c++", "machine learning",
            "communication", "leadership", "problem solving", "teamwork"
    );

    public Map<String, Object> match(String resumeText, String jobDescription) {
        if (jobDescription == null || jobDescription.isBlank()) {
            return null;
        }
        String lowerResume = resumeText.toLowerCase(Locale.ROOT);
        String lowerJob = jobDescription.toLowerCase(Locale.ROOT);
        List<String> required = KNOWN_TERMS.stream()
                .filter(term -> containsTerm(lowerJob, term))
                .toList();
        List<String> matching = required.stream().filter(term -> containsTerm(lowerResume, term)).toList();
        List<String> missing = required.stream().filter(term -> !containsTerm(lowerResume, term)).toList();
        int score = required.isEmpty() ? 0 : (int) Math.round(matching.size() * 100.0 / required.size());
        return Map.of(
                "matchPercentage", score,
                "matchingSkills", matching,
                "missingSkills", missing,
                "partiallyMatchingSkills", List.of(),
                "recommendations", missing.stream()
                        .map(skill -> "If you genuinely have " + skill + " experience, make it visible with a specific example.")
                        .collect(Collectors.toList())
        );
    }

    private boolean containsTerm(String text, String term) {
        return Pattern.compile("(?<![a-z0-9])" + Pattern.quote(term) + "(?![a-z0-9])").matcher(text).find();
    }
}
