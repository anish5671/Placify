package com.placify.resume;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GeminiAnalysisServiceTest {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com";

    @Test
    void returnsStructuredFeedbackAndSendsKeyInHeader() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo(BASE_URL + "/v1beta/models/gemini-2.5-flash:generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-goog-api-key", "test-key"))
                .andRespond(withSuccess("""
                        {"candidates":[{"content":{"parts":[{"text":"{\\"summary\\":\\"Backend profile\\",\\"strengths\\":[\\"Java\\"],\\"improvementSuggestions\\":[\\"Add project impact\\"]}"}]}}]}
                        """, MediaType.APPLICATION_JSON));

        var service = service(builder, "test-key");
        var feedback = service.analyze("Java Spring developer", List.of("java"));

        assertEquals("AVAILABLE", feedback.status());
        assertEquals("Backend profile", feedback.summary());
        assertEquals(List.of("Java"), feedback.strengths());
        server.verify();
    }

    @Test
    void keepsLocalAnalysisAvailableWhenGeminiDeniesAccess() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo(BASE_URL + "/v1beta/models/gemini-2.5-flash:generateContent"))
                .andRespond(withStatus(org.springframework.http.HttpStatus.FORBIDDEN));

        var feedback = service(builder, "test-key").analyze("Java developer", List.of("java"));

        assertEquals("ACCESS_DENIED", feedback.status());
        server.verify();
    }

    @Test
    void reportsMissingKeyWithoutMakingARequest() {
        var feedback = service(RestClient.builder(), "").analyze("Java developer", List.of("java"));

        assertEquals("NOT_CONFIGURED", feedback.status());
    }

    private GeminiAnalysisService service(RestClient.Builder builder, String key) {
        return new GeminiAnalysisService(builder, new ObjectMapper(), BASE_URL, key, "gemini-2.5-flash");
    }
}
