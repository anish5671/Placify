package com.placify.seeder;

import com.placify.question.Question;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DsaPracticeCatalogTest {

    @Test
    void catalogProvidesUniqueQuestionsToCompleteThe150ProblemSheet() {
        List<Question> questions = DsaPracticeCatalog.createQuestions(null);

        assertEquals(142, questions.size());
        assertEquals(DsaPracticeCatalog.TARGET_SIZE, questions.size() + 8);

        Set<String> titles = questions.stream()
                .map(Question::getTitle)
                .collect(Collectors.toSet());
        assertEquals(questions.size(), titles.size());

        Set<String> correctOptions = Set.of("A", "B", "C", "D");
        for (Question question : questions) {
            assertNotNull(question.getDescription());
            assertFalse(question.getDescription().isBlank());
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
