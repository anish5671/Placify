package com.placify.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestStartResponseDto {
    private Long testId;
    private String title;
    private String description;
    private String companyName;
    private String category;
    private int durationMinutes;
    private int totalQuestions;
    private int totalMarks;
    private int passingMarks;
    private List<TestQuestionExamDto> questions;
}
