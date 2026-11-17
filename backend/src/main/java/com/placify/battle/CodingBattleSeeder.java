package com.placify.battle;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CodingBattleSeeder implements ApplicationRunner {

    private final CodingProblemRepository problemRepository;
    private final CodingProblemTestCaseRepository caseRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (CodingBattleCatalog.SeedProblem seed : CodingBattleCatalog.problems()) {
            CodingProblem problem = problemRepository.findBySlug(seed.spec().slug())
                    .orElseGet(() -> problemRepository.saveAndFlush(seed.toEntity()));
            List<CodingProblemTestCase> existing = caseRepository
                    .findByProblemIdOrderByPositionAsc(problem.getId());
            long visibleCount = existing.stream().filter(testCase -> !testCase.isHidden()).count();
            if (existing.size() == 10 && visibleCount == 2) continue;

            if (!existing.isEmpty()) caseRepository.deleteAll(existing);
            List<CodingProblemTestCase> testCases = new ArrayList<>();
            for (int i = 0; i < seed.cases().size(); i++) {
                CodingBattleCatalog.TestInput testInput = seed.cases().get(i);
                testCases.add(CodingProblemTestCase.builder()
                        .problem(problem)
                        .position(i)
                        .hidden(testInput.hidden())
                        .input(testInput.input())
                        .expectedOutput(testInput.output())
                        .build());
            }
            caseRepository.saveAll(testCases);
        }
    }
}
