package com.preptrack.seeder;

import com.preptrack.question.Question;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockAssessmentCatalogTest {

    @Test
    void createsOneHundredUniqueAptitudeQuestionsWithHintsAndSolutions() {
        List<Question> questions = MockAssessmentCatalog.createAptitudeQuestions(null, 1, 100);

        assertEquals(100, questions.size());
        assertEquals(100, questions.stream().map(Question::getTitle).distinct().count());
        assertQuestionsHaveFourOptionsAndValidAnswers(questions);
        assertTrue(questions.stream().allMatch(question -> question.getCategory().equals("APTITUDE")));
    }

    @Test
    void createsOneHundredUniqueCoreQuestionsAcrossFourCseSubjects() {
        HashMap<String, com.preptrack.question.Topic> topics = new HashMap<>();
        MockAssessmentCatalog.CORE_CATEGORIES.forEach(category -> topics.put(category, null));
        List<Question> questions = MockAssessmentCatalog.createCoreQuestions(topics, 1, 100);

        assertEquals(100, questions.size());
        assertEquals(100, questions.stream().map(Question::getTitle).distinct().count());
        assertQuestionsHaveFourOptionsAndValidAnswers(questions);
        for (String category : MockAssessmentCatalog.CORE_CATEGORIES) {
            assertEquals(25, questions.stream().filter(question -> question.getCategory().equals(category)).count());
        }
    }

    private void assertQuestionsHaveFourOptionsAndValidAnswers(List<Question> questions) {
        Set<String> validOptions = Set.of("A", "B", "C", "D");
        for (Question question : questions) {
            List<String> options = List.of(
                    question.getOptionA(),
                    question.getOptionB(),
                    question.getOptionC(),
                    question.getOptionD()
            );
            assertEquals(4, options.stream().distinct().count(), question.getTitle());
            assertFalse(options.stream().anyMatch(String::isBlank), question.getTitle());
            assertTrue(validOptions.contains(question.getCorrectOption()), question.getTitle());
            assertNotNull(question.getExplanation(), question.getTitle());
            assertFalse(question.getExplanation().isBlank(), question.getTitle());
            assertNotNull(question.getHint(), question.getTitle());
            assertFalse(question.getHint().isBlank(), question.getTitle());
        }
    }
}
