package com.placify.seeder;

import com.placify.question.Question;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DsaPracticeCatalogTest {

    @Test
    void catalogProvidesUniqueQuestionsAcrossElevenModulesWithTwentyProblemsEach() {
        Map<String, com.placify.question.Topic> moduleTopics = new HashMap<>();
        DsaPracticeCatalog.createModuleTopics().forEach(topic ->
                moduleTopics.put(topic.getSlug().substring("dsa-".length()), topic));
        List<Question> questions = DsaPracticeCatalog.createQuestions(moduleTopics);

        assertEquals(212, questions.size());
        assertEquals(11, DsaPracticeCatalog.MODULES.size());
        assertEquals(DsaPracticeCatalog.TARGET_SIZE, questions.size() + 8);

        Set<String> titles = questions.stream()
                .map(Question::getTitle)
                .collect(Collectors.toSet());
        assertEquals(questions.size(), titles.size());

        Map<String, Long> countsByTopic = questions.stream()
                .collect(Collectors.groupingBy(
                        question -> question.getTopic().getSlug(),
                        Collectors.counting()));
        Map<String, Long> legacyQuestionsByModule = Map.of(
                "dsa-arrays", 1L,
                "dsa-strings", 1L,
                "dsa-linked-list", 1L,
                "dsa-binary-search", 1L,
                "dsa-trees", 1L,
                "dsa-dynamic-programming", 2L,
                "dsa-graphs", 1L
        );
        for (DsaPracticeCatalog.Module module : DsaPracticeCatalog.MODULES) {
            String topicSlug = "dsa-" + module.slug();
            assertEquals(20L, countsByTopic.getOrDefault(topicSlug, 0L)
                    + legacyQuestionsByModule.getOrDefault(topicSlug, 0L), module.name());
        }

        Set<String> correctOptions = Set.of("A", "B", "C", "D");
        for (Question question : questions) {
            assertNotNull(question.getDescription());
            assertFalse(question.getDescription().isBlank());
            assertNotNull(question.getTopic());
            assertEquals("DSA", question.getCategory());
            assertEquals("CODE_PRACTICE", question.getQuestionType());
            assertTrue(correctOptions.contains(question.getCorrectOption()));
            assertNotNull(question.getOptionA());
            assertNotNull(question.getOptionB());
            assertNotNull(question.getOptionC());
            assertNotNull(question.getOptionD());
        }

        assertTrue(questions.stream().map(Question::getCorrectOption).distinct().count() > 1);
    }
}
