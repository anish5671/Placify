package com.preptrack.test;

import com.preptrack.company.Company;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false, length = 50)
    private String category; // e.g. "COMPANY_MOCK", "APTITUDE", "TECHNICAL_CS", "JAVA_OOPS", "DSA"

    @Column(name = "duration_minutes", nullable = false)
    @Builder.Default
    private int durationMinutes = 30;

    @Column(name = "total_questions", nullable = false)
    @Builder.Default
    private int totalQuestions = 15;

    @Column(name = "total_marks", nullable = false)
    @Builder.Default
    private int totalMarks = 30;

    @Column(name = "passing_marks", nullable = false)
    @Builder.Default
    private int passingMarks = 18;

    @Column(length = 20)
    @Builder.Default
    private String difficulty = "MEDIUM";

    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
