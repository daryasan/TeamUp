package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDetailsFromTokenDto;
import org.example.exceptions.AccessException;
import org.example.exceptions.QueryException;
import org.example.exceptions.TeamException;
import org.example.models.Query;
import org.example.models.QueryStatus;
import org.example.models.Team;
import org.example.repositories.QueryRepository;
import org.example.security.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final QueryRepository queryRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final Utils utils;

    @Transactional
    public Query participateInTeam(Long teamId) throws TeamException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();

        if (utils.isOrganizer(user)) throw new AccessException("Organizer cannot participate in a team!");

        // PARTICIPANT hase other team
        if (utils.isParticipant(user) && teamService.hasTeam(user.getId()))
            throw new TeamException("User cannot join new team while being in other team");

        // otherwise all requests are pinging
        Query query = new Query();
        query.setTeam(teamService.findTeamById(teamId));
        query.setSenderId(user.getId());
        query.setReceiverId(teamService.findLeaderOrMentorId(teamId));

        queryRepository.save(query);
        return query;
    }

    @Transactional
    public Query suggestParticipation(Long teamId, Long userId) throws TeamException, AccessException {
        // current user
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();

        // current user (PARTICIPANT or MENTOR) is not in current team
        if (!teamService.isInTeam(user.getId(), teamId))
            throw new AccessException("User who is suggesting to participate does not have a team");

        Query query = new Query();
        query.setTeam(teamService.findTeamById(teamId));
        query.setSenderId(user.getId());
        query.setReceiverId(userId);


        queryRepository.save(query);
        return query;

    }

    @Transactional
    public Query acceptDeclineByReceiver(Long queryId, boolean isAccepted) throws QueryException, TeamException, AccessException {
        // current user
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Query query = findQueryById(queryId);
        Team team = query.getTeam();

        if (query.getQueryStatus() != QueryStatus.pinging) throw new QueryException("Action only available for pinging queries");
        if (!(query.getReceiverId() == user.getId())) throw new AccessException("Current user is not receiver");
        if (utils.isOrganizer(user)) throw new AccessException("Organizer can't access team queries");

        // if query not accepted = nothing happens
        if (!isAccepted) {
            query.setQueryStatus(QueryStatus.declined);
            queryRepository.save(query);
            return query;
        } else {
            // query was from TEAM to PARTICIPANT
            // user becomes PARTICIPANT of the team associated with query
            if (utils.isParticipant(user) && !teamService.hasTeam(user.getId()) &&
                    teamService.isInTeam(query.getSenderId(), team.getId())) {
                teamService.addParticipant(user.getId(), team.getId());
            }

            // query was from PARTICIPANT to TEAM
            else if (teamService.isInTeam(user.getId(), team.getId())
                    && !teamService.hasTeam(query.getSenderId()) && utils.isParticipant(query.getSenderId())) {
                teamService.addParticipant(query.getSenderId(), team.getId());
            }

            // query was from MENTOR to TEAM
            else if (utils.isMentor(query.getSenderId())) {
                teamService.addMentor(query.getSenderId(), team.getId());
            }

            // query was from TEAM to MENTOR
            // user becomes MENTOR
            else if (utils.isMentor(user)) {
                team.setMentorId(user.getId());
                team.setHasMentor(true);
            } else {
                throw new QueryException("Bad query");
            }

            query.setQueryStatus(QueryStatus.accepted);
            queryRepository.save(query);
            return query;
        }

    }


    public Query cancelBySender(Long queryId) throws QueryException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Query query = findQueryById(queryId);

        if (user.getId() == query.getSenderId()) {
            query.setQueryStatus(QueryStatus.cancelled);
            queryRepository.save(query);
        }
        throw new AccessException("User is not sender!");
    }

    public Query findQueryById(Long queryId) throws QueryException {
        Optional<Query> query = queryRepository.findById(queryId);
        if (query.isEmpty()) throw new QueryException("No such query found");
        else return query.get();
    }


}
