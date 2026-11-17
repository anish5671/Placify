package com.placify.battle;

import com.placify.exception.BadRequestException;
import com.placify.exception.ResourceNotFoundException;
import com.placify.user.User;
import com.placify.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CodingBattleService {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CodingProblemRepository problemRepository;
    private final CodingProblemTestCaseRepository caseRepository;
    private final BattleRoomRepository roomRepository;
    private final UserRepository userRepository;
    private final Judge0Service judge0Service;

    @Transactional(readOnly = true)
    public List<CodingProblemSummaryDto> listProblems() {
        return problemRepository.findAllByOrderByIdAsc().stream()
                .map(problem -> new CodingProblemSummaryDto(problem.getId(), problem.getTitle(), problem.getDifficulty()))
                .toList();
    }

    @Transactional
    public BattleRoomDto createRoom(String username, CreateBattleRoomRequest request) {
        User host = user(username);
        CodingProblem problem = problemRepository.findById(request.problemId())
                .orElseThrow(() -> new ResourceNotFoundException("Coding problem not found"));

        BattleRoom room = BattleRoom.builder()
                .code(newRoomCode())
                .host(host)
                .problem(problem)
                .durationMinutes(request.durationMinutes())
                .status("WAITING")
                .build();
        return toRoomDto(roomRepository.save(room), host);
    }

    @Transactional
    public BattleRoomDto joinRoom(String username, String roomCode) {
        User joiningUser = user(username);
        BattleRoom room = room(roomCode, true);
        expireIfElapsed(room);
        if (room.getHost().getId().equals(joiningUser.getId())) {
            throw new BadRequestException("The room host cannot join their own room as the opponent.");
        }
        if (room.getOpponent() != null) {
            if (room.getOpponent().getId().equals(joiningUser.getId())) {
                return toRoomDto(room, joiningUser);
            }
            throw new BadRequestException("This battle room already has two players.");
        }
        if (!"WAITING".equals(room.getStatus())) {
            throw new BadRequestException("This battle room is no longer waiting for a player.");
        }

        Instant now = Instant.now();
        room.setOpponent(joiningUser);
        room.setStatus("ACTIVE");
        room.setStartedAt(now);
        room.setEndsAt(now.plusSeconds(room.getDurationMinutes() * 60L));
        return toRoomDto(room, joiningUser);
    }

    @Transactional
    public BattleRoomDto getRoom(String username, String roomCode) {
        User currentUser = user(username);
        BattleRoom room = room(roomCode, false);
        requireParticipant(room, currentUser);
        expireIfElapsed(room);
        return toRoomDto(room, currentUser);
    }

    @Transactional
    public BattleExecutionResponse run(String username, String roomCode, CodeExecutionRequest request) {
        User currentUser = user(username);
        BattleRoom room = room(roomCode, false);
        requireActiveParticipant(room, currentUser);

        List<CodingProblemTestCase> visibleCases = caseRepository
                .findByProblemIdOrderByPositionAsc(room.getProblem().getId()).stream()
                .filter(testCase -> !testCase.isHidden())
                .toList();
        if (visibleCases.size() != 2) {
            throw new IllegalStateException("The selected problem must have exactly two visible examples.");
        }
        List<BattleExecutionResponse.TestResult> results = evaluate(request, visibleCases);
        int passed = (int) results.stream().filter(BattleExecutionResponse.TestResult::passed).count();
        boolean accepted = passed == visibleCases.size();
        return new BattleExecutionResponse(
                "RUN", accepted ? "All visible examples passed" : firstFailure(results),
                accepted, passed, visibleCases.size(),
                accepted ? "All visible examples passed." : "Review the visible example results and try again.",
                results, null
        );
    }

    @Transactional
    public BattleExecutionResponse submit(String username, String roomCode, CodeExecutionRequest request) {
        User currentUser = user(username);
        BattleRoom room = room(roomCode, false);
        requireActiveParticipant(room, currentUser);

        List<CodingProblemTestCase> hiddenCases = caseRepository
                .findByProblemIdOrderByPositionAsc(room.getProblem().getId()).stream()
                .filter(CodingProblemTestCase::isHidden)
                .toList();
        if (hiddenCases.size() != 8) {
            throw new IllegalStateException("The selected problem must have exactly eight hidden test cases.");
        }
        int passed = 0;
        String verdict = "Accepted";
        for (CodingProblemTestCase testCase : hiddenCases) {
            Judge0Service.Execution execution = judge0Service.execute(
                    request.sourceCode(), request.language(), testCase.getInput());
            if (!execution.succeeded()) {
                verdict = execution.status();
                break;
            }
            if (!sameOutput(execution.stdout(), testCase.getExpectedOutput())) {
                verdict = "Wrong Answer";
                break;
            }
            passed++;
        }

        if (passed == hiddenCases.size()) {
            BattleRoom lockedRoom = room(roomCode, true);
            expireIfElapsed(lockedRoom);
            requireParticipant(lockedRoom, currentUser);
            if ("EXPIRED".equals(lockedRoom.getStatus())) {
                return new BattleExecutionResponse(
                        "SUBMIT", "Time Limit Exceeded", false, passed, hiddenCases.size(),
                        "All tests finished after the server-controlled battle timer expired.",
                        List.of(), toRoomDto(lockedRoom, currentUser)
                );
            }
            if (lockedRoom.getWinner() == null) {
                lockedRoom.setWinner(currentUser);
                lockedRoom.setStatus("FINISHED");
                lockedRoom.setFinishedAt(Instant.now());
            }
            BattleRoomDto roomDto = toRoomDto(lockedRoom, currentUser);
            boolean winner = currentUser.getId().equals(roomDto.winner().id());
            return new BattleExecutionResponse(
                    "SUBMIT", winner ? "Accepted" : "Accepted (opponent solved first)",
                    true, passed, hiddenCases.size(),
                    winner ? "All hidden tests passed. You won the battle!" : "All hidden tests passed, but your opponent solved it first.",
                    List.of(), roomDto
            );
        }

        BattleRoom latestRoom = room(roomCode, false);
        requireParticipant(latestRoom, currentUser);
        expireIfElapsed(latestRoom);
        BattleRoomDto roomDto = toRoomDto(latestRoom, currentUser);
        return new BattleExecutionResponse(
                "SUBMIT", verdict, false, passed, hiddenCases.size(),
                verdict.equals("Wrong Answer")
                        ? "A hidden test did not pass. Hidden inputs and expected outputs are not shown."
                        : "Execution stopped on a hidden test. Hidden test data is not shown.",
                List.of(), roomDto
        );
    }

    private List<BattleExecutionResponse.TestResult> evaluate(
            CodeExecutionRequest request, List<CodingProblemTestCase> testCases) {
        List<BattleExecutionResponse.TestResult> results = new ArrayList<>();
        for (CodingProblemTestCase testCase : testCases) {
            Judge0Service.Execution execution = judge0Service.execute(
                    request.sourceCode(), request.language(), testCase.getInput());
            boolean passed = execution.succeeded()
                    && sameOutput(execution.stdout(), testCase.getExpectedOutput());
            String verdict = execution.succeeded()
                    ? (passed ? "Passed" : "Wrong Answer")
                    : execution.status();
            String actualOutput = execution.succeeded() ? execution.stdout() : execution.diagnostic();
            results.add(new BattleExecutionResponse.TestResult(
                    testCase.getPosition() + 1,
                    passed,
                    verdict,
                    actualOutput,
                    testCase.getExpectedOutput()
            ));
            if (!execution.succeeded()) break;
        }
        return results;
    }

    private String firstFailure(List<BattleExecutionResponse.TestResult> results) {
        return results.stream().filter(result -> !result.passed())
                .map(BattleExecutionResponse.TestResult::verdict).findFirst().orElse("Wrong Answer");
    }

    private boolean sameOutput(String actual, String expected) {
        return normalize(actual).equals(normalize(expected));
    }

    private String normalize(String value) {
        return Objects.toString(value, "").replace("\r\n", "\n").trim();
    }

    private void requireActiveParticipant(BattleRoom room, User user) {
        requireParticipant(room, user);
        expireIfElapsed(room);
        if (!"ACTIVE".equals(room.getStatus())) {
            throw new BadRequestException("The battle is not active.");
        }
    }

    private void requireParticipant(BattleRoom room, User user) {
        boolean isHost = room.getHost().getId().equals(user.getId());
        boolean isOpponent = room.getOpponent() != null && room.getOpponent().getId().equals(user.getId());
        if (!isHost && !isOpponent) {
            throw new AccessDeniedException("Only the two room participants can view this battle.");
        }
    }

    private void expireIfElapsed(BattleRoom room) {
        if ("ACTIVE".equals(room.getStatus()) && room.getEndsAt() != null
                && !Instant.now().isBefore(room.getEndsAt())) {
            room.setStatus("EXPIRED");
            room.setFinishedAt(Instant.now());
        }
    }

    private CodingProblemDto toProblemDto(CodingProblem problem) {
        List<CodingProblemDto.Example> examples = caseRepository
                .findByProblemIdOrderByPositionAsc(problem.getId()).stream()
                .filter(testCase -> !testCase.isHidden())
                .map(testCase -> new CodingProblemDto.Example(
                        testCase.getPosition() + 1, testCase.getInput(), testCase.getExpectedOutput()))
                .toList();
        return new CodingProblemDto(
                problem.getId(), problem.getTitle(), problem.getDifficulty(), problem.getDescription(),
                problem.getInputFormat(), problem.getOutputFormat(), problem.getConstraints(), examples
        );
    }

    private BattleRoomDto toRoomDto(BattleRoom room, User currentUser) {
        String role = room.getHost().getId().equals(currentUser.getId()) ? "HOST" : "OPPONENT";
        return new BattleRoomDto(
                room.getCode(), room.getStatus(), room.getDurationMinutes(), room.getStartedAt(),
                room.getEndsAt(), room.getFinishedAt(), Instant.now(), participant(room.getHost()),
                participant(room.getOpponent()), participant(room.getWinner()), role, toProblemDto(room.getProblem())
        );
    }

    private BattleRoomDto.Participant participant(User user) {
        if (user == null) return null;
        return new BattleRoomDto.Participant(user.getId(), user.getUsername(), user.getFullName());
    }

    private User user(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private BattleRoom room(String code, boolean lock) {
        String normalizedCode = code.toUpperCase(Locale.ROOT);
        return (lock ? roomRepository.findByCodeForUpdate(normalizedCode) : roomRepository.findByCode(normalizedCode))
                .orElseThrow(() -> new ResourceNotFoundException("Battle room not found"));
    }

    private String newRoomCode() {
        for (int attempt = 0; attempt < 5; attempt++) {
            StringBuilder code = new StringBuilder(8);
            for (int i = 0; i < 8; i++) code.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
            if (roomRepository.findByCode(code.toString()).isEmpty()) return code.toString();
        }
        throw new IllegalStateException("Could not allocate a unique battle room code.");
    }
}
