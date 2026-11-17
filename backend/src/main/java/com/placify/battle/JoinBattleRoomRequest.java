package com.placify.battle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record JoinBattleRoomRequest(
        @NotBlank @Pattern(regexp = "[A-Z0-9]{6,10}") String code
) {}
