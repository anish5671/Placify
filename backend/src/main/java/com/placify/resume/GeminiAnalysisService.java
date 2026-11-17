package com.placify.resume;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Slf4j
@Service
public class GeminiAnalysisService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public GeminiAnalysisService(
            ObjectMapper objectMapper,
            @Value("${gemini.base-url:https://generativelanguage.googleapis.com}") String baseUrl,
            @Value("${gemini.api-key:}") String apiKey,
            @Value("${gemini.model:gemini-2.5-flash}") String model) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
    }

    public JsonNode analyze(String resumeText, Map<String, Object> deterministicScore, String jobDescription) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResumeAnalysisException(HttpStatus.SERVICE_UNAVAILABLE, "GEMINI_NOT_CONFIGURED",
                    "Resume analysis is unavailable until GEMINI_API_KEY is configured");
        }

        String prompt = """
                Analyze the resume text below. Return ONLY valid JSON with these keys:
                candidateName, contactInformation, skills (object with technicalSkills, programmingLanguages,
                frameworks, databases, developerTools, softSkills arrays), education, workExperience, internships,
                projects, certifications, achievements, strengths, weaknesses, missingInformation,
                improvementSuggestions, experienceSuggestions, atsAnalysis (object with score, findings, missingSections,
                keywordSuggestions, parsingRisks), scoreExplanation.
                Do not invent qualifications, skills, employers, dates, or achievements. Only mention facts present
                in the resume. Suggestions must be clearly labeled as suggestions and must never tell the candidate
                to claim an unearned skill. The deterministic score data is supplied only as an input to explain.
                If a section is absent, use an empty array or a clear missing note.

                Deterministic score data:
                %s

                Optional job description:
                %s

                Resume text:
                %s
                """.formatted(toJson(deterministicScore), jobDescription == null ? "(none)" : jobDescription, resumeText);

        try {
            String body = restClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/v1beta/models/{model}:generateContent")
                            .queryParam("key", apiKey).build(model))
                    .body(Map.of(
                            "contents", new Object[]{Map.of("parts", new Object[]{Map.of("text", prompt)})},
                            "generationConfig", Map.of("responseMimeType", "application/json", "temperature", 0.2)
                    ))
                    .retrieve()
                    .body(String.class);
            JsonNode response = objectMapper.readTree(body);
            String content = response.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText("");
            if (content.isBlank()) {
                throw new ResumeAnalysisException(HttpStatus.BAD_GATEWAY, "GEMINI_EMPTY_RESPONSE",
                        "Gemini returned an empty analysis");
            }
            JsonNode parsed = objectMapper.readTree(stripMarkdownFence(content));
            if (!parsed.isObject()) {
                throw new ResumeAnalysisException(HttpStatus.BAD_GATEWAY, "GEMINI_INVALID_RESPONSE",
                        "The resume analysis service returned an invalid response");
            }
            return parsed;
        } catch (ResumeAnalysisException ex) {
            throw ex;
        } catch (RestClientResponseException ex) {
            log.warn("Gemini resume analysis request returned HTTP {}", ex.getStatusCode().value());
            if (ex.getStatusCode().value() == 429) {
                throw new ResumeAnalysisException(HttpStatus.TOO_MANY_REQUESTS, "GEMINI_QUOTA_EXCEEDED",
                        "The resume analysis service quota has been reached. Please try again later");
            }
            throw new ResumeAnalysisException(HttpStatus.BAD_GATEWAY, "GEMINI_SERVICE_ERROR",
                    "The resume analysis service rejected the request");
        } catch (RestClientException ex) {
            log.warn("Gemini resume analysis request failed: {}", ex.getMessage());
            throw new ResumeAnalysisException(HttpStatus.BAD_GATEWAY, "GEMINI_REQUEST_FAILED",
                    "The resume analysis service is temporarily unavailable");
        } catch (Exception ex) {
            log.warn("Gemini returned invalid resume analysis JSON: {}", ex.getMessage());
            throw new ResumeAnalysisException(HttpStatus.BAD_GATEWAY, "GEMINI_INVALID_RESPONSE",
                    "The resume analysis service returned an invalid response");
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return "{}";
        }
    }

    private String stripMarkdownFence(String content) {
        return content.replaceFirst("^\\s*```(?:json)?\\s*", "").replaceFirst("\\s*```\\s*$", "").trim();
    }
}
