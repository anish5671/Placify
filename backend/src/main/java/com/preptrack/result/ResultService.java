package com.preptrack.result;

import com.preptrack.exception.ResourceNotFoundException;
import com.preptrack.progress.UserProgress;
import com.preptrack.progress.UserProgressRepository;
import com.preptrack.question.Question;
import com.preptrack.test.Test;
import com.preptrack.test.TestQuestion;
import com.preptrack.test.TestQuestionRepository;
import com.preptrack.test.TestRepository;
import com.preptrack.user.User;
import com.preptrack.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final UserService userService;
    private final TestRepository testRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserProgressRepository userProgressRepository;

    @Transactional
    public TestResultDto submitTest(Long testId, TestSubmissionRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test", "id", testId));

        List<TestQuestion> testQuestions = testQuestionRepository.findByTestIdOrderByQuestionOrderAsc(testId);

        // Map submitted answers by questionId
        Map<Long, TestSubmissionRequest.QuestionAnswerDto> answerMap = new HashMap<>();
        if (request.getAnswers() != null) {
            for (TestSubmissionRequest.QuestionAnswerDto ans : request.getAnswers()) {
                answerMap.put(ans.getQuestionId(), ans);
            }
        }

        int score = 0;
        int totalMarks = 0;
        int correctCount = 0;
        int incorrectCount = 0;
        int unattemptedCount = 0;

        List<UserAnswer> userAnswersToSave = new ArrayList<>();
        List<QuestionResultDetailDto> details = new ArrayList<>();

        // Create initial attempt
        TestAttempt attempt = TestAttempt.builder()
                .user(user)
                .test(test)
                .timeTakenSeconds(request.getTimeTakenSeconds())
                .status("COMPLETED")
                .completedAt(LocalDateTime.now())
                .build();

        for (TestQuestion tq : testQuestions) {
            Question q = tq.getQuestion();
            int marks = tq.getMarks();
            totalMarks += marks;

            TestSubmissionRequest.QuestionAnswerDto userAns = answerMap.get(q.getId());
            String selected = (userAns != null && userAns.getSelectedOption() != null && !userAns.getSelectedOption().isBlank())
                    ? userAns.getSelectedOption().trim().toUpperCase() : null;
            boolean markedForReview = userAns != null && userAns.isMarkedForReview();

            boolean isCorrect = false;
            int marksObtained = 0;

            if (selected == null) {
                unattemptedCount++;
            } else {
                String correctOpt = q.getCorrectOption() != null ? q.getCorrectOption().trim().toUpperCase() : "";
                if (correctOpt.equalsIgnoreCase(selected)) {
                    isCorrect = true;
                    marksObtained = marks;
                    correctCount++;
                    score += marks;

                    // Update UserProgress as solved
                    updateUserProgressAsSolved(user, q);
                } else {
                    incorrectCount++;
                }
            }

            UserAnswer answerEntity = UserAnswer.builder()
                    .attempt(attempt)
                    .question(q)
                    .selectedOption(selected)
                    .isCorrect(isCorrect)
                    .marksObtained(marksObtained)
                    .isMarkedForReview(markedForReview)
                    .build();

            userAnswersToSave.add(answerEntity);

            details.add(QuestionResultDetailDto.builder()
                    .questionId(q.getId())
                    .questionOrder(tq.getQuestionOrder())
                    .title(q.getTitle())
                    .description(q.getDescription())
                    .optionA(q.getOptionA())
                    .optionB(q.getOptionB())
                    .optionC(q.getOptionC())
                    .optionD(q.getOptionD())
                    .selectedOption(selected)
                    .correctOption(q.getCorrectOption())
                    .isCorrect(isCorrect)
                    .explanation(q.getExplanation())
                    .marksObtained(marksObtained)
                    .maxMarks(marks)
                    .isMarkedForReview(markedForReview)
                    .category(q.getCategory())
                    .difficulty(q.getDifficulty())
                    .build());
        }

        double percentage = totalMarks > 0 ? ((double) score / totalMarks) * 100.0 : 0.0;
        percentage = Math.round(percentage * 10.0) / 10.0;
        boolean isPassed = score >= test.getPassingMarks();

        attempt.setScore(score);
        attempt.setTotalMarks(totalMarks);
        attempt.setPercentage(percentage);
        attempt.setCorrectCount(correctCount);
        attempt.setIncorrectCount(incorrectCount);
        attempt.setUnattemptedCount(unattemptedCount);
        attempt.setPassed(isPassed);

        TestAttempt savedAttempt = testAttemptRepository.save(attempt);

        for (UserAnswer ua : userAnswersToSave) {
            ua.setAttempt(savedAttempt);
        }
        userAnswerRepository.saveAll(userAnswersToSave);

        return TestResultDto.fromEntity(savedAttempt, details);
    }

    private void updateUserProgressAsSolved(User user, Question question) {
        UserProgress progress = userProgressRepository.findByUserIdAndQuestionId(user.getId(), question.getId())
                .orElseGet(() -> UserProgress.builder()
                        .user(user)
                        .question(question)
                        .attemptsCount(0)
                        .build());
        progress.setSolved(true);
        progress.setSolvedAt(LocalDateTime.now());
        progress.setAttemptsCount(progress.getAttemptsCount() + 1);
        userProgressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public TestResultDto getAttemptResult(Long attemptId) {
        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("TestAttempt", "id", attemptId));

        List<UserAnswer> userAnswers = userAnswerRepository.findByAttemptIdOrderByIdAsc(attemptId);
        List<TestQuestion> testQuestions = testQuestionRepository.findByTestIdOrderByQuestionOrderAsc(attempt.getTest().getId());

        Map<Long, Integer> orderMap = new HashMap<>();
        Map<Long, Integer> marksMap = new HashMap<>();
        for (TestQuestion tq : testQuestions) {
            orderMap.put(tq.getQuestion().getId(), tq.getQuestionOrder());
            marksMap.put(tq.getQuestion().getId(), tq.getMarks());
        }

        List<QuestionResultDetailDto> details = userAnswers.stream().map(ua -> {
            Question q = ua.getQuestion();
            int order = orderMap.getOrDefault(q.getId(), 1);
            int maxMarks = marksMap.getOrDefault(q.getId(), 2);
            return QuestionResultDetailDto.builder()
                    .questionId(q.getId())
                    .questionOrder(order)
                    .title(q.getTitle())
                    .description(q.getDescription())
                    .optionA(q.getOptionA())
                    .optionB(q.getOptionB())
                    .optionC(q.getOptionC())
                    .optionD(q.getOptionD())
                    .selectedOption(ua.getSelectedOption())
                    .correctOption(q.getCorrectOption())
                    .isCorrect(ua.isCorrect())
                    .explanation(q.getExplanation())
                    .marksObtained(ua.getMarksObtained())
                    .maxMarks(maxMarks)
                    .isMarkedForReview(ua.isMarkedForReview())
                    .category(q.getCategory())
                    .difficulty(q.getDifficulty())
                    .build();
        }).collect(Collectors.toList());

        return TestResultDto.fromEntity(attempt, details);
    }

    @Transactional(readOnly = true)
    public List<TestResultDto> getUserAttempts() {
        User user = userService.getCurrentAuthenticatedUser();
        List<TestAttempt> attempts = testAttemptRepository.findByUserIdOrderByStartedAtDesc(user.getId());
        return attempts.stream()
                .map(a -> TestResultDto.fromEntity(a, Collections.emptyList()))
                .collect(Collectors.toList());
    }
}
