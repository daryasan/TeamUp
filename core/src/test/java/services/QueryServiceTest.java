package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.dto.UserDetailsFromTokenDto;
import org.example.exceptions.AccessException;
import org.example.exceptions.QueryException;
import org.example.exceptions.TeamException;
import org.example.models.Query;
import org.example.models.QueryStatus;
import org.example.models.Team;
import org.example.repositories.QueryRepository;
import org.example.security.UserService;
import org.example.services.QueryService;
import org.example.services.TeamService;
import org.example.services.Utils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class QueryServiceTest {

    @InjectMocks
    QueryService queryService;

    @Mock
    QueryRepository queryRepository;

    @Mock
    TeamService teamService;

    @Mock
    UserService userService;

    @Mock
    Utils utils;


    @Test
    public void participate_in_team_organizer_throws_access_exception() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(1L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(true);


        Long teamId = 10L;


        assertThrows(AccessException.class, () -> queryService.participateInTeam(teamId));
    }


    @Test
    public void participate_in_team_participant_with_team_throws_team_exception() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(2L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(false);
        when(utils.isParticipant(user)).thenReturn(true);
        when(teamService.hasTeam(user.getId())).thenReturn(true);


        Long teamId = 20L;


        assertThrows(TeamException.class, () -> queryService.participateInTeam(teamId));
    }


    @Test
    public void participate_in_team_valid() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(3L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(false);
        when(utils.isParticipant(user)).thenReturn(false);

        Long teamId = 30L;
        Team team = new Team();
        team.setId(teamId);

        when(teamService.findTeamById(teamId)).thenReturn(team);
        Long leaderId = 100L;
        when(teamService.findLeaderOrMentorId(teamId)).thenReturn(leaderId);
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        Query result = queryService.participateInTeam(teamId);


        assertNotNull(result);
        assertEquals(team, result.getTeam());
        assertEquals(user.getId(), result.getSenderId());
        assertEquals(leaderId, result.getReceiverId());
    }


    @Test
    public void suggest_participation_not_in_team_throws_access_exception() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(4L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Long teamId = 40L;
        when(teamService.isInTeam(user.getId(), teamId)).thenReturn(false);
        Long targetUserId = 200L;


        assertThrows(AccessException.class, () -> queryService.suggestParticipation(teamId, targetUserId));
    }


    @Test
    public void suggest_participation_valid_returns_query() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(5L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Long teamId = 50L;
        when(teamService.isInTeam(user.getId(), teamId)).thenReturn(true);
        Team team = new Team();
        team.setId(teamId);
        when(teamService.findTeamById(teamId)).thenReturn(team);
        Long targetUserId = 300L;
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        Query result = queryService.suggestParticipation(teamId, targetUserId);


        assertNotNull(result);
        assertEquals(team, result.getTeam());
        assertEquals(user.getId(), result.getSenderId());
        assertEquals(targetUserId, result.getReceiverId());
    }


    @Test
    public void accept_decline_by_receiver_query_not_pinging_throws_query_exception() {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(6L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.accepted);
        query.setReceiverId(6L);
        Team team = new Team();
        team.setId(60L);
        query.setTeam(team);

        when(queryRepository.findById(100L)).thenReturn(Optional.of(query));


        assertThrows(QueryException.class, () -> queryService.acceptDeclineByReceiver(100L, true));
    }


    @Test
    public void accept_decline_by_receiver_receiver_mismatch_throws_access_exception() {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(7L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(999L);
        Team team = new Team();
        team.setId(70L);
        query.setTeam(team);

        when(queryRepository.findById(101L)).thenReturn(Optional.of(query));


        assertThrows(AccessException.class, () -> queryService.acceptDeclineByReceiver(101L, true));
    }


    @Test
    public void accept_decline_by_receiver_organizer_throws_access_exception() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(8L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(8L);
        Team team = new Team();
        team.setId(80L);
        query.setTeam(team);

        when(queryRepository.findById(102L)).thenReturn(Optional.of(query));
        when(utils.isOrganizer(user)).thenReturn(true);


        assertThrows(AccessException.class, () -> queryService.acceptDeclineByReceiver(102L, true));
    }


    @Test
    public void accept_decline_by_receiver_not_accepted_returns_declined() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(9L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(9L);
        Team team = new Team();
        team.setId(90L);
        query.setTeam(team);

        when(queryRepository.findById(103L)).thenReturn(Optional.of(query));
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        Query result = queryService.acceptDeclineByReceiver(103L, false);


        assertNotNull(result);
        assertEquals(QueryStatus.declined, result.getQueryStatus());
    }


    @Test
    public void accept_decline_by_receiver_accepted_scenario_adds_participant_for_receiver() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(10L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(10L);
        query.setSenderId(20L);
        Team team = new Team();
        team.setId(100L);
        query.setTeam(team);

        when(queryRepository.findById(104L)).thenReturn(Optional.of(query));
        when(utils.isOrganizer(user)).thenReturn(false);
        when(utils.isParticipant(user)).thenReturn(true);
        when(teamService.hasTeam(user.getId())).thenReturn(false);
        when(teamService.isInTeam(query.getSenderId(), team.getId())).thenReturn(true);
        doNothing().when(teamService).addParticipant(user.getId(), team.getId());
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        Query result = queryService.acceptDeclineByReceiver(104L, true);


        assertNotNull(result);
        assertEquals(QueryStatus.accepted, result.getQueryStatus());
    }


    @Test
    public void accept_decline_by_receiver_accepted_scenario_adds_participant_for_sender() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(11L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(11L);
        query.setSenderId(22L);
        Team team = new Team();
        team.setId(110L);
        query.setTeam(team);

        when(queryRepository.findById(105L)).thenReturn(Optional.of(query));
        when(utils.isOrganizer(user)).thenReturn(false);
        when(utils.isParticipant(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(true);
        when(teamService.hasTeam(query.getSenderId())).thenReturn(false);
        when(utils.isParticipant(query.getSenderId())).thenReturn(true);
        doNothing().when(teamService).addParticipant(query.getSenderId(), team.getId());
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        Query result = queryService.acceptDeclineByReceiver(105L, true);


        assertNotNull(result);
        assertEquals(QueryStatus.accepted, result.getQueryStatus());
    }


    @Test
    public void accept_decline_by_receiver_accepted_scenario_adds_mentor_for_sender() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(12L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(12L);
        query.setSenderId(33L);
        Team team = new Team();
        team.setId(120L);
        query.setTeam(team);

        when(queryRepository.findById(106L)).thenReturn(Optional.of(query));
        when(utils.isOrganizer(user)).thenReturn(false);
        when(utils.isParticipant(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(false);
        when(utils.isMentor(query.getSenderId())).thenReturn(true);
        doNothing().when(teamService).addMentor(query.getSenderId(), team.getId());
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        Query result = queryService.acceptDeclineByReceiver(106L, true);


        assertNotNull(result);
        assertEquals(QueryStatus.accepted, result.getQueryStatus());
    }


    @Test
    public void accept_decline_by_receiver_accepted_scenario_adds_mentor_for_receiver() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(13L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(13L);
        query.setSenderId(44L);
        Team team = new Team();
        team.setId(130L);
        query.setTeam(team);

        when(queryRepository.findById(107L)).thenReturn(Optional.of(query));
        when(utils.isOrganizer(user)).thenReturn(false);
        when(utils.isParticipant(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(false);
        when(utils.isMentor(44L)).thenReturn(true);
        doAnswer(invocation -> {
            team.setMentorId(user.getId());
            team.setHasMentor(true);
            return null;
        }).when(teamService).addMentor(anyLong(), org.mockito.ArgumentMatchers.eq(team.getId()));
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        Query result = queryService.acceptDeclineByReceiver(107L, true);


        assertNotNull(result);
        assertEquals(QueryStatus.accepted, result.getQueryStatus());
        assertEquals(user.getId(), team.getMentorId());
        assertTrue(team.isHasMentor());
    }


    @Test
    public void accept_decline_by_receiver_accepted_bad_query_throws_query_exception() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(14L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setQueryStatus(QueryStatus.pinging);
        query.setReceiverId(14L);
        query.setSenderId(55L);
        Team team = new Team();
        team.setId(140L);
        query.setTeam(team);

        when(queryRepository.findById(108L)).thenReturn(Optional.of(query));
        when(utils.isOrganizer(user)).thenReturn(false);
        when(utils.isParticipant(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(false);
        when(utils.isMentor(query.getSenderId())).thenReturn(false);
        when(utils.isMentor(user)).thenReturn(false);


        assertThrows(QueryException.class, () -> queryService.acceptDeclineByReceiver(108L, true));
    }


    @Test
    public void cancel_by_sender_sender_throws_access_exception() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(15L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setSenderId(15L);
        query.setQueryStatus(QueryStatus.pinging);
        when(queryRepository.findById(109L)).thenReturn(Optional.of(query));
        when(queryRepository.save(any(Query.class))).thenAnswer(i -> i.getArgument(0));


        assertThrows(AccessException.class, () -> queryService.cancelBySender(109L));
    }


    @Test
    public void cancel_by_sender_not_sender_throws_access_exception() throws Exception {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(16L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        Query query = new Query();
        query.setSenderId(999L);
        query.setQueryStatus(QueryStatus.pinging);
        when(queryRepository.findById(110L)).thenReturn(Optional.of(query));


        assertThrows(AccessException.class, () -> queryService.cancelBySender(110L));
    }


    @Test
    public void find_query_by_id_found_returns_query() throws Exception {
        Long queryId = 1L;
        Query query = new Query();
        query.setId(queryId);
        when(queryRepository.findById(queryId)).thenReturn(Optional.of(query));


        Query result = queryService.findQueryById(queryId);


        assertNotNull(result);
        assertEquals(queryId, result.getId());
    }


    @Test
    public void find_query_by_id_not_found_throws_query_exception() {
        Long queryId = 1L;
        when(queryRepository.findById(queryId)).thenReturn(Optional.empty());


        assertThrows(QueryException.class, () -> queryService.findQueryById(queryId));
    }
}