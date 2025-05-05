package org.example.services;


import lombok.RequiredArgsConstructor;
import org.example.dto.UserDetailsFromTokenDto;
import org.example.exceptions.EventException;
import org.example.exceptions.TagException;
import org.example.exceptions.TeamException;
import org.example.models.Event;
import org.example.models.Tag;
import org.example.models.Team;
import org.example.repositories.EventRepository;
import org.example.repositories.TagRepository;
import org.example.repositories.TeamRepository;
import org.example.security.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserService userService;
    private final TeamRepository teamRepository;
    private final EventRepository eventRepository;

    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();

        for (Tag t : tagRepository.findAll()) {
            if (t.getAuthorId() == null || t.getAuthorId() == user.getId()) {
                tags.add(t);
            }
        }

        return tags;

    }

    public Tag findTagById(Long id) throws TagException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) throw new TagException("No such tag");
        return tag.get();
    }


    public Tag createTag(String name) {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();

        Tag tag = new Tag();

        tag.setName(name);
        tag.setAuthorId(user.getId());

        tagRepository.save(tag);
        return tag;
    }


    public void assignTagsToTeam(Long teamId, List<Tag> tags) throws TeamException {
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isEmpty()) throw new TeamException("Couldn't find team");

        team.get().setTags(tags);
        teamRepository.save(team.get());
    }


    public void assignTagsToEvent(Long eventId, List<Tag> tags) throws EventException {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) throw new EventException("Couldn't find event");

        event.get().setTags(tags);
        eventRepository.save(event.get());
    }


    public List<Event> findEventsByTags(List<Tag> tags) {
        List<Event> events = new ArrayList<>();

        for (Event e : eventRepository.findAll()) {
            if (new HashSet<>(e.getTags()).containsAll(tags)) {
                events.add(e);
            }
        }

        return events;
    }


    public List<Team> findTeamsByTags(List<Tag> tags) {
        List<Team> teams = new ArrayList<>();

        for (Team team : teamRepository.findAll()) {
            if (new HashSet<>(team.getTags()).containsAll(tags)) {
                teams.add(team);
            }
        }

        return teams;
    }


}
