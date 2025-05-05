package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.example.dto.ChangeMeetingDto;
import org.example.dto.CreateMeetingDto;
import org.example.dto.UserDetailsFromTokenDto;
import org.example.exceptions.AccessException;
import org.example.exceptions.DataException;
import org.example.exceptions.MeetingException;
import org.example.exceptions.TeamException;
import org.example.models.Meeting;
import org.example.models.Team;
import org.example.repositories.MeetingRepository;
import org.example.services.MeetingService;
import org.example.services.Utils;
import org.example.services.TeamService;
import org.example.security.UserService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MeetingServiceTest {

    @InjectMocks
    MeetingService meetingService;

    @Mock
    MeetingRepository meetingRepository;

    @Mock
    TeamService teamService;

    @Mock
    UserService userService;

    @Mock
    Utils utils;


    @Test
    public void create_meeting_invalid_access_organizer() {
        Long teamId = 1L;
        CreateMeetingDto dto = new CreateMeetingDto();
        dto.setLink("https://meeting.com");
        dto.setStartTime(new Date());
        dto.setEndTime(new Date());


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(100L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(true);


        assertThrows(AccessException.class, () -> meetingService.createMeeting(teamId, dto));
    }


    @Test
    public void create_meeting_invalid_access_not_in_team() throws TeamException {
        Long teamId = 1L;
        CreateMeetingDto dto = new CreateMeetingDto();
        dto.setLink("https://meeting.com");
        dto.setStartTime(new Date());
        dto.setEndTime(new Date());


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(101L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), teamId)).thenReturn(false);


        assertThrows(AccessException.class, () -> meetingService.createMeeting(teamId, dto));
    }


    @Test
    public void create_meeting_invalid_access_not_mentor() throws TeamException {
        Long teamId = 1L;
        CreateMeetingDto dto = new CreateMeetingDto();
        dto.setLink("https://meeting.com");
        dto.setStartTime(new Date());
        dto.setEndTime(new Date());


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(102L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), teamId)).thenReturn(true);
        when(teamService.isMentor(user.getId(), teamId)).thenReturn(false);


        assertThrows(AccessException.class, () -> meetingService.createMeeting(teamId, dto));
    }


    @Test
    public void create_meeting_invalid_data() throws TeamException {
        Long teamId = 1L;
        CreateMeetingDto dto = new CreateMeetingDto();
        dto.setLink("");
        dto.setStartTime(null);
        dto.setEndTime(null);


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(103L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), teamId)).thenReturn(true);
        when(teamService.isMentor(user.getId(), teamId)).thenReturn(true);


        assertThrows(DataException.class, () -> meetingService.createMeeting(teamId, dto));
    }


    @Test
    public void create_meeting_valid() throws Exception {
        Long teamId = 1L;
        CreateMeetingDto dto = new CreateMeetingDto();
        dto.setLink("https://meeting.com");
        Date start = new Date();
        Date end = new Date(start.getTime() + 3600000);
        dto.setStartTime(start);
        dto.setEndTime(end);


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(104L);


        Team team = new Team();
        team.setId(teamId);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(utils.isOrganizer(user)).thenReturn(false);
        when(teamService.isInTeam(user.getId(), teamId)).thenReturn(true);
        when(teamService.isMentor(user.getId(), teamId)).thenReturn(true);
        when(teamService.findTeamById(teamId)).thenReturn(team);
        when(meetingRepository.save(any(Meeting.class))).thenAnswer(i -> i.getArgument(0));


        Meeting result = meetingService.createMeeting(teamId, dto);


        assertNotNull(result);
        assertEquals("https://meeting.com", result.getLink());
        assertEquals(team, result.getTeam());
        assertEquals(start, result.getStartTime());
        assertEquals(end, result.getEndTime());
    }


    @Test
    public void find_meetingbyid_found() throws Exception {
        Long meetingId = 1L;
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);


        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));


        Meeting result = meetingService.findMeetingById(meetingId);


        assertNotNull(result);
        assertEquals(meetingId, result.getId());
    }


    @Test
    public void find_meetingbyid_not_found() {
        Long meetingId = 1L;


        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());


        assertThrows(MeetingException.class, () -> meetingService.findMeetingById(meetingId));
    }


    @Test
    public void change_meeting_valid() throws Exception {
        Long meetingId = 1L;
        ChangeMeetingDto dto = new ChangeMeetingDto();
        dto.setLink("https://newmeeting.com");
        Date newStart = new Date();
        Date newEnd = new Date(newStart.getTime() + 7200000);
        dto.setStartTime(newStart);
        dto.setEndTime(newEnd);


        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Team team = new Team();
        team.setId(2L);
        meeting.setTeam(team);


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(105L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(true);
        when(teamService.isMentor(user.getId(), team.getId())).thenReturn(true);
        when(meetingRepository.save(any(Meeting.class))).thenAnswer(i -> i.getArgument(0));


        Meeting result = meetingService.changeMeeting(meetingId, dto);


        assertNotNull(result);
        assertEquals("https://newmeeting.com", result.getLink());
        assertEquals(newStart, result.getStartTime());
        assertEquals(newEnd, result.getEndTime());
    }


    @Test
    public void change_meeting_invalid_access() throws Exception {
        Long meetingId = 1L;
        ChangeMeetingDto dto = new ChangeMeetingDto();
        dto.setLink("https://newmeeting.com");
        Date newStart = new Date();
        Date newEnd = new Date(newStart.getTime() + 7200000);
        dto.setStartTime(newStart);
        dto.setEndTime(newEnd);


        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Team team = new Team();
        team.setId(2L);
        meeting.setTeam(team);


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(106L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(false);


        assertThrows(AccessException.class, () -> meetingService.changeMeeting(meetingId, dto));
    }


    @Test
    public void delete_meeting_valid() throws Exception {
        Long meetingId = 1L;
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Team team = new Team();
        team.setId(2L);
        meeting.setTeam(team);


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(107L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(true);
        when(teamService.isMentor(user.getId(), team.getId())).thenReturn(true);


        boolean result = meetingService.deleteMeeting(meetingId);


        assertTrue(result);
        verify(meetingRepository).delete(meeting);
    }


    @Test
    public void delete_meeting_invalid_access() throws Exception {
        Long meetingId = 1L;
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Team team = new Team();
        team.setId(2L);
        meeting.setTeam(team);


        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(108L);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(teamService.isInTeam(user.getId(), team.getId())).thenReturn(true);
        when(teamService.isMentor(user.getId(), team.getId())).thenReturn(false);


        boolean result = meetingService.deleteMeeting(meetingId);


        assertFalse(result);
    }


    @Test
    public void get_teammeetings_valid() throws Exception {
        Long teamId = 1L;
        List<Meeting> meetings = new ArrayList<>();
        Meeting meeting1 = new Meeting();
        Meeting meeting2 = new Meeting();
        meetings.add(meeting1);
        meetings.add(meeting2);


        Team team = new Team();
        team.setId(teamId);
        team.setMeetings(meetings);


        when(teamService.findTeamById(teamId)).thenReturn(team);


        List<Meeting> result = meetingService.getTeamMeetings(teamId);


        assertEquals(2, result.size());
    }
}