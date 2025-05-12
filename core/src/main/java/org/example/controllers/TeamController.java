package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exceptions.*;
import org.example.models.Meeting;
import org.example.models.Query;
import org.example.models.Team;
import org.example.services.MeetingService;
import org.example.services.QueryService;
import org.example.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final QueryService queryService;
    private final MeetingService meetingService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Team> createTeam(
            @RequestBody CreateTeamDto createTeamDto
    ) throws EventException, TeamException, AccessException {
        return new ResponseEntity<>(teamService.createTeam(createTeamDto), HttpStatus.CREATED);
    }


    @GetMapping("/id")
    public ResponseEntity<Team> findTeamById(
            @RequestParam Long id
    ) throws TeamException {
        return ResponseEntity.ok(teamService.findTeamById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Team>> findAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }


    @PostMapping("/participate")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> participateInTeam(
            @RequestParam Long teamId
    ) throws TeamException, AccessException {
        return new ResponseEntity<>(queryService.participateInTeam(teamId), HttpStatus.CREATED);
    }


    @PostMapping("/suggest-participation")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> suggestParticipation(
            @RequestParam Long teamId,
            @RequestParam Long userId
    ) throws TeamException, AccessException {
        return new ResponseEntity<>(queryService.suggestParticipation(teamId, userId), HttpStatus.CREATED);
    }


    @GetMapping("query/id")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> findQueryById(
            @RequestParam Long queryId
    ) throws QueryException {
        return ResponseEntity.ok(queryService.findQueryById(queryId));
    }


    @PatchMapping("query/set-status")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> acceptDeclineByReceiver(
            @RequestParam Long queryId,
            @RequestParam boolean status
    ) throws QueryException, TeamException, AccessException {
        return ResponseEntity.ok(queryService.acceptDeclineByReceiver(queryId, status));
    }


    @PatchMapping("query/cancel")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> cancelBySender(
            @RequestParam Long queryId
    ) throws QueryException, AccessException {
        return ResponseEntity.ok(queryService.cancelBySender(queryId));
    }


    @GetMapping("/participants")
    public ResponseEntity<List<UserDto>> getTeamParticipants(
            @RequestParam Long teamId
    ) throws TeamException {
        return ResponseEntity.ok(teamService.getTeamParticipants(teamId));
    }


    @GetMapping("/mentor")
    public ResponseEntity<UserDto> getTeamMentor(
            @RequestParam Long teamId
    ) throws TeamException {
        return ResponseEntity.ok(teamService.getTeamMentor(teamId));
    }

    @PatchMapping("/leave")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Team> leaveTeam(
            @RequestParam Long teamId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.leaveTeam(teamId));
    }


    @PatchMapping("/edit")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Team> editTeam(
            @RequestParam Long teamId,
            @RequestBody EditTeamDto editTeamDto
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.editTeam(teamId, editTeamDto));
    }


    @PatchMapping("delete-user")
    @PreAuthorize("hasAnyRole('PARTICIPANT')")
    public ResponseEntity<Team> deleteParticipantOrMentor(
            @RequestParam Long teamId,
            @RequestParam Long userId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.deleteParticipantOrMentor(teamId, userId));
    }

    @PatchMapping("/change-formed")
    @PreAuthorize("hasAnyRole('PARTICIPANT')")
    public ResponseEntity<Team> reverseStatusFormed(
            @RequestParam Long teamId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.reverseStatusFormed(teamId));
    }


    @PatchMapping("/change-leader")
    @PreAuthorize("hasAnyRole('PARTICIPANT')")
    public ResponseEntity<Team> setNewLeader(
            @RequestParam Long teamId,
            @RequestParam Long newLeaderId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.changeLeader(teamId, newLeaderId));
    }


    @GetMapping("/is-leader")
    public ResponseEntity<HttpStatus> isLeader(
            @RequestParam Long teamId,
            @RequestParam Long userId
    ) throws TeamException {
        if (teamService.isLeader(teamId, userId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/delete/confirm")
    public ResponseEntity<HttpStatus> deleteTeam(
            @RequestParam Long teamId
    ) throws TeamException {
        if (teamService.deleteTeam(teamId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @PostMapping("/meetings/create")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Meeting> createMeeting(
            @RequestParam Long teamId,
            @RequestBody CreateMeetingDto createMeetingDto
    ) throws TeamException, DataException, AccessException {
        return ResponseEntity.ok(meetingService.createMeeting(teamId, createMeetingDto));
    }


    @PatchMapping("/meetings/edit")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Meeting> changeMeeting(
            @RequestParam Long meetingId,
            @RequestBody ChangeMeetingDto changeMeetingDto
    ) throws TeamException, AccessException, MeetingException {
        return ResponseEntity.ok(meetingService.changeMeeting(meetingId, changeMeetingDto));
    }

    @DeleteMapping("/meetings/delete")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<HttpStatus> deleteMeeting(
            @PathVariable Long meetingId
    ) throws TeamException, AccessException, MeetingException {
        if (meetingService.deleteMeeting(meetingId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/meetings/id")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Meeting> getMeetingById(
            @RequestParam Long meetingId
    ) throws MeetingException {
        return ResponseEntity.ok(meetingService.findMeetingById(meetingId));
    }


}
