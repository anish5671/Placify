package com.placify.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategoryIgnoreCase(String category);

    List<Question> findByTopicId(Long topicId);

    List<Question> findByDifficultyIgnoreCase(String difficulty);

    @Query("SELECT q FROM Question q WHERE (:category = '' OR LOWER(q.category) = LOWER(:category)) " +
           "AND (:topicId IS NULL OR q.topic.id = :topicId) " +
           "AND (:difficulty = '' OR LOWER(q.difficulty) = LOWER(:difficulty)) " +
           "AND (:search = '' OR LOWER(q.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(q.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:company = '' OR LOWER(q.companyTags) LIKE LOWER(CONCAT('%', :company, '%')))")
    List<Question> findQuestionsWithFilters(
            @Param("category") String category,
            @Param("topicId") Long topicId,
            @Param("difficulty") String difficulty,
            @Param("search") String search,
            @Param("company") String company
    );

    @Query("SELECT q FROM Question q WHERE LOWER(q.companyTags) LIKE LOWER(CONCAT('%', :companyCode, '%'))")
    List<Question> findByCompanyTag(@Param("companyCode") String companyCode);

    long countByCategoryIgnoreCase(String category);

    long countByTopicId(Long topicId);

    @Query("SELECT COUNT(q) FROM Question q WHERE LOWER(q.companyTags) LIKE LOWER(CONCAT('%', :companyCode, '%'))")
    long countByCompanyTag(@Param("companyCode") String companyCode);
}
