package com.placify.test;

import com.placify.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TestDto>>> getAllTests(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(ApiResponse.success(testService.getAllTests(companyId, category)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestDto>> getTestById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(testService.getTestById(id)));
    }

    @GetMapping("/{id}/start")
    public ResponseEntity<ApiResponse<TestStartResponseDto>> startTest(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(testService.startTest(id)));
    }
}
