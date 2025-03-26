package org.example.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.CreateEventStepDto;
import org.example.dto.EditEventDto;
import org.example.dto.EventInfoDto;
import org.example.dto.UserDto;
import org.example.exceptions.DataException;
import org.example.exceptions.EventException;
import org.example.models.Event;
import org.example.models.EventStep;
import org.example.models.Team;
import org.example.services.EventService;
import org.example.services.EventStepService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private EventService eventService;
    private EventStepService eventStepService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<Event> createEvent(
            @RequestBody EventInfoDto eventInfoDto
    ) throws DataException {
        return new ResponseEntity<>(eventService.createEvent(eventInfoDto), HttpStatus.CREATED);
    }

    @PatchMapping("/id={id}/edit")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<Event> editEvent(
            @PathVariable long id,
            @RequestBody EditEventDto editEventDto
    ) throws EventException {
        return ResponseEntity.ok(eventService.editEvent(id, editEventDto));
    }

    @GetMapping("/id={id}")
    public ResponseEntity<Event> getEventById(
            @PathVariable long id
    ) throws EventException {
        return ResponseEntity.ok(eventService.getEventById(id));
    }


    @GetMapping("id={id}/get-participants")
    public ResponseEntity<List<UserDto>> getEventParticipants(
            @PathVariable long id
    ) throws EventException {
        return ResponseEntity.ok(eventService.getEventParticipants(id));
    }


    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }


    @PatchMapping("/id={id}/participate")
    @PreAuthorize("hasAnyRole('PARTICIPANT')")
    public ResponseEntity<HttpStatus> participateUser(
            @PathVariable long eventId
    ) throws EventException {
        if (eventService.participateUser(eventId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else throw new EventException("Cannot participate");
    }


    @GetMapping("id={id}/teams")
    public ResponseEntity<List<Team>> getTeamsByEvent(
            @PathVariable long id
    ) {
        return ResponseEntity.ok(eventService.getTeamsByEvent(id));
    }


    @GetMapping("/all/filter?dateStart={dateStart}&dateEnd={dateEnd}")
    public ResponseEntity<List<Event>> getAllEvents(
            @PathVariable Optional<Date> dateStart,
            @PathVariable Optional<Date> dateEnd
    ) {
        return ResponseEntity.ok(eventService.getEventsByDate(dateStart, dateEnd));
    }


    @PostMapping("id={eventId}/add-step")
    public ResponseEntity<EventStep> createStep(
            @PathVariable long eventId,
            @RequestBody CreateEventStepDto createEventStepDto
    ) {
        return new ResponseEntity<>(eventStepService.createEventStep(eventId, createEventStepDto), HttpStatus.CREATED);
    }

    @GetMapping("id={eventId}/step-id={id}")
    public ResponseEntity<EventStep> getEventStepById(
            @PathVariable long id
    ) throws EventException {
        return ResponseEntity.ok(eventStepService.findEventStepById(id));
    }

    @PatchMapping("id={eventId}/step-id={id}/edit")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<EventStep> editEventStep(
            @PathVariable long eventId,
            @PathVariable long id,
            @RequestBody CreateEventStepDto createEventStepDto
    ) throws EventException {
        return ResponseEntity.ok(eventStepService.editEventStep(eventId, id, createEventStepDto));
    }


    @PatchMapping("id={eventId}/step-id={id}/delete")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<HttpStatus> deleteEventStep(
            @PathVariable long eventId,
            @PathVariable long id
    ) throws EventException {
        eventStepService.deleteEventStep(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("id={eventId}/step-id={id}/set-winner")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<EventStep> setWinners(
            @PathVariable long id,
            @RequestBody List<Long> teamsIds
    ) throws EventException {
        return ResponseEntity.ok(eventStepService.setWinners(id, teamsIds));
    }


}
