package org.example.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.CreateEventStepDto;
import org.example.exceptions.EventException;
import org.example.models.EventStep;
import org.example.repositories.EventStepRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class EventStepService {

    private EventStepRepository eventStepRepository;

    @Transactional
    public EventStep createEventStep(long eventId, CreateEventStepDto eventStepDto) {
        EventStep eventStep = new EventStep();
        eventStep.setEventId(eventId);
        eventStep.setStepNumber(eventStepDto.getStepNumber());
        eventStep.setEndDate(eventStepDto.getEndDate());
        eventStep.setStartDate(eventStepDto.getStartDate());

        eventStepRepository.save(eventStep);
        return eventStep;
    }

    public EventStep findEventStepById(Long id) throws EventException {
        Optional<EventStep> eventStepOpt = eventStepRepository.findById(id);
        if (eventStepOpt.isEmpty()) throw new EventException("Event step doesn't exist");
        return eventStepOpt.get();
    }

    public EventStep editEventStep(long eventId, long id, CreateEventStepDto eventStepDto) throws EventException {
        EventStep eventStep = findEventStepById(id);
        eventStep.setEventId(eventId);
        eventStep.setStepNumber(eventStepDto.getStepNumber());
        eventStep.setEndDate(eventStepDto.getEndDate());
        eventStep.setStartDate(eventStepDto.getStartDate());

        eventStepRepository.save(eventStep);
        return eventStep;
    }

    public void deleteEventStep(Long id) throws EventException {
        EventStep eventStep = findEventStepById(id);
        eventStepRepository.delete(eventStep);
    }

    public EventStep setWinners(Long id, List<Long> teams) throws EventException {
        EventStep eventStep = findEventStepById(id);
        eventStep.setWinnersTeamsIds(teams);
        eventStepRepository.save(eventStep);
        return eventStep;
    }

}
