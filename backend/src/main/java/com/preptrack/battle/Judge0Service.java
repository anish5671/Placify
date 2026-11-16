package com.preptrack.battle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.time.Duration;
import java.util.*;

@Service
public class Judge0Service {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String authToken;

    @Autowired
    public Judge0Service(
            @Value("${judge0.base-url:https://ce.judge0.com}") String baseUrl,
            @Value("${judge0.auth-token:}") String authToken
    ) {
        this(baseUrl, authToken, createRestTemplate());
    }

    Judge0Service(String baseUrl, String authToken, RestTemplate restTemplate) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.authToken = authToken;
        this.restTemplate = restTemplate;
    }

    private static RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(60));
        return new RestTemplate(factory);
    }

    public Execution execute(String sourceCode, String language, String input) {
        int languageId = switch (language.toLowerCase(Locale.ROOT)) {
            case "cpp", "c++" -> 54;
            case "java" -> 62;
            case "python", "python3" -> 71;
            default -> throw new JudgeProviderException(
                    HttpStatus.BAD_REQUEST, "UNSUPPORTED_LANGUAGE",
                    "Choose C++, Java, or Python 3."
            );
        };

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("source_code", sourceCode);
        payload.put("language_id", languageId);
        payload.put("stdin", input);
        payload.put("cpu_time_limit", 3);
        payload.put("memory_limit", 128000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (authToken != null && !authToken.isBlank()) {
            headers.set("X-Auth-Token", authToken);
        }

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    baseUrl + "/submissions?base64_encoded=false&wait=true",
                    HttpMethod.POST,
                    new HttpEntity<>(payload, headers),
                    new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> body = response.getBody();
            if (body == null) {
                throw providerUnavailable("Judge0 returned an empty response.");
            }
            return parseExecution(body);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 429) {
                throw new JudgeProviderException(HttpStatus.TOO_MANY_REQUESTS, "JUDGE0_RATE_LIMITED",
                        "Judge0's free service is rate-limiting requests. Wait a little and try again.");
            }
            if (ex.getStatusCode().value() == 401 || ex.getStatusCode().value() == 403) {
                throw new JudgeProviderException(HttpStatus.BAD_GATEWAY, "JUDGE0_AUTH_FAILED",
                        "Judge0 rejected its configured credentials. Check JUDGE0_AUTH_TOKEN.");
            }
            throw new JudgeProviderException(HttpStatus.BAD_GATEWAY, "JUDGE0_REQUEST_REJECTED",
                    "Judge0 rejected the execution request (HTTP " + ex.getStatusCode().value() + "). "
                            + "Check the configured provider and language.");
        } catch (HttpServerErrorException ex) {
            throw providerUnavailable("Judge0 is temporarily unavailable (HTTP "
                    + ex.getStatusCode().value() + "). Try again shortly.");
        } catch (ResourceAccessException ex) {
            throw new JudgeProviderException(HttpStatus.SERVICE_UNAVAILABLE, "JUDGE0_UNREACHABLE",
                    "Unable to reach Judge0. Check the provider URL and network, then try again.");
        } catch (IllegalArgumentException ex) {
            throw new JudgeProviderException(HttpStatus.SERVICE_UNAVAILABLE, "JUDGE0_CONFIGURATION_ERROR",
                    "Judge0's base URL is invalid. Set JUDGE0_BASE_URL to a valid provider URL.");
        } catch (RestClientException ex) {
            throw providerUnavailable("Could not read the response from Judge0. Try again shortly.");
        }
    }

    private Execution parseExecution(Map<String, Object> response) {
        Object rawStatus = response.get("status");
        Map<?, ?> status = rawStatus instanceof Map<?, ?> map ? map : Map.of();
        int statusId = status.get("id") instanceof Number number ? number.intValue() : -1;
        String description = Objects.toString(status.get("description"), "Unknown Judge0 status");
        if (statusId == 1 || statusId == 2) {
            throw providerUnavailable("Judge0 did not finish execution in time. Try again shortly.");
        }
        String stdout = Objects.toString(response.get("stdout"), "");
        String stderr = Objects.toString(response.get("stderr"), "");
        String compileOutput = Objects.toString(response.get("compile_output"), "");
        String message = Objects.toString(response.get("message"), "");
        return new Execution(statusId, description, stdout, stderr, compileOutput, message);
    }

    private JudgeProviderException providerUnavailable(String message) {
        return new JudgeProviderException(HttpStatus.SERVICE_UNAVAILABLE, "JUDGE0_UNAVAILABLE", message);
    }

    public record Execution(
            int statusId,
            String status,
            String stdout,
            String stderr,
            String compileOutput,
            String providerMessage
    ) {
        public boolean succeeded() {
            return statusId == 3;
        }

        public String diagnostic() {
            if (!compileOutput.isBlank()) return compileOutput;
            if (!stderr.isBlank()) return stderr;
            return providerMessage;
        }
    }
}
