package com.preptrack.company;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "hiring_process", columnDefinition = "TEXT")
    private String hiringProcess;

    @Column(name = "eligibility_criteria", columnDefinition = "TEXT")
    private String eligibilityCriteria;

    @Column(name = "syllabus_overview", columnDefinition = "TEXT")
    private String syllabusOverview;

    @Column(name = "difficulty_level", length = 30)
    private String difficultyLevel;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "package_info", length = 100)
    private String packageInfo;

    @Column(name = "roles_offered", length = 255)
    private String rolesOffered;

    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
