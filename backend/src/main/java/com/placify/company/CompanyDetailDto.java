package com.placify.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailDto {
    private Long id;
    private String name;
    private String code;
    private String logoUrl;
    private String description;
    private String hiringProcess;
    private String eligibilityCriteria;
    private String syllabusOverview;
    private String difficultyLevel;
    private String websiteUrl;
    private String packageInfo;
    private String rolesOffered;
    private long questionCount;
    private long testCount;

    public static CompanyDetailDto fromEntity(Company company, long questionCount, long testCount) {
        return CompanyDetailDto.builder()
                .id(company.getId())
                .name(company.getName())
                .code(company.getCode())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .hiringProcess(company.getHiringProcess())
                .eligibilityCriteria(company.getEligibilityCriteria())
                .syllabusOverview(company.getSyllabusOverview())
                .difficultyLevel(company.getDifficultyLevel())
                .websiteUrl(company.getWebsiteUrl())
                .packageInfo(company.getPackageInfo())
                .rolesOffered(company.getRolesOffered())
                .questionCount(questionCount)
                .testCount(testCount)
                .build();
    }
}
