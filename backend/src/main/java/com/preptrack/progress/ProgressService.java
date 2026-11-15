package com.preptrack.progress;

import com.preptrack.question.QuestionRepository;
import com.preptrack.question.BookmarkRepository;
import com.preptrack.result.TestAttempt;
import com.preptrack.result.TestAttemptRepository;
import com.preptrack.result.TestResultDto;
import com.preptrack.user.User;
import com.preptrack.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserService userService;
    private final UserProgressRepository userProgressRepository;
    private final QuestionRepository questionRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        User user = userService.getCurrentAuthenticatedUser();
        Long userId = user.getId();

        long solvedQuestions = userProgressRepository.countByUserIdAndIsSolvedTrue(userId);
        long totalQuestions = questionRepository.count();
        long testsAttempted = testAttemptRepository.countByUserId(userId);
        Double avgScore = testAttemptRepository.getAverageScoreByUserId(userId);
        double averageTestScore = avgScore != null ? Math.round(avgScore * 10.0) / 10.0 : 0.0;
        long bookmarksCount = bookmarkRepository.countByUserId(userId);

        // Calculate Overall Readiness
        // Weighted formula: 50% test score avg + 50% question completion
        double questionRatio = totalQuestions > 0 ? ((double) solvedQuestions / totalQuestions) * 100.0 : 0.0;
        double overallReadiness = (testsAttempted > 0)
                ? (0.5 * averageTestScore) + (0.5 * Math.min(100.0, questionRatio * 2.5))
                : Math.min(100.0, questionRatio * 3.0);
        overallReadiness = Math.round(overallReadiness * 10.0) / 10.0;

        // Categories
        List<String[]> categories = List.of(
                new String[]{"APTITUDE", "Quantitative & Aptitude"},
                new String[]{"REASONING", "Logical Reasoning"},
                new String[]{"VERBAL", "Verbal Ability"},
                new String[]{"JAVA_OOPS", "Java & OOPs"},
                new String[]{"DBMS", "DBMS & SQL"},
                new String[]{"OS", "Operating Systems"},
                new String[]{"NETWORKS", "Computer Networks"},
                new String[]{"DSA", "Data Structures & Algos"}
        );

        List<DashboardStatsDto.CategoryProgressDto> categoryStats = new ArrayList<>();
        for (String[] cat : categories) {
            String code = cat[0];
            String name = cat[1];
            long catTotal = questionRepository.countByCategoryIgnoreCase(code);
            long catSolved = userProgressRepository.countSolvedByUserIdAndCategory(userId, code);
            double pct = catTotal > 0 ? Math.round(((double) catSolved / catTotal) * 1000.0) / 10.0 : 0.0;

            categoryStats.add(DashboardStatsDto.CategoryProgressDto.builder()
                    .category(code)
                    .displayName(name)
                    .total(catTotal)
                    .solved(catSolved)
                    .percentage(pct)
                    .build());
        }

        List<TestAttempt> recentAttemptsList = testAttemptRepository.findTop5ByUserIdOrderByStartedAtDesc(userId);
        List<TestResultDto> recentAttempts = recentAttemptsList.stream()
                .map(a -> TestResultDto.fromEntity(a, Collections.emptyList()))
                .collect(Collectors.toList());

        return DashboardStatsDto.builder()
                .totalQuestionsSolved(solvedQuestions)
                .totalQuestionsAvailable(totalQuestions)
                .totalTestsAttempted(testsAttempted)
                .averageTestScore(averageTestScore)
                .overallReadinessPercentage(overallReadiness)
                .totalBookmarks(bookmarksCount)
                .categoryStats(categoryStats)
                .recentAttempts(recentAttempts)
                .build();
    }
}
