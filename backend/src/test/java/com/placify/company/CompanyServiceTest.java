package com.placify.company;

import com.placify.question.QuestionDto;
import com.placify.question.QuestionService;
import com.placify.question.QuestionRepository;
import com.placify.test.TestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void getCompanyQuestionsReturnsUserAwareQuestionDtos() {
        Company company = Company.builder().id(4L).code("ACCENTURE").build();
        List<QuestionDto> questions = List.of(QuestionDto.builder().id(8L).isSolved(true).isBookmarked(true).build());
        when(companyRepository.findById(4L)).thenReturn(Optional.of(company));
        when(questionService.getQuestions(null, null, null, null, "ACCENTURE")).thenReturn(questions);

        assertSame(questions, companyService.getCompanyQuestions(4L));
        verify(questionService).getQuestions(null, null, null, null, "ACCENTURE");
    }
}
