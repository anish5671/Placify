package com.placify.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    List<TestAttempt> findByUserIdOrderByStartedAtDesc(Long userId);
    List<TestAttempt> findTop5ByUserIdOrderByStartedAtDesc(Long userId);
    long countByUserId(Long userId);

    @Query("SELECT AVG(ta.percentage) FROM TestAttempt ta WHERE ta.user.id = :userId")
    Double getAverageScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(ta.score) FROM TestAttempt ta WHERE ta.user.id = :userId")
    Integer getTotalScoreByUserId(@Param("userId") Long userId);
}
