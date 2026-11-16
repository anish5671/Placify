package com.preptrack.battle;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CodingBattleCatalogTest {

    @Test
    void seedsFiftyDistinctProblemsWithTwoVisibleAndEightHiddenCasesEach() {
        List<CodingBattleCatalog.SeedProblem> problems = CodingBattleCatalog.problems();

        assertEquals(50, problems.size());
        Set<String> slugs = new HashSet<>();
        Set<String> titles = new HashSet<>();
        for (CodingBattleCatalog.SeedProblem seed : problems) {
            CodingProblem problem = seed.toEntity();
            assertTrue(slugs.add(problem.getSlug()), "Duplicate slug: " + problem.getSlug());
            assertTrue(titles.add(problem.getTitle()), "Duplicate title: " + problem.getTitle());
            assertNotNull(problem.getDescription());
            assertFalse(problem.getDescription().isBlank());
            assertFalse(problem.getInputFormat().isBlank());
            assertFalse(problem.getOutputFormat().isBlank());
            assertFalse(problem.getConstraints().isBlank());
            assertEquals(10, seed.cases().size(), problem.getTitle());
            assertEquals(2, seed.cases().stream().filter(testCase -> !testCase.hidden()).count(), problem.getTitle());
            assertEquals(8, seed.cases().stream().filter(CodingBattleCatalog.TestInput::hidden).count(), problem.getTitle());
            assertTrue(seed.cases().stream().allMatch(testCase -> testCase.output() != null));
        }
    }

    @Test
    void roomProblemDtoOnlyDefinesVisibleExamples() {
        assertEquals(8, CodingProblemDto.class.getRecordComponents().length);
        assertEquals(3, CodingProblemDto.Example.class.getRecordComponents().length);
        assertEquals(List.of("id", "title", "difficulty", "description", "inputFormat", "outputFormat", "constraints", "examples"),
                List.of(CodingProblemDto.class.getRecordComponents()).stream().map(component -> component.getName()).toList());
        assertEquals(List.of("number", "input", "output"),
                List.of(CodingProblemDto.Example.class.getRecordComponents()).stream().map(component -> component.getName()).toList());
    }

    @Test
    void duplicateSearchValuesExpectTheFirstMatchingIndex() {
        CodingBattleCatalog.SeedProblem seed = CodingBattleCatalog.problems().stream()
                .filter(problem -> problem.spec().slug().equals("binary-search"))
                .findFirst().orElseThrow();

        assertEquals("0", seed.cases().get(8).output());
    }
}
