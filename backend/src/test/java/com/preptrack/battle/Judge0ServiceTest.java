package com.preptrack.battle;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class Judge0ServiceTest {

    @Test
    void submitsSourceCodeToConfiguredJudge0AndParsesOutput() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://judge.example/submissions?base64_encoded=false&wait=true"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Auth-Token", "test-token"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess("""
                        {"stdout":"42\\n","status":{"id":3,"description":"Accepted"}}
                        """, MediaType.APPLICATION_JSON));

        Judge0Service judge = new Judge0Service("https://judge.example/", "test-token", restTemplate);
        Judge0Service.Execution result = judge.execute("print(42)", "python", "");

        assertTrue(result.succeeded());
        assertEquals("42\n", result.stdout());
        server.verify();
    }

    @Test
    void reportsProviderRateLimitAsAnExplicitError() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://judge.example/submissions?base64_encoded=false&wait=true"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        JudgeProviderException error = assertThrows(JudgeProviderException.class,
                () -> new Judge0Service("https://judge.example", "", restTemplate)
                        .execute("print(1)", "python", ""));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, error.getHttpStatus());
        assertEquals("JUDGE0_RATE_LIMITED", error.getErrorCode());
        assertTrue(error.getMessage().contains("rate-limiting"));
        server.verify();
    }

    @Test
    void rejectsUnsupportedLanguageBeforeCallingProvider() {
        JudgeProviderException error = assertThrows(JudgeProviderException.class,
                () -> new Judge0Service("https://judge.example", "", new RestTemplate())
                        .execute("source", "javascript", ""));

        assertEquals("UNSUPPORTED_LANGUAGE", error.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST, error.getHttpStatus());
    }
}
