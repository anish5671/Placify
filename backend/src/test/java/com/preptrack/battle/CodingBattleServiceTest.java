package com.preptrack.battle;

import com.preptrack.user.User;
import com.preptrack.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodingBattleServiceTest {

    @Mock private CodingProblemRepository problemRepository;
    @Mock private CodingProblemTestCaseRepository caseRepository;
    @Mock private BattleRoomRepository roomRepository;
    @Mock private UserRepository userRepository;
    @Mock private Judge0Service judge0Service;
    @InjectMocks private CodingBattleService service;

    private User host;
    private BattleRoom room;
    private List<CodingProblemTestCase> cases;

    @BeforeEach
    void setUp() {
        host = User.builder().id(1L).username("host").fullName("Battle Host").build();
        User opponent = User.builder().id(2L).username("friend").fullName("Battle Friend").build();
        CodingProblem problem = CodingProblem.builder().id(5L).slug("sample").title("Sample")
                .difficulty("EASY").description("Sample description").inputFormat("Input")
                .outputFormat("Output").constraints("Small").build();
        room = BattleRoom.builder().id(1L).code("ROOM1234").host(host).opponent(opponent)
                .problem(problem).durationMinutes(30).status("ACTIVE")
                .startedAt(Instant.now()).endsAt(Instant.now().plusSeconds(1200)).build();
        cases = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cases.add(CodingProblemTestCase.builder().problem(problem).position(i).hidden(i >= 2)
                    .input("case-" + i).expectedOutput("answer-" + i).build());
        }
        when(roomRepository.findByCode("ROOM1234")).thenReturn(Optional.of(room));
    }

    private void stubExecution() {
        when(userRepository.findByUsername("host")).thenReturn(Optional.of(host));
        when(caseRepository.findByProblemIdOrderByPositionAsc(5L)).thenReturn(cases);
        when(judge0Service.execute(anyString(), anyString(), anyString())).thenAnswer(invocation -> {
            String input = invocation.getArgument(2);
            return new Judge0Service.Execution(3, "Accepted", input.replace("case-", "answer-"),
                    "", "", "");
        });
    }

    @Test
    void runEvaluatesAndReturnsOnlyTheTwoVisibleExamples() {
        stubExecution();
        BattleExecutionResponse response = service.run("host", "ROOM1234", new CodeExecutionRequest("code", "python"));

        assertTrue(response.accepted());
        assertEquals(2, response.passedCount());
        assertEquals(2, response.totalCount());
        assertEquals(2, response.results().size());
        verify(judge0Service, times(2)).execute(anyString(), anyString(), anyString());
    }

    @Test
    void hiddenSubmissionWinsAndStoresWinnerOnTheSharedRoom() {
        stubExecution();
        when(roomRepository.findByCodeForUpdate("ROOM1234")).thenReturn(Optional.of(room));
        BattleExecutionResponse response = service.submit("host", "ROOM1234", new CodeExecutionRequest("code", "python"));

        assertTrue(response.accepted());
        assertEquals(8, response.passedCount());
        assertEquals("FINISHED", room.getStatus());
        assertEquals(host, room.getWinner());
        assertEquals("host", response.room().winner().username());
        assertTrue(response.results().isEmpty());
        verify(judge0Service, times(8)).execute(anyString(), anyString(), anyString());
    }

    @Test
    void hiddenFailureDoesNotReturnPrivateInputOrExpectedOutput() {
        stubExecution();
        when(judge0Service.execute(anyString(), anyString(), eq("case-3")))
                .thenReturn(new Judge0Service.Execution(3, "Accepted", "wrong", "", "", ""));

        BattleExecutionResponse response = service.submit("host", "ROOM1234", new CodeExecutionRequest("code", "python"));

        assertFalse(response.accepted());
        assertEquals("Wrong Answer", response.verdict());
        assertEquals(1, response.passedCount());
        assertTrue(response.results().isEmpty());
        assertFalse(response.message().contains("case-"));
        assertFalse(response.message().contains("answer-"));
    }

    @Test
    void roomCannotBeReadByNonParticipants() {
        User intruder = User.builder().id(3L).username("stranger").build();
        when(userRepository.findByUsername("stranger")).thenReturn(Optional.of(intruder));

        assertThrows(AccessDeniedException.class, () -> service.getRoom("stranger", "ROOM1234"));
    }
}
