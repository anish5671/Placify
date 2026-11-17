package com.placify.question;

import com.placify.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String company
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                questionService.getQuestions(category, topicId, difficulty, search, company)
        ));
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<ApiResponse<QuestionDto>> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(questionService.getQuestionById(id)));
    }

    @PostMapping("/questions/{id}/bookmark")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleBookmark(@PathVariable Long id) {
        boolean bookmarked = questionService.toggleBookmark(id);
        return ResponseEntity.ok(ApiResponse.success(
                bookmarked ? "Question added to bookmarks" : "Question removed from bookmarks",
                Map.of("questionId", id, "isBookmarked", bookmarked)
        ));
    }

    @PostMapping("/questions/{id}/solve")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleSolved(@PathVariable Long id) {
        boolean solved = questionService.toggleSolved(id);
        return ResponseEntity.ok(ApiResponse.success(
                solved ? "Question marked as solved" : "Question marked as unsolved",
                Map.of("questionId", id, "isSolved", solved)
        ));
    }

    @GetMapping("/questions/bookmarked")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getBookmarkedQuestions() {
        return ResponseEntity.ok(ApiResponse.success(questionService.getBookmarkedQuestions()));
    }

    @GetMapping("/questions/dsa")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getDsaQuestions(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty
    ) {
        return ResponseEntity.ok(ApiResponse.success(questionService.getDsaQuestions(topic, difficulty)));
    }

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<TopicDto>>> getAllTopics() {
        return ResponseEntity.ok(ApiResponse.success(questionService.getAllTopics()));
    }
}
