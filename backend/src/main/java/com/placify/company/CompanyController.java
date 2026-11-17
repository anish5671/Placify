package com.placify.company;

import com.placify.common.ApiResponse;
import com.placify.question.Question;
import com.placify.test.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyDto>>> getAllCompanies() {
        return ResponseEntity.ok(ApiResponse.success(companyService.getAllCompanies()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDetailDto>> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(companyService.getCompanyById(id)));
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<ApiResponse<List<Question>>> getCompanyQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(companyService.getCompanyQuestions(id)));
    }

    @GetMapping("/{id}/tests")
    public ResponseEntity<ApiResponse<List<Test>>> getCompanyTests(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(companyService.getCompanyTests(id)));
    }
}
