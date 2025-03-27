package org.example.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.CreateTeamDto;
import org.example.dto.UserDetailsFromTokenDto;
import org.example.exceptions.EventException;
import org.example.exceptions.TeamException;
import org.example.models.Team;
import org.example.repositories.TeamRepository;
import org.example.security.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class TeamService {

    private TeamRepository teamRepository;
    private EventService eventService;
    private UserService userService;
    private Utils utils;


    @Transactional
    public Team createTeam(CreateTeamDto createTeamDto) throws EventException {
        Team team = new Team();
        team.setDescription(createTeamDto.getDescription());
        team.setName(createTeamDto.getName());
        team.setEvent(eventService.findEventById(createTeamDto.getEventId()));
        team.setProjectName(createTeamDto.getProjectName());

        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        // user is PARTICIPANT
        if (utils.isParticipant(user)) {
            team.setLeaderId(user.getId());
            team.getParticipantsId().add(user.getId());
            // user is MENTOR
        } else if (utils.isMentor(user)) {
            team.setMentorId(user.getId());
            team.setStatusHasMentor(true);
        }

        teamRepository.save(team);
        return team;
    }

    public Team findTeamById(Long id) throws TeamException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) throw new TeamException("No such team!");
        else return team.get();
    }


    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }


    public boolean hasTeam(long userId) {
        for (Team team : teamRepository.findAll()) {
            if (team.getParticipantsId().contains(userId)) {
                return true;
            }
        }
        return false;
    }


    public boolean isInTeam(long userId, Long teamId) throws TeamException {
        return findTeamById(teamId).getParticipantsId().contains(userId);
    }

    public Long findLeaderOrMentorId(Long teamId) throws TeamException {
        Team team = findTeamById(teamId);
        Long leaderId = team.getLeaderId();
        if (leaderId != null) return leaderId;
        else if (team.getMentorId() != null) return team.getMentorId();
        else throw new TeamException("Team does not have any mentor or leader");
    }

    public void addParticipant(long userId, long teamId) throws TeamException {
        Team team = findTeamById(teamId);
        team.getParticipantsId().add(userId);
        teamRepository.save(team);
    }

    public void addMentor(long mentorId, long teamId) throws TeamException {
        Team team = findTeamById(teamId);
        team.setMentorId(mentorId);
        team.setStatusHasMentor(true);
        teamRepository.save(team);
    }
}
