package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.example.dto.EditEventDto;
import org.example.dto.EventInfoDto;
import org.example.dto.UserDetailsFromTokenDto;
import org.example.dto.UserDto;
import org.example.exceptions.DataException;
import org.example.exceptions.EventException;
import org.example.models.Event;
import org.example.models.Team;
import org.example.repositories.EventRepository;
import org.example.repositories.TeamRepository;
import org.example.security.UserService;
import org.example.services.EventService;
import org.example.services.Utils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EventServiceTest {

    @InjectMocks
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    UserService userService;

    @Mock
    Utils utils;


    @Test
    public void create_event_invalid_weblink() {
        EventInfoDto dto = new EventInfoDto();
        dto.setName("test event");
        dto.setDescription("desc");
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        dto.setPhotoPath("path");
        dto.setPrizeDescription("prize");
        dto.setWebLink("invalid-link");


        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyWebLink("invalid-link")).thenReturn(false);


            assertThrows(DataException.class, () -> eventService.createEvent(dto));


            utilsMock.verify(() -> Utils.verifyWebLink("invalid-link"));
        }
    }


    @Test
    public void create_event_valid() throws Exception {
        EventInfoDto dto = new EventInfoDto();
        dto.setName("test event");
        dto.setDescription("desc");
        Date start = new Date();
        Date end = new Date(start.getTime() + 3600000);
        dto.setStartDate(start);
        dto.setEndDate(end);
        dto.setPhotoPath("path");
        dto.setPrizeDescription("prize");
        dto.setWebLink("valid-link");


        try (MockedStatic<Utils> utilsMock = org.mockito.Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyWebLink("valid-link")).thenReturn(true);


            Event event = new Event();
            event.setName(dto.getName());
            event.setDescription(dto.getDescription());
            event.setStartDate(dto.getStartDate());
            event.setEndDate(dto.getEndDate());
            event.setPhotoPath(dto.getPhotoPath());
            event.setPrizeDescription(dto.getPrizeDescription());
            event.setWebLink(dto.getWebLink());
            when(eventRepository.save(any(Event.class))).thenReturn(event);


            Event result = eventService.createEvent(dto);


            assertNotNull(result);
            assertEquals("test event", result.getName());
            assertEquals("desc", result.getDescription());
            assertEquals("path", result.getPhotoPath());
            utilsMock.verify(() -> Utils.verifyWebLink("valid-link"));
        }
    }


    @Test
    public void edit_event_valid() throws Exception {
        Long eventId = 1L;
        EditEventDto dto = new EditEventDto();
        dto.setName("edited event");
        dto.setDescription("edited desc");
        dto.setPhotoPath("newpath");
        dto.setPrizeDescription("newprize");
        dto.setWebLink("newlink");


        Event existing = new Event();
        existing.setName("old event");
        existing.setDescription("olddesc");
        existing.setPhotoPath("oldpath");
        existing.setPrizeDescription("oldprize");
        existing.setWebLink("oldlink");
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));


        Event result = eventService.editEvent(eventId, dto);


        assertEquals("edited event", result.getName());
        assertEquals("edited desc", result.getDescription());
        assertEquals("newpath", result.getPhotoPath());
        assertEquals("newprize", result.getPrizeDescription());
        assertEquals("newlink", result.getWebLink());
    }


    @Test
    public void find_eventbyid_found() throws Exception {
        Long eventId = 1L;
        Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));


        Event result = eventService.findEventById(eventId);


        assertNotNull(result);
    }


    @Test
    public void find_eventbyid_notfound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());


        assertThrows(EventException.class, () -> eventService.findEventById(eventId));
    }


    @Test
    public void get_event_participants_valid() throws Exception {
        Long eventId = 1L;
        Event event = new Event();
        List<Long> participantIds = new ArrayList<>();
        participantIds.add(10L);
        participantIds.add(20L);
        event.setParticipants(participantIds);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));


        UserDto user1 = new UserDto();
        user1.setId(10L);
        user1.setEmail("user1@example.com");
        UserDto user2 = new UserDto();
        user2.setId(20L);
        user2.setEmail("user2@example.com");
        when(utils.getUserById(10L)).thenReturn(new ResponseEntity<>(user1, HttpStatus.OK));
        when(utils.getUserById(20L)).thenReturn(new ResponseEntity<>(user2, HttpStatus.OK));


        List<UserDto> result = eventService.getEventParticipants(eventId);


        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());
    }


    @Test
    public void get_all_events() {
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        events.add(new Event());
        when(eventRepository.findAll()).thenReturn(events);


        List<Event> result = eventService.getAllEvents();


        assertEquals(2, result.size());
    }


    @Test
    public void participate_user_valid() throws Exception {
        Long eventId = 1L;
        Event event = new Event();
        event.setParticipants(new ArrayList<>());
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));


        UserDetailsFromTokenDto userDetails = new UserDetailsFromTokenDto();
        userDetails.setId(100L);
        when(userService.getDetailsFromToken()).thenReturn(userDetails);
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));


        boolean result = eventService.participateUser(eventId);


        assertTrue(result);
        assertEquals(1, event.getParticipants().size());
        assertEquals(100L, event.getParticipants().get(0).longValue());
    }


    @Test
    public void participate_user_event_not_found() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());


        boolean result = eventService.participateUser(eventId);


        assertFalse(result);
    }


    @Test
    public void get_teams_by_event_valid() {
        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);
        Team team1 = new Team();
        team1.setEvent(event);
        Team team2 = new Team();
        Event otherEvent = new Event();
        otherEvent.setId(2L);
        team2.setEvent(otherEvent);
        List<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        when(teamRepository.findAll()).thenReturn(teams);


        List<Team> result = eventService.getTeamsByEvent(eventId);


        assertEquals(1, result.size());
        assertEquals(eventId, result.get(0).getEvent().getId());
    }


    @Test
    public void get_events_by_date_all() {
        List<Event> events = new ArrayList<>();
        Event event1 = new Event();
        Date now = new Date();
        event1.setStartDate(now);
        events.add(event1);
        when(eventRepository.findAll()).thenReturn(events);


        List<Event> result = eventService.getEventsByDate(Optional.empty(), Optional.empty());


        assertEquals(1, result.size());
    }


    @Test
    public void get_events_by_date_start_only() {
        List<Event> events = new ArrayList<>();
        Date now = new Date();
        Event event1 = new Event();
        event1.setStartDate(new Date(now.getTime() + 10000));
        Event event2 = new Event();
        event2.setStartDate(new Date(now.getTime() - 10000));
        events.add(event1);
        events.add(event2);
        when(eventRepository.findAll()).thenReturn(events);


        List<Event> result = eventService.getEventsByDate(Optional.of(now), Optional.empty());


        assertEquals(1, result.size());
        assertTrue(result.get(0).getStartDate().after(now));
    }


    @Test
    public void get_events_by_date_start_and_end() {
        List<Event> events = new ArrayList<>();
        Date now = new Date();
        Event event1 = new Event();
        event1.setStartDate(new Date(now.getTime() + 10000));
        Event event2 = new Event();
        event2.setStartDate(new Date(now.getTime() + 20000));
        Event event3 = new Event();
        event3.setStartDate(new Date(now.getTime() + 30000));
        events.add(event1);
        events.add(event2);
        events.add(event3);
        when(eventRepository.findAll()).thenReturn(events);
        Date start = new Date(now.getTime() + 15000);
        Date end = new Date(now.getTime() + 25000);


        List<Event> result = eventService.getEventsByDate(Optional.of(start), Optional.of(end));


        assertEquals(1, result.size());
        assertTrue(result.get(0).getStartDate().after(start));
        assertTrue(result.get(0).getStartDate().before(end));
    }
}
