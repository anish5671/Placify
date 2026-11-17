package com.placify.result;

import com.placify.question.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_answers", indexes = {
        @Index(name = "idx_user_answer_attempt", columnList = "attempt_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private TestAttempt attempt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "selected_option", length = 10)
    private String selectedOption; // A, B, C, D or null if unattempted

    @Column(name = "is_correct")
    private boolean isCorrect;

    @Column(name = "marks_obtained")
    private int marksObtained;

    @Column(name = "is_marked_for_review")
    private boolean isMarkedForReview;
}
