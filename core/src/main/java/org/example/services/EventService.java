package org.example.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final Utils utils;

    @Transactional
    public Event createEvent(EventInfoDto eventInfoDto) throws DataException {

        if (eventInfoDto.getWebLink() != null
                && !Utils.verifyWebLink(eventInfoDto.getWebLink())) throw new DataException("Wring link!");

        Event event = new Event();
        event.setName(eventInfoDto.getName());
        event.setDescription(eventInfoDto.getDescription());
        event.setEndDate(eventInfoDto.getEndDate());
        event.setStartDate(eventInfoDto.getStartDate());
        event.setPhotoPath(eventInfoDto.getPhotoPath());
        event.setPrizeDescription(eventInfoDto.getPrizeDescription());
        event.setWebLink(eventInfoDto.getWebLink());

        eventRepository.save(event);

        return event;

    }


    @Transactional
    public Event editEvent(Long id, EditEventDto editEventDto) throws EventException {
        Event event = findEventById(id);

        event.setName(editEventDto.getName());
        event.setDescription(editEventDto.getDescription());
        event.setPhotoPath(editEventDto.getPhotoPath());
        event.setPrizeDescription(editEventDto.getPrizeDescription());
        event.setWebLink(editEventDto.getWebLink());

        eventRepository.save(event);

        event.setTags(event.getTags());
        return event;
    }


    @Transactional
    public Event findEventById(Long id) throws EventException {
        Optional<Event> eventOpt = eventRepository.findById(id);

        if (eventOpt.isEmpty()) throw new EventException("Event doesn't exist");

        return eventOpt.get();
    }

    public List<UserDto> getEventParticipants(Long id) throws EventException {
        Event event = findEventById(id);
        List<Long> userIds = event.getParticipants() == null ? new ArrayList<>() : event.getParticipants();
        List<UserDto> users = new ArrayList<>();

        for (Long userId : userIds) {
            users.add(userService.getUserById(userId));
        }

        return users;

    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public boolean participateUser(Long eventId) {
        Event event;
        try {
            event = findEventById(eventId);
        } catch (EventException e) {
            return false;
        }
        List<Long> participants = event.getParticipants() == null ? new ArrayList<>() : event.getParticipants();


        UserDetailsFromTokenDto userDetailsFromTokenDto = userService.getDetailsFromToken();
        participants.add(userDetailsFromTokenDto.getId());

        event.setParticipants(participants);

        eventRepository.save(event);
        return true;
    }


    public List<Team> getTeamsByEvent(Long eventId) {
        return teamRepository.findAll().stream().filter(t -> t.getEvent().getId() == eventId).toList();
    }

    public List<Event> getEventsByDate(Date start, Date end) {
        List<Event> events = getAllEvents();
        List<Event> res = new ArrayList<>();

        for (Event e : events){
            if (e.getStartDate().after(start) && e.getEndDate().before(end)){
                res.add(e);
            }
        }


        return res;

    }

}
