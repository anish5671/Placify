package com.placify.progress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndQuestionId(Long userId, Long questionId);
    boolean existsByUserIdAndQuestionIdAndIsSolvedTrue(Long userId, Long questionId);
    List<UserProgress> findByUserIdAndIsSolvedTrue(Long userId);
    long countByUserIdAndIsSolvedTrue(Long userId);

    @Query("SELECT COUNT(up) FROM UserProgress up WHERE up.user.id = :userId AND up.isSolved = true AND up.question.category = :category")
    long countSolvedByUserIdAndCategory(@Param("userId") Long userId, @Param("category") String category);

    @Query("SELECT COUNT(up) FROM UserProgress up WHERE up.user.id = :userId AND up.isSolved = true AND up.question.topic.id = :topicId")
    long countSolvedByUserIdAndTopicId(@Param("userId") Long userId, @Param("topicId") Long topicId);
}
