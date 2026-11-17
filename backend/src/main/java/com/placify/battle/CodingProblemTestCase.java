package com.placify.battle;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coding_battle_cases", uniqueConstraints = @UniqueConstraint(
        name = "uk_battle_case_position", columnNames = {"problem_id", "case_position"}),
        indexes = @Index(name = "idx_battle_case_problem", columnList = "problem_id, case_position"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodingProblemTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private CodingProblem problem;

    @Column(name = "case_position", nullable = false)
    private int position;

    @Column(nullable = false)
    private boolean hidden;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String input;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;
}
