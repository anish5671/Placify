package com.placify.battle;

import java.util.List;

public record BattleExecutionResponse(
        String action,
        String verdict,
        boolean accepted,
        int passedCount,
        int totalCount,
        String message,
        List<TestResult> results,
        BattleRoomDto room
) {
    public record TestResult(
            int exampleNumber,
            boolean passed,
            String verdict,
            String actualOutput,
            String expectedOutput
    ) {}
}
