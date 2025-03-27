package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.ocsp.Req;
import org.example.dto.CreateTeamDto;
import org.example.exceptions.AuthException;
import org.example.exceptions.EventException;
import org.example.exceptions.QueryException;
import org.example.exceptions.TeamException;
import org.example.models.Query;
import org.example.models.Team;
import org.example.services.QueryService;
import org.example.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private TeamService teamService;
    private QueryService queryService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Team> createTeam(
            @RequestBody CreateTeamDto createTeamDto
    ) throws EventException {
        return new ResponseEntity<>(teamService.createTeam(createTeamDto), HttpStatus.CREATED);
    }


    @GetMapping("/id={id}")
    public ResponseEntity<Team> findTeamById(
            @PathVariable Long id
    ) throws TeamException {
        return ResponseEntity.ok(teamService.findTeamById(id));
    }


    @PostMapping("/id={teamId}/participate")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> participateInTeam(
            @PathVariable Long teamId
    ) throws TeamException {
        return new ResponseEntity<>(queryService.participateInTeam(teamId), HttpStatus.CREATED);
    }


    @PostMapping("/id={teamId}/suggest-participation/userId={userId}")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'MENTOR')")
    public ResponseEntity<Query> suggestParticipation(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) throws TeamException {
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
    ) throws QueryException, AuthException {
        return ResponseEntity.ok(queryService.acceptDeclineByReceiver(queryId, status));
    }


}
