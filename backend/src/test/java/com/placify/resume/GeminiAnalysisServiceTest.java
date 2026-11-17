package com.placify.resume;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

class GeminiAnalysisServiceTest {

    private static final String API_KEY = "test-api-key";
    private static final String BASE_URL = "https://generativelanguage.googleapis.com";
    private MockRestServiceServer server;
    private GeminiAnalysisService service;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        service = new GeminiAnalysisService(
                new ObjectMapper(), builder, BASE_URL, API_KEY, "gemini-2.5-flash");
    }

    @Test
    void sendsApiKeyInHeaderAndExplainsForbiddenResponse() {
        server.expect(requestTo(BASE_URL + "/v1beta/models/gemini-2.5-flash:generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-goog-api-key", API_KEY))
                .andRespond(withStatus(org.springframework.http.HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {"error":{"code":403,"message":"API key is not authorized","status":"PERMISSION_DENIED"}}
                                """));

        ResumeAnalysisException exception = assertThrows(ResumeAnalysisException.class,
                () -> service.analyze("resume text", Map.of(), null));

        assertEquals("GEMINI_ACCESS_DENIED", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("API key is valid"));
        server.verify();
    }
}
