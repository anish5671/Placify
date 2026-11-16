package com.preptrack.battle;

import com.preptrack.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
public class CodingBattleController {

    private final CodingBattleService battleService;

    @GetMapping("/problems")
    public ApiResponse<List<CodingProblemSummaryDto>> listProblems() {
        return ApiResponse.success(battleService.listProblems());
    }

    @PostMapping("/rooms")
    public ApiResponse<BattleRoomDto> createRoom(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody CreateBattleRoomRequest request) {
        return ApiResponse.success("Battle room created", battleService.createRoom(user.getUsername(), request));
    }

    @PostMapping("/rooms/join")
    public ApiResponse<BattleRoomDto> joinRoom(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody JoinBattleRoomRequest request) {
        return ApiResponse.success("Joined battle room", battleService.joinRoom(user.getUsername(), request.code()));
    }

    @GetMapping("/rooms/{code}")
    public ApiResponse<BattleRoomDto> getRoom(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String code) {
        return ApiResponse.success(battleService.getRoom(user.getUsername(), code));
    }

    @PostMapping("/rooms/{code}/run")
    public ApiResponse<BattleExecutionResponse> run(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String code,
            @Valid @RequestBody CodeExecutionRequest request) {
        return ApiResponse.success("Visible example run complete",
                battleService.run(user.getUsername(), code, request));
    }

    @PostMapping("/rooms/{code}/submit")
    public ApiResponse<BattleExecutionResponse> submit(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String code,
            @Valid @RequestBody CodeExecutionRequest request) {
        return ApiResponse.success("Submission complete",
                battleService.submit(user.getUsername(), code, request));
    }
}
