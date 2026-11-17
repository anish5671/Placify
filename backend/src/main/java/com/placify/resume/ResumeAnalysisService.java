package com.placify.resume;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.placify.security.UserPrincipal;
import com.placify.user.User;
import com.placify.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeAnalysisService {

    private final ResumeFileValidator fileValidator;
    private final ResumeTextExtractor textExtractor;
    private final ResumeScoringService scoringService;
    private final JobDescriptionMatcher jobDescriptionMatcher;
    private final GeminiAnalysisService geminiAnalysisService;
    private final ResumeAnalysisRepository repository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JsonNode analyze(MultipartFile file, String jobDescription, UserPrincipal principal) {
        fileValidator.validate(file);
        String text = textExtractor.extract(file, fileValidator.extension(file));
        Map<String, Object> score = scoringService.score(text);
        Map<String, Object> jobMatch = jobDescriptionMatcher.match(text, jobDescription);
        JsonNode ai = geminiAnalysisService.analyze(text, score, jobDescription);

        ObjectNode result = objectMapper.createObjectNode();
        if (!ai.isObject()) {
            throw new ResumeAnalysisException(org.springframework.http.HttpStatus.BAD_GATEWAY,
                    "GEMINI_INVALID_RESPONSE", "The resume analysis service returned an invalid response");
        }
        result.setAll((ObjectNode) ai);
        result.set("deterministicScore", objectMapper.valueToTree(score));
        result.set("jobMatch", jobMatch == null ? null : objectMapper.valueToTree(jobMatch));
        result.put("originalFilename", Optional.ofNullable(file.getOriginalFilename()).orElse("resume"));
        result.put("extractedTextPreview", text.substring(0, Math.min(text.length(), 1800)));

        persist(result, score, jobMatch, file.getOriginalFilename(), principal);
        log.info("Resume analysis completed for user {} without storing uploaded file contents", principal.getId());
        return result;
    }

    private void persist(ObjectNode result, Map<String, Object> score, Map<String, Object> jobMatch,
                         String filename, UserPrincipal principal) {
        try {
            User user = userRepository.findById(principal.getId())
                    .orElseThrow(() -> new IllegalStateException("Authenticated user no longer exists"));
            int overallScore = ((Number) score.get("overallScore")).intValue();
            ObjectNode storedResult = result.deepCopy();
            storedResult.remove("extractedTextPreview");
            ResumeAnalysis saved = repository.save(ResumeAnalysis.builder()
                    .user(user)
                    .originalFilename(Optional.ofNullable(filename).orElse("resume"))
                    .overallScore(overallScore)
                    .extractedSkillsJson(writeJson(result.path("skills")))
                    .jobMatchingJson(jobMatch == null ? null : writeJson(jobMatch))
                    .analysisJson(writeJson(storedResult))
                    .build());
            result.put("analysisId", saved.getId());
        } catch (RuntimeException ex) {
            log.error("Could not persist resume analysis for user {}", principal.getId(), ex);
            throw ex;
        }
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not serialize resume analysis", ex);
        }
    }

    public byte[] createReport(Long analysisId, UserPrincipal principal) {
        ResumeAnalysis analysis = repository.findByIdAndUserId(analysisId, principal.getId())
                .orElseThrow(() -> new com.placify.exception.ResourceNotFoundException("Resume analysis", "id", analysisId));
        return ResumeReportService.createPdf(analysis);
    }
}
