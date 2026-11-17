package com.placify.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private Long companyId;
    private String companyName;
    private String companyCode;
    private String companyLogoUrl;
    private String category;
    private int durationMinutes;
    private int totalQuestions;
    private int totalMarks;
    private int passingMarks;
    private String difficulty;
    private LocalDateTime createdAt;

    public static TestDto fromEntity(Test test) {
        return TestDto.builder()
                .id(test.getId())
                .title(test.getTitle())
                .slug(test.getSlug())
                .description(test.getDescription())
                .companyId(test.getCompany() != null ? test.getCompany().getId() : null)
                .companyName(test.getCompany() != null ? test.getCompany().getName() : null)
                .companyCode(test.getCompany() != null ? test.getCompany().getCode() : null)
                .companyLogoUrl(test.getCompany() != null ? test.getCompany().getLogoUrl() : null)
                .category(test.getCategory())
                .durationMinutes(test.getDurationMinutes())
                .totalQuestions(test.getTotalQuestions())
                .totalMarks(test.getTotalMarks())
                .passingMarks(test.getPassingMarks())
                .difficulty(test.getDifficulty())
                .createdAt(test.getCreatedAt())
                .build();
    }
}
