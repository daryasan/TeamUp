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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final EventStepService eventStepService;

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
            @PathVariable Long id,
            @RequestBody EditEventDto editEventDto
    ) throws EventException {
        return ResponseEntity.ok(eventService.editEvent(id, editEventDto));
    }

    @GetMapping("/id={id}")
    public ResponseEntity<Event> findEventById(
            @PathVariable long id
    ) throws EventException {
        return ResponseEntity.ok(eventService.findEventById(id));
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


    @PatchMapping("/participate")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<HttpStatus> participateUser(
            @RequestParam long eventId
    ) throws EventException {
        if (eventService.participateUser(eventId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else throw new EventException("Cannot participate");
    }


    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeamsByEvent(
            @RequestParam long id
    ) {
        return ResponseEntity.ok(eventService.getTeamsByEvent(id));
    }


    @GetMapping("/all/filter")
    public ResponseEntity<List<Event>> getAllEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateEnd
    ) {
        return ResponseEntity.ok(eventService.getEventsByDate(dateStart, dateEnd));
    }


    @PreAuthorize("hasAnyRole('ORGANIZER')")
    @PostMapping("/add-step")
    public ResponseEntity<EventStep> createStep(
            @RequestParam long eventId,
            @RequestBody CreateEventStepDto createEventStepDto
    ) {
        return new ResponseEntity<>(eventStepService.createEventStep(eventId, createEventStepDto), HttpStatus.CREATED);
    }

    @GetMapping("step-id")
    public ResponseEntity<EventStep> getEventStepById(
            @RequestParam long id
    ) throws EventException {
        return ResponseEntity.ok(eventStepService.findEventStepById(id));
    }

    @PatchMapping("edit")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<EventStep> editEventStep(
            @RequestParam long eventId,
            @RequestParam long stepId,
            @RequestBody CreateEventStepDto createEventStepDto
    ) throws EventException {
        return ResponseEntity.ok(eventStepService.editEventStep(eventId, stepId, createEventStepDto));
    }


    @PatchMapping("/delete")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<HttpStatus> deleteEventStep(
            @RequestParam long eventId,
            @RequestParam long stepId
    ) throws EventException {
        eventStepService.deleteEventStep(eventId, stepId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/set-winner")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    public ResponseEntity<EventStep> setWinners(
            @RequestParam long stepId,
            @RequestBody List<Long> teamsIds
    ) throws EventException {
        return ResponseEntity.ok(eventStepService.setWinners(stepId, teamsIds));
    }


}
