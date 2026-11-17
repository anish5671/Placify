package com.placify.battle;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CodingProblemTestCaseRepository extends JpaRepository<CodingProblemTestCase, Long> {
    List<CodingProblemTestCase> findByProblemIdOrderByPositionAsc(Long problemId);
}
