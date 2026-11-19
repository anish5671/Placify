package com.placify.seeder;

import com.placify.question.Question;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockAssessmentCatalogTest {

    @Test
    void createsThreeHundredUniqueAptitudeQuestionsWithHintsAndSolutions() {
        List<Question> questions = MockAssessmentCatalog.createAptitudeQuestions(
                null, 1, MockAssessmentCatalog.APTITUDE_BANK_SIZE);

        assertEquals(300, questions.size());
        assertEquals(300, questions.stream().map(Question::getTitle).distinct().count());
        assertEquals(300, questions.stream()
                .map(question -> question.getTitle().substring(question.getTitle().indexOf(": ") + 2))
                .distinct()
                .count());
        assertQuestionsHaveFourOptionsAndValidAnswers(questions);
        assertTrue(questions.stream().allMatch(question -> question.getCategory().equals("APTITUDE")));
    }

    @Test
    void createsOneHundredFiftyUniqueCoreQuestionsAcrossFourCseSubjects() {
        HashMap<String, com.placify.question.Topic> topics = new HashMap<>();
        MockAssessmentCatalog.CORE_CATEGORIES.forEach(category -> topics.put(category, null));
        List<Question> questions = MockAssessmentCatalog.createCoreQuestions(
                topics, 1, MockAssessmentCatalog.CORE_BANK_SIZE);

        assertEquals(150, questions.size());
        assertEquals(150, questions.stream().map(Question::getTitle).distinct().count());
        assertEquals(150, questions.stream()
                .map(question -> question.getTitle().substring(question.getTitle().indexOf(": ") + 2))
                .distinct()
                .count());
        assertQuestionsHaveFourOptionsAndValidAnswers(questions);
        assertEquals(38, questions.stream().filter(question -> question.getCategory().equals("JAVA_OOPS")).count());
        assertEquals(38, questions.stream().filter(question -> question.getCategory().equals("DBMS")).count());
        assertEquals(37, questions.stream().filter(question -> question.getCategory().equals("OS")).count());
        assertEquals(37, questions.stream().filter(question -> question.getCategory().equals("NETWORKS")).count());
    }

    @Test
    void createsOriginalScenarioQuestionsWithTrustedTopicReferences() {
        List<Question> aptitudeQuestions = MockAssessmentCatalog.createAptitudeQuestions(
                null, 1, MockAssessmentCatalog.APTITUDE_BANK_SIZE);
        HashMap<String, com.placify.question.Topic> topics = new HashMap<>();
        MockAssessmentCatalog.CORE_CATEGORIES.forEach(category -> topics.put(category, null));
        List<Question> coreQuestions = MockAssessmentCatalog.createCoreQuestions(
                topics, 1, MockAssessmentCatalog.CORE_BANK_SIZE);

        assertTrue(aptitudeQuestions.get(0).getTitle().contains("successive discounts"));
        assertTrue(aptitudeQuestions.stream().allMatch(question ->
                question.getDescription().startsWith("Original company-style practice question.")
                        && question.getCompanyTags().equals("Amazon,Infosys,Accenture,Google,Microsoft")
                        && question.getExternalLink().startsWith("https://www.indiabix.com/")));
        assertTrue(coreQuestions.get(0).getTitle().contains("volatile"));
        assertTrue(coreQuestions.stream().allMatch(question ->
                question.getDescription().startsWith("Original company-style practice question.")
                        && question.getCompanyTags().equals("Amazon,Infosys,Accenture,Google,Microsoft")
                        && question.getExternalLink().startsWith("https://www.geeksforgeeks.org/")));
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
            assertEquals(4, options.stream().distinct().count(), question.getTitle() + " " + options);
            assertFalse(options.stream().anyMatch(String::isBlank), question.getTitle());
            assertTrue(validOptions.contains(question.getCorrectOption()), question.getTitle());
            assertNotNull(question.getExplanation(), question.getTitle());
            assertFalse(question.getExplanation().isBlank(), question.getTitle());
            assertNotNull(question.getHint(), question.getTitle());
            assertFalse(question.getHint().isBlank(), question.getTitle());
        }
    }
}
