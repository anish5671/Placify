package com.placify.battle;

import java.time.Instant;

public record BattleRoomDto(
        String code,
        String status,
        int durationMinutes,
        Instant startedAt,
        Instant endsAt,
        Instant finishedAt,
        Instant serverNow,
        Participant host,
        Participant opponent,
        Participant winner,
        String currentUserRole,
        CodingProblemDto problem
) {
    public record Participant(Long id, String username, String fullName) {}
}
