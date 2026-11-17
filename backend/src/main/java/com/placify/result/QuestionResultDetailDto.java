package com.placify.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultDetailDto {
    private Long questionId;
    private int questionOrder;
    private String title;
    private String description;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String selectedOption;
    private String correctOption;
    private boolean isCorrect;
    private String explanation;
    private String hint;
    private int marksObtained;
    private int maxMarks;
    private boolean isMarkedForReview;
    private String category;
    private String difficulty;
}
