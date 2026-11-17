package com.placify.question;

import java.util.Locale;

public final class QuestionHint {

    private QuestionHint() {
    }

    public static String forQuestion(Question question) {
        if (question.getHint() != null && !question.getHint().isBlank()) {
            return question.getHint();
        }

        return switch (question.getCategory().toUpperCase(Locale.ROOT)) {
            case "APTITUDE" -> "Identify the relevant formula, write down the known values, and calculate step by step.";
            case "JAVA_OOPS" -> "Recall the Java language rule or API contract being tested and compare the exact terms.";
            case "DBMS" -> "Identify the database concept involved and distinguish it from related SQL commands or constraints.";
            case "OS" -> "Focus on the resource or execution-state change described in the question.";
            case "NETWORKS" -> "Map the behavior to the relevant network layer, protocol, or addressing mechanism.";
            default -> "Break the problem into smaller steps and eliminate choices that do not fit.";
        };
    }
}
