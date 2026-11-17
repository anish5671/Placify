package com.placify.resume;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class ResumeJobMatcher {
    private static final List<RoleProfile> ROLE_PROFILES = List.of(
            new RoleProfile("Backend Developer", List.of("java", "spring boot", "rest api", "sql", "postgresql", "microservices", "node.js")),
            new RoleProfile("Frontend Developer", List.of("javascript", "typescript", "react", "angular", "html", "css", "vue")),
            new RoleProfile("Full Stack Developer", List.of("javascript", "react", "html", "css", "node.js", "java", "spring boot", "sql")),
            new RoleProfile("Data Analyst", List.of("sql", "python", "excel", "power bi", "tableau", "statistics")),
            new RoleProfile("Data Scientist", List.of("python", "machine learning", "pandas", "statistics", "sql", "tensorflow")),
            new RoleProfile("DevOps Engineer", List.of("linux", "docker", "kubernetes", "aws", "azure", "ci/cd", "terraform")),
            new RoleProfile("QA Automation Engineer", List.of("selenium", "playwright", "junit", "test automation", "java", "python", "api testing")),
            new RoleProfile("Mobile App Developer", List.of("kotlin", "android", "swift", "ios", "flutter", "react native"))
    );

    private static final Set<String> SKILL_CATALOG = ROLE_PROFILES.stream()
            .flatMap(role -> role.skills().stream())
            .collect(LinkedHashSet::new, Set::add, Set::addAll);

    public List<String> detectSkills(String text) {
        return SKILL_CATALOG.stream()
                .filter(skill -> containsTerm(text, skill))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public List<ResumeAnalysisResult.JobRoleMatch> predictRoles(List<String> detectedSkills) {
        Set<String> resumeSkills = lowercaseSet(detectedSkills);
        return ROLE_PROFILES.stream()
                .map(profile -> roleMatch(profile, resumeSkills))
                .filter(match -> match.matchingSkills().size() >= 2)
                .sorted((first, second) -> Integer.compare(second.matchPercentage(), first.matchPercentage()))
                .limit(3)
                .toList();
    }

    public ResumeAnalysisResult.JobDescriptionMatch matchJobDescription(String resumeText, String jobDescription) {
        if (jobDescription == null || jobDescription.isBlank()) {
            return null;
        }
        List<String> requiredSkills = SKILL_CATALOG.stream()
                .filter(skill -> containsTerm(jobDescription, skill))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        if (requiredSkills.isEmpty()) {
            return new ResumeAnalysisResult.JobDescriptionMatch(0, List.of(), List.of(),
                    List.of("The description did not contain skills from the supported matching catalog."));
        }
        List<String> matched = requiredSkills.stream()
                .filter(skill -> containsTerm(resumeText, skill))
                .toList();
        List<String> missing = requiredSkills.stream()
                .filter(skill -> !containsTerm(resumeText, skill))
                .toList();
        int percentage = (int) Math.round(matched.size() * 100.0 / requiredSkills.size());
        List<String> recommendations = missing.stream()
                .map(skill -> "If you have this experience, add a truthful, specific example of " + skill + " to your resume.")
                .toList();
        return new ResumeAnalysisResult.JobDescriptionMatch(percentage, matched, missing, recommendations);
    }

    private ResumeAnalysisResult.JobRoleMatch roleMatch(RoleProfile profile, Set<String> resumeSkills) {
        List<String> matched = profile.skills().stream()
                .filter(skill -> resumeSkills.contains(skill.toLowerCase(Locale.ROOT)))
                .toList();
        List<String> missing = profile.skills().stream()
                .filter(skill -> !resumeSkills.contains(skill.toLowerCase(Locale.ROOT)))
                .toList();
        int percentage = (int) Math.round(matched.size() * 100.0 / profile.skills().size());
        return new ResumeAnalysisResult.JobRoleMatch(profile.name(), percentage, matched, missing);
    }

    private Set<String> lowercaseSet(List<String> values) {
        Set<String> result = new LinkedHashSet<>();
        values.forEach(value -> result.add(value.toLowerCase(Locale.ROOT)));
        return result;
    }

    private boolean containsTerm(String text, String term) {
        return Pattern.compile("(?i)(?<![\\p{Alnum}])" + Pattern.quote(term) + "(?![\\p{Alnum}])")
                .matcher(text)
                .find();
    }

    private record RoleProfile(String name, List<String> skills) {}
}
