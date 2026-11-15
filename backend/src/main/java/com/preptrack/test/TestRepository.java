package com.preptrack.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findBySlug(String slug);
    List<Test> findByCompanyId(Long companyId);
    List<Test> findByCategoryIgnoreCase(String category);
    long countByCompanyId(Long companyId);
}
