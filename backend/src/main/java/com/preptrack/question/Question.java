package com.preptrack.question;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions", indexes = {
        @Index(name = "idx_question_category", columnList = "category"),
        @Index(name = "idx_question_difficulty", columnList = "difficulty"),
        @Index(name = "idx_question_topic", columnList = "topic_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(nullable = false, length = 50)
    private String category; // APTITUDE, REASONING, VERBAL, JAVA_OOPS, DBMS, OS, NETWORKS, SQL, DSA

    @Column(nullable = false, length = 20)
    private String difficulty; // EASY, MEDIUM, HARD

    @Column(name = "question_type", length = 30)
    @Builder.Default
    private String questionType = "MCQ"; // MCQ, CODE_PRACTICE

    @Column(name = "option_a", columnDefinition = "TEXT")
    private String optionA;

    @Column(name = "option_b", columnDefinition = "TEXT")
    private String optionB;

    @Column(name = "option_c", columnDefinition = "TEXT")
    private String optionC;

    @Column(name = "option_d", columnDefinition = "TEXT")
    private String optionD;

    @Column(name = "correct_option", length = 10)
    private String correctOption; // A, B, C, D

    @Column(columnDefinition = "LONGTEXT")
    private String explanation;

    @Column(columnDefinition = "TEXT")
    private String hint;

    @Column(name = "company_tags", length = 255)
    private String companyTags; // e.g., "TCS,Infosys,Amazon"

    @Column(name = "external_link", length = 500)
    private String externalLink; // LeetCode or GFG link

    @Column(name = "code_snippet", columnDefinition = "LONGTEXT")
    private String codeSnippet;

    @Column(columnDefinition = "TEXT")
    private String constraints;

    @Column(name = "sample_input", columnDefinition = "TEXT")
    private String sampleInput;

    @Column(name = "sample_output", columnDefinition = "TEXT")
    private String sampleOutput;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
