package com.placify.resume;

import com.fasterxml.jackson.databind.JsonNode;
import com.placify.common.ApiResponse;
import com.placify.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeAnalysisController {

    private final ResumeAnalysisService analysisService;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<JsonNode>> analyze(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "jobDescription", required = false) String jobDescription,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success("Resume analyzed successfully", analysisService.analyze(file, jobDescription, principal)));
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<ByteArrayResource> downloadReport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        byte[] report = analysisService.createReport(id, principal);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume-analysis-" + id + ".pdf")
                .contentLength(report.length)
                .body(new ByteArrayResource(report));
    }
}
