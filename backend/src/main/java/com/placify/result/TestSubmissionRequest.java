package com.placify.result;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestSubmissionRequest {
    private int timeTakenSeconds;
    private List<QuestionAnswerDto> answers = new ArrayList<>();

    @Data
    public static class QuestionAnswerDto {
        @NotNull
        private Long questionId;
        private String selectedOption; // null or "A", "B", "C", "D"
        private boolean isMarkedForReview;
    }
}
