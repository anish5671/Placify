package com.placify.company;

import com.placify.exception.ResourceNotFoundException;
import com.placify.question.QuestionDto;
import com.placify.question.QuestionRepository;
import com.placify.question.QuestionService;
import com.placify.test.Test;
import com.placify.test.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findByActiveTrueOrderByNameAsc().stream().map(company -> {
            long qCount = questionRepository.countByCompanyTag(company.getCode());
            long tCount = testRepository.countByCompanyId(company.getId());
            return CompanyDto.fromEntity(company, qCount, tCount);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompanyDetailDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));

        long qCount = questionRepository.countByCompanyTag(company.getCode());
        long tCount = testRepository.countByCompanyId(company.getId());
        return CompanyDetailDto.fromEntity(company, qCount, tCount);
    }

    @Transactional(readOnly = true)
    public List<QuestionDto> getCompanyQuestions(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        return questionService.getQuestions(null, null, null, null, company.getCode());
    }

    @Transactional(readOnly = true)
    public List<Test> getCompanyTests(Long companyId) {
        return testRepository.findByCompanyId(companyId);
    }
}
