package com.preptrack.test;

import com.preptrack.exception.ResourceNotFoundException;
import com.preptrack.question.QuestionHint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final TestQuestionRepository testQuestionRepository;

    @Transactional(readOnly = true)
    public List<TestDto> getAllTests(Long companyId, String category) {
        List<Test> tests;
        if (companyId != null) {
            tests = testRepository.findByCompanyId(companyId);
        } else if (category != null && !category.isBlank()) {
            tests = testRepository.findByCategoryIgnoreCase(category);
        } else {
            tests = testRepository.findAll();
        }

        return tests.stream().map(TestDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestDto getTestById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test", "id", id));
        return TestDto.fromEntity(test);
    }

    @Transactional(readOnly = true)
    public TestStartResponseDto startTest(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test", "id", id));

        List<TestQuestion> testQuestions = testQuestionRepository.findByTestIdOrderByQuestionOrderAsc(id);

        List<TestQuestionExamDto> questionExamDtos = testQuestions.stream().map(tq -> TestQuestionExamDto.builder()
                .questionId(tq.getQuestion().getId())
                .questionOrder(tq.getQuestionOrder())
                .title(tq.getQuestion().getTitle())
                .description(tq.getQuestion().getDescription())
                .category(tq.getQuestion().getCategory())
                .difficulty(tq.getQuestion().getDifficulty())
                .optionA(tq.getQuestion().getOptionA())
                .optionB(tq.getQuestion().getOptionB())
                .optionC(tq.getQuestion().getOptionC())
                .optionD(tq.getQuestion().getOptionD())
                .hint(QuestionHint.forQuestion(tq.getQuestion()))
                .codeSnippet(tq.getQuestion().getCodeSnippet())
                .marks(tq.getMarks())
                .build()
        ).collect(Collectors.toList());

        return TestStartResponseDto.builder()
                .testId(test.getId())
                .title(test.getTitle())
                .description(test.getDescription())
                .companyName(test.getCompany() != null ? test.getCompany().getName() : "General")
                .category(test.getCategory())
                .durationMinutes(test.getDurationMinutes())
                .totalQuestions(test.getTotalQuestions())
                .totalMarks(test.getTotalMarks())
                .passingMarks(test.getPassingMarks())
                .questions(questionExamDtos)
                .build();
    }
}
