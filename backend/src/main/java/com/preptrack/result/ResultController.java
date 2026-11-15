package com.preptrack.result;

import com.preptrack.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @PostMapping("/tests/{id}/submit")
    public ResponseEntity<ApiResponse<TestResultDto>> submitTest(
            @PathVariable Long id,
            @Valid @RequestBody TestSubmissionRequest request
    ) {
        TestResultDto result = resultService.submitTest(id, request);
        return ResponseEntity.ok(ApiResponse.success("Test submitted and evaluated successfully", result));
    }

    @GetMapping("/results/{attemptId}")
    public ResponseEntity<ApiResponse<TestResultDto>> getAttemptResult(@PathVariable Long attemptId) {
        return ResponseEntity.ok(ApiResponse.success(resultService.getAttemptResult(attemptId)));
    }

    @GetMapping("/results/my-attempts")
    public ResponseEntity<ApiResponse<List<TestResultDto>>> getUserAttempts() {
        return ResponseEntity.ok(ApiResponse.success(resultService.getUserAttempts()));
    }
}
