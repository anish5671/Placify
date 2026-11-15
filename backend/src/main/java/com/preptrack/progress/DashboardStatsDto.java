package com.preptrack.progress;

import com.preptrack.result.TestResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalQuestionsSolved;
    private long totalQuestionsAvailable;
    private long totalTestsAttempted;
    private double averageTestScore;
    private double overallReadinessPercentage;
    private long totalBookmarks;
    private List<CategoryProgressDto> categoryStats;
    private List<TestResultDto> recentAttempts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryProgressDto {
        private String category;
        private String displayName;
        private long solved;
        private long total;
        private double percentage;
    }
}
