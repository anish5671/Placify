package com.preptrack.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long id;
    private String title;
    private String description;
    private Long topicId;
    private String topicName;
    private String category;
    private String difficulty;
    private String questionType;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
    private String explanation;
    private String companyTags;
    private String externalLink;
    private String codeSnippet;
    private String constraints;
    private String sampleInput;
    private String sampleOutput;
    private boolean isBookmarked;
    private boolean isSolved;
    private LocalDateTime createdAt;

    public static QuestionDto fromEntity(Question q, boolean isBookmarked, boolean isSolved) {
        return QuestionDto.builder()
                .id(q.getId())
                .title(q.getTitle())
                .description(q.getDescription())
                .topicId(q.getTopic() != null ? q.getTopic().getId() : null)
                .topicName(q.getTopic() != null ? q.getTopic().getName() : null)
                .category(q.getCategory())
                .difficulty(q.getDifficulty())
                .questionType(q.getQuestionType())
                .optionA(q.getOptionA())
                .optionB(q.getOptionB())
                .optionC(q.getOptionC())
                .optionD(q.getOptionD())
                .correctOption(q.getCorrectOption())
                .explanation(q.getExplanation())
                .companyTags(q.getCompanyTags())
                .externalLink(q.getExternalLink())
                .codeSnippet(q.getCodeSnippet())
                .constraints(q.getConstraints())
                .sampleInput(q.getSampleInput())
                .sampleOutput(q.getSampleOutput())
                .isBookmarked(isBookmarked)
                .isSolved(isSolved)
                .createdAt(q.getCreatedAt())
                .build();
    }
}
