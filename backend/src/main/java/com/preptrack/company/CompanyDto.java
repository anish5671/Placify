package com.preptrack.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String code;
    private String logoUrl;
    private String description;
    private String difficultyLevel;
    private String packageInfo;
    private String rolesOffered;
    private long questionCount;
    private long testCount;

    public static CompanyDto fromEntity(Company company, long questionCount, long testCount) {
        return CompanyDto.builder()
                .id(company.getId())
                .name(company.getName())
                .code(company.getCode())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .difficultyLevel(company.getDifficultyLevel())
                .packageInfo(company.getPackageInfo())
                .rolesOffered(company.getRolesOffered())
                .questionCount(questionCount)
                .testCount(testCount)
                .build();
    }
}
