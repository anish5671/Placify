package com.preptrack.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDto {
    private Long attemptId;
    private Long testId;
    private String testTitle;
    private String companyName;
    private String category;
    private int score;
    private int totalMarks;
    private double percentage;
    private int correctCount;
    private int incorrectCount;
    private int unattemptedCount;
    private int timeTakenSeconds;
    private boolean isPassed;
    private int passingMarks;
    private LocalDateTime completedAt;
    private List<QuestionResultDetailDto> questionDetails;

    public static TestResultDto fromEntity(TestAttempt attempt, List<QuestionResultDetailDto> details) {
        return TestResultDto.builder()
                .attemptId(attempt.getId())
                .testId(attempt.getTest().getId())
                .testTitle(attempt.getTest().getTitle())
                .companyName(attempt.getTest().getCompany() != null ? attempt.getTest().getCompany().getName() : "General")
                .category(attempt.getTest().getCategory())
                .score(attempt.getScore())
                .totalMarks(attempt.getTotalMarks())
                .percentage(attempt.getPercentage())
                .correctCount(attempt.getCorrectCount())
                .incorrectCount(attempt.getIncorrectCount())
                .unattemptedCount(attempt.getUnattemptedCount())
                .timeTakenSeconds(attempt.getTimeTakenSeconds())
                .isPassed(attempt.isPassed())
                .passingMarks(attempt.getTest().getPassingMarks())
                .completedAt(attempt.getCompletedAt())
                .questionDetails(details)
                .build();
    }
}
