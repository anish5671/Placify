package com.placify.test;

import com.placify.question.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_questions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"test_id", "question_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "question_order", nullable = false)
    private int questionOrder;

    @Column(nullable = false)
    @Builder.Default
    private int marks = 2;
}
