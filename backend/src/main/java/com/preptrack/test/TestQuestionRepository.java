package com.preptrack.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {
    List<TestQuestion> findByTestIdOrderByQuestionOrderAsc(Long testId);
    void deleteByTestId(Long testId);
}
