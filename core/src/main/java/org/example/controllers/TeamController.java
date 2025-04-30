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

    private TeamService teamService;
    private QueryService queryService;
    private MeetingService meetingService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Team> createTeam(
            @RequestBody CreateTeamDto createTeamDto
    ) throws EventException, TeamException, AccessException {
        return new ResponseEntity<>(teamService.createTeam(createTeamDto), HttpStatus.CREATED);
    }


    @GetMapping("/id={id}")
    public ResponseEntity<Team> findTeamById(
            @PathVariable Long id
    ) throws TeamException {
        return ResponseEntity.ok(teamService.findTeamById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Team>> findAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }


    @PostMapping("/id={teamId}/participate")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> participateInTeam(
            @PathVariable Long teamId
    ) throws TeamException, AccessException {
        return new ResponseEntity<>(queryService.participateInTeam(teamId), HttpStatus.CREATED);
    }


    @PostMapping("/id={teamId}/suggest-participation/userId={userId}")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> suggestParticipation(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) throws TeamException, AccessException {
        return new ResponseEntity<>(queryService.suggestParticipation(teamId, userId), HttpStatus.CREATED);
    }


    @GetMapping("query/id={id}")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> findQueryById(
            @PathVariable Long queryId
    ) throws QueryException {
        return ResponseEntity.ok(queryService.findQueryById(queryId));
    }


    @PatchMapping("query/id={id}/set-status={status}")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> acceptDeclineByReceiver(
            @PathVariable Long queryId,
            @PathVariable boolean status
    ) throws QueryException, TeamException, AccessException {
        return ResponseEntity.ok(queryService.acceptDeclineByReceiver(queryId, status));
    }


    @PatchMapping("query/id={id}/cancel")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> cancelBySender(
            @PathVariable Long queryId
    ) throws QueryException, AccessException {
        return ResponseEntity.ok(queryService.cancelBySender(queryId));
    }


    @GetMapping("/id={teamId}/participants")
    public ResponseEntity<List<UserDto>> getTeamParticipants(
            @PathVariable Long teamId
    ) throws TeamException {
        return ResponseEntity.ok(teamService.getTeamParticipants(teamId));
    }


    @GetMapping("/id={teamId}/mentor")
    public ResponseEntity<UserDto> getTeamMentor(
            @PathVariable Long teamId
    ) throws TeamException {
        return ResponseEntity.ok(teamService.getTeamMentor(teamId));
    }

    @PatchMapping("/id={teamId}/leave")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Team> leaveTeam(
            @PathVariable Long teamId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.leaveTeam(teamId));
    }


    @PatchMapping("id={teamId}/edit")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Team> editTeam(
            @PathVariable Long teamId,
            @RequestBody EditTeamDto editTeamDto
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.editTeam(teamId, editTeamDto));
    }


    @PatchMapping("id={teamId}/delete/user={userId}")
    @PreAuthorize("hasAnyRole('PARTICIPANT')")
    public ResponseEntity<Team> deleteParticipantOrMentor(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.deleteParticipantOrMentor(teamId, userId));
    }

    @PatchMapping("id={teamId}/change-formed")
    @PreAuthorize("hasAnyRole('PARTICIPANT')")
    public ResponseEntity<Team> reverseStatusFormed(
            @PathVariable Long teamId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.reverseStatusFormed(teamId));
    }


    @PatchMapping("id={teamId}/change-leader/id={newLeaderId}")
    @PreAuthorize("hasAnyRole('PARTICIPANT')")
    public ResponseEntity<Team> setNewLeader(
            @PathVariable Long teamId,
            @PathVariable Long newLeaderId
    ) throws TeamException, AccessException {
        return ResponseEntity.ok(teamService.changeLeader(teamId, newLeaderId));
    }


    @GetMapping("id={teamId}/is-leader/id={userId}")
    public ResponseEntity<HttpStatus> isLeader(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) throws TeamException {
        if (teamService.isLeader(teamId, userId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("id={teamId}/delete/confirm")
    public ResponseEntity<HttpStatus> deleteTeam(
            @PathVariable Long teamId
    ) throws TeamException {
        if (teamService.deleteTeam(teamId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @PostMapping("id={teamId}/meeting/create")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Meeting> createMeeting(
            @PathVariable Long teamId,
            @RequestBody CreateMeetingDto createMeetingDto
    ) throws TeamException, DataException, AccessException {
        return ResponseEntity.ok(meetingService.createMeeting(teamId, createMeetingDto));
    }


    @PatchMapping("meetings/id={meetingId}/edit")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Meeting> changeMeeting(
            @PathVariable Long meetingId,
            @RequestBody ChangeMeetingDto changeMeetingDto
    ) throws TeamException, AccessException, MeetingException {
        return ResponseEntity.ok(meetingService.changeMeeting(meetingId, changeMeetingDto));
    }

    @DeleteMapping("meetings/id={meetingId}/delete")
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


    @GetMapping("meetings/id={meetingId}")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Meeting> getMeetingById(
            @PathVariable Long meetingId
    ) throws MeetingException {
        return ResponseEntity.ok(meetingService.findMeetingById(meetingId));
    }


}
