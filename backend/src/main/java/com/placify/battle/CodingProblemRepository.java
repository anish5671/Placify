package com.placify.battle;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CodingProblemRepository extends JpaRepository<CodingProblem, Long> {
    List<CodingProblem> findAllByOrderByIdAsc();
    Optional<CodingProblem> findBySlug(String slug);
}
