package org.example.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.EditEventDto;
import org.example.dto.EventInfoDto;
import org.example.dto.UserDetailsDto;
import org.example.dto.UserDto;
import org.example.exceptions.DataException;
import org.example.exceptions.EventException;
import org.example.models.Event;
import org.example.models.Team;
import org.example.repositories.EventRepository;
import org.example.repositories.TeamRepository;
import org.example.security.PublicKeyClient;
import org.example.security.PublicKeyService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class EventService {

    private EventRepository eventRepository;
    private RestTemplate restTemplate;
    private TeamRepository teamRepository;
    private PublicKeyService publicKeyService;

    @Transactional
    public Event createEvent(EventInfoDto eventInfoDto) throws DataException {

        if (!Utils.verifyWebLink(eventInfoDto.getWebLink())) throw new DataException("Wring link!");

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


    public Event editEvent(Long id, EditEventDto editEventDto) throws EventException {
        Event event = getEventById(id);

        event.setName(editEventDto.getName());
        event.setDescription(editEventDto.getDescription());
        event.setPhotoPath(editEventDto.getPhotoPath());
        event.setPrizeDescription(editEventDto.getPrizeDescription());
        event.setWebLink(editEventDto.getWebLink());

        eventRepository.save(event);

        return event;
    }


    public Event getEventById(Long id) throws EventException {
        Optional<Event> eventOpt = eventRepository.findById(id);

        if (eventOpt.isEmpty()) throw new EventException("Event doesn't exist");

        return eventOpt.get();
    }

    public List<UserDto> getEventParticipants(Long id) throws EventException {
        Event event = getEventById(id);
        List<Long> userIds = event.getParticipantsId();
        List<UserDto> users = new ArrayList<>();

        for (Long userId : userIds) {
            ResponseEntity<UserDto> responseEntity = restTemplate
                    // TODO set port
                    .getForEntity("http://localhost:8081/auth/id=" + userId,
                            UserDto.class);
            users.add(responseEntity.getBody());
        }

        return users;

    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public boolean participateUser(Long eventId) {
        Event event;
        try {
            event = getEventById(eventId);
        } catch (EventException e) {
            return false;
        }
        List<Long> participants = event.getParticipantsId();


        UserDetailsDto userDetailsDto = publicKeyService.getDetailsFromToken();
        participants.add(userDetailsDto.getId());

        eventRepository.save(event);
        return true;
    }


    public List<Team> getTeamsByEvent(Long eventId) {
        return teamRepository.findAll().stream().filter(t -> t.getEvent().getId() == eventId).toList();
    }

    public List<Event> getEventsByDate(Optional<Date> start, Optional<Date> end) {
        List<Event> events = getAllEvents();
        List<Event> eventsStart;

        eventsStart = start.map(date -> events.stream().filter(
                e -> e.getStartDate().after(date)).toList()).orElse(events);

        List<Event> eventsEnd = eventsStart;
        if (end.isPresent()) {
            eventsEnd = eventsStart.stream().filter(
                    e -> e.getStartDate().before(end.get())).toList();
        }

        return eventsEnd;

    }

}
