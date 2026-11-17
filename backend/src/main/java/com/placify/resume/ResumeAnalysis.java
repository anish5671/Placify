package com.placify.resume;

import com.placify.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resume_analyses", indexes = @Index(name = "idx_resume_analysis_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "overall_score", nullable = false)
    private int overallScore;

    @Lob
    @Column(name = "extracted_skills_json", nullable = false)
    private String extractedSkillsJson;

    @Lob
    @Column(name = "job_matching_json")
    private String jobMatchingJson;

    @Lob
    @Column(name = "analysis_json", nullable = false)
    private String analysisJson;

    @CreationTimestamp
    @Column(name = "analyzed_at", updatable = false, nullable = false)
    private LocalDateTime analyzedAt;
}
