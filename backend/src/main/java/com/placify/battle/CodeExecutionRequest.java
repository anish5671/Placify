package com.placify.battle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CodeExecutionRequest(
        @NotBlank @Size(max = 100000) String sourceCode,
        @NotBlank String language
) {}
