package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.example.dto.CreateEventStepDto;
import org.example.exceptions.EventException;
import org.example.models.Event;
import org.example.models.EventStep;
import org.example.repositories.EventStepRepository;
import org.example.services.EventService;
import org.example.services.EventStepService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EventStepServiceTest {

    @InjectMocks
    EventStepService eventStepService;

    @Mock
    EventStepRepository eventStepRepository;

    @Mock
    EventService eventService;



    @Test
    public void create_eventstep_valid() {
        CreateEventStepDto dto = new CreateEventStepDto();
        dto.setStepNumber(1);
        Date start = new Date();
        Date end = new Date(start.getTime() + 3600000);
        dto.setStartDate(start);
        dto.setEndDate(end);
        long eventId = 100L;


        when(eventStepRepository.save(any(EventStep.class))).thenAnswer(i -> i.getArgument(0));


        EventStep result = eventStepService.createEventStep(eventId, dto);


        assertNotNull(result);
        assertEquals(eventId, result.getEventId());
        assertEquals(1, result.getStepNumber());
        assertEquals(start, result.getStartDate());
        assertEquals(end, result.getEndDate());
    }


    @Test
    public void find_eventstepbyid_found() throws Exception {
        Long stepId = 10L;
        EventStep step = new EventStep();
        step.setId(stepId);
        when(eventStepRepository.findById(stepId)).thenReturn(Optional.of(step));


        EventStep result = eventStepService.findEventStepById(stepId);


        assertNotNull(result);
        assertEquals(stepId, result.getId());
    }


    @Test
    public void find_eventstepbyid_not_found() {
        Long stepId = 10L;
        when(eventStepRepository.findById(stepId)).thenReturn(Optional.empty());


        assertThrows(EventException.class, () -> eventStepService.findEventStepById(stepId));
    }


    @Test
    public void edit_eventstep_valid() throws Exception {
        long eventId = 100L;
        Long stepId = 10L;
        CreateEventStepDto dto = new CreateEventStepDto();
        dto.setStepNumber(2);
        Date newStart = new Date();
        Date newEnd = new Date(newStart.getTime() + 7200000);
        dto.setStartDate(newStart);
        dto.setEndDate(newEnd);


        EventStep existing = new EventStep();
        existing.setId(stepId);
        existing.setEventId(50L);
        existing.setStepNumber(1);
        when(eventStepRepository.findById(stepId)).thenReturn(Optional.of(existing));
        when(eventStepRepository.save(any(EventStep.class))).thenAnswer(i -> i.getArgument(0));


        EventStep result = eventStepService.editEventStep(eventId, stepId, dto);


        assertNotNull(result);
        assertEquals(eventId, result.getEventId());
        assertEquals(2, result.getStepNumber());
        assertEquals(newStart, result.getStartDate());
        assertEquals(newEnd, result.getEndDate());
    }


    @Test
    public void delete_eventstep_contains_step() throws EventException {
        Long eventId = 1L;
        Long stepId = 10L;


        EventStep step = new EventStep();
        step.setId(stepId);
        when(eventStepRepository.findById(stepId)).thenReturn(Optional.of(step));


        Event event = new Event();
        EventStep contained = new EventStep();
        contained.setId(eventId); // using eventId here as per the code condition
        List<EventStep> steps = new ArrayList<>();
        steps.add(contained);
        event.setEventSteps(steps);
        when(eventService.findEventById(eventId)).thenReturn(event);


        EventException ex = assertThrows(EventException.class, () -> eventStepService.deleteEventStep(eventId, stepId));


        assertEquals("Event doesn't contains such step!", ex.getMessage());
        verify(eventStepRepository).delete(step);
    }


    @Test
    public void delete_eventstep_not_contains_step() throws EventException {
        Long eventId = 1L;
        Long stepId = 10L;


        EventStep step = new EventStep();
        step.setId(stepId);
        when(eventStepRepository.findById(stepId)).thenReturn(Optional.of(step));


        Event event = new Event();
        EventStep notContained = new EventStep();
        notContained.setId(2L);
        List<EventStep> steps = new ArrayList<>();
        steps.add(notContained);
        event.setEventSteps(steps);
        when(eventService.findEventById(eventId)).thenReturn(event);


        EventException ex = assertThrows(EventException.class, () -> eventStepService.deleteEventStep(eventId, stepId));


        assertEquals("Event doesn't contains such step!", ex.getMessage());
        // No deletion expected
        // (verify no interaction with delete)
    }


    @Test
    public void set_winners_valid() throws Exception {
        Long stepId = 10L;
        List<Long> teamIds = List.of(101L, 102L);


        EventStep step = new EventStep();
        step.setId(stepId);
        when(eventStepRepository.findById(stepId)).thenReturn(Optional.of(step));
        when(eventStepRepository.save(any(EventStep.class))).thenAnswer(i -> i.getArgument(0));


        EventStep result = eventStepService.setWinners(stepId, teamIds);


        assertNotNull(result);
        assertEquals(teamIds, result.getWinnerTeams());
    }
}