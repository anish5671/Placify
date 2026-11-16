package com.preptrack.battle;

import java.util.List;

public record CodingProblemDto(
        Long id,
        String title,
        String difficulty,
        String description,
        String inputFormat,
        String outputFormat,
        String constraints,
        List<Example> examples
) {
    public record Example(int number, String input, String output) {}
}
