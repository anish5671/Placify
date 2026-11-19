package com.placify.resume;

import com.placify.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeAnalysisController {
    private final ResumeAnalysisService analysisService;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResumeAnalysisResult>> analyze(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "jobDescription", required = false) String jobDescription) {
        return ResponseEntity.ok(ApiResponse.success(
                "Resume analyzed successfully",
                analysisService.analyze(file, jobDescription))) ;
    }
}
