package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.EventException;
import org.example.exceptions.TagException;
import org.example.exceptions.TeamException;
import org.example.models.Event;
import org.example.models.Tag;
import org.example.models.Team;
import org.example.services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("tags/")
public class TagController {

    private TagService tagService;


    @PostMapping("/create")
    public ResponseEntity<Tag> createTag(
            @RequestBody String name
    ) {
        return new ResponseEntity<>(tagService.createTag(name), HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Tag>> findAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }


    @GetMapping("/id={id}")
    public ResponseEntity<Tag> findTagById(
            @PathVariable Long id
    ) throws TagException {
        return ResponseEntity.ok(tagService.findTagById(id));
    }


    @PatchMapping("assign/teams/id={teamId}")
    public ResponseEntity<HttpStatus> assignTagsToTeam(
            @PathVariable Long teamId,
            @RequestBody List<Tag> tags
    ) throws TeamException {
        tagService.assignTagsToTeam(teamId, tags);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("assign/events/id={eventId}")
    public ResponseEntity<HttpStatus> assignTagsToEvent(
            @PathVariable Long eventId,
            @RequestBody List<Tag> tags
    ) throws EventException {
        tagService.assignTagsToEvent(eventId, tags);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("search/teams")
    public ResponseEntity<List<Team>> findTeamsByTags(
            @RequestBody List<Tag> tags
    ) {
        return ResponseEntity.ok(tagService.findTeamsByTags(tags));
    }

    @GetMapping("search/events")
    public ResponseEntity<List<Event>> findEventsByTags(
            @RequestBody List<Tag> tags
    ) {
        return ResponseEntity.ok(tagService.findEventsByTags(tags));
    }

}
