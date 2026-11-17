package com.placify.battle;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBattleRoomRequest(
        @NotNull Long problemId,
        @Min(5) @Max(120) int durationMinutes
) {}
