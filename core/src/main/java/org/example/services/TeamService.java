package org.example.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exceptions.AccessException;
import org.example.exceptions.EventException;
import org.example.exceptions.TeamException;
import org.example.models.Team;
import org.example.repositories.TeamRepository;
import org.example.security.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final EventService eventService;
    private final UserService userService;
    private final Utils utils;


    @Transactional
    public Team createTeam(CreateTeamDto createTeamDto) throws EventException, TeamException, AccessException {
        Team team = new Team();
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();

        if (utils.isOrganizer(user)) throw new AccessException("Organizer cannot create team!");
        if (createTeamDto.getName() == null || createTeamDto.getName().isEmpty())
            throw new TeamException("Cant set empty name");

        team.setDescription(createTeamDto.getDescription());
        team.setName(createTeamDto.getName());
        team.setEvent(eventService.findEventById(createTeamDto.getEventId()));
        team.setProjectName(createTeamDto.getProjectName());
        team.setHasMentor(false);
        team.setFormed(false);
        if (team.getParticipants() == null) team.setParticipants(new ArrayList<>());

        // user is PARTICIPANT
        if (utils.isParticipant(user)) {
            team.setLeaderId(user.getId());
            team.getParticipants().add(user.getId());
            // user is MENTOR
        } else if (utils.isMentor(user)) {
            team.setMentorId(user.getId());
            team.setHasMentor(true);
        }

        teamRepository.save(team);
        return team;
    }

    @Transactional
    public TeamDto findTeamDtoById(Long id) throws TeamException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) throw new TeamException("No such team!");
        else return teamToTeamDto(team.get());
    }

    @Transactional
    public Team findTeamById(Long id) throws TeamException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) throw new TeamException("No such team!");
        else return team.get();
    }


    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }


    public List<UserDto> getTeamParticipants(Long teamId) throws TeamException {
        Team team = findTeamById(teamId);
        List<UserDto> participants = new ArrayList<>();

        for (Long id : team.getParticipants()) {
            participants.add(userService.getUserById(id));
        }

        return participants;
    }


    public UserDto getTeamMentor(Long teamId) throws TeamException {
        Team team = findTeamById(teamId);

        if (team.getMentorId() != null) return userService.getUserById(team.getMentorId());
        else throw new TeamException("Team does not have a mentor");
    }


    public void leaveTeam(Long teamId) throws TeamException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Team team = findTeamById(teamId);

        if (utils.isMentor(user)) {
            deleteMentor(team);

        } else if (utils.isParticipant(user)) {

            if (team.getLeaderId() == user.getId()) {
                throw new AccessException("To leave team leader must set another leader first");
            } else {

                deleteParticipant(team, user.getId());

            }

        }
    }


    public TeamDto editTeam(Long teamId, EditTeamDto editTeamDto) throws TeamException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        if (!isInTeam(user.getId(), teamId)) {
            throw new AccessException("User cannot edit this team");
        }
        if (editTeamDto.getName() == null || editTeamDto.getName().isEmpty())
            throw new TeamException("Cant set empty name");

        Team team = findTeamById(teamId);
        team.setName(editTeamDto.getName());
        team.setDescription(editTeamDto.getDescription());
        team.setProjectName(editTeamDto.getProjectName());

        teamRepository.save(team);
        return teamToTeamDto(team);
    }


    @Transactional
    public void deleteParticipantOrMentor(Long teamId, Long userId) throws TeamException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Team team = findTeamById(teamId);

        // only leader can delete anyone
        if (team.getLeaderId() == user.getId()) {
            if (utils.isMentor(userId)) {
                deleteMentor(team);
            } else if (isInTeam(userId, teamId)) {
                deleteParticipant(team, userId);
            }
        } else throw new AccessException("Current user is not the leader of a team");

    }


    @Transactional
    public TeamDto reverseStatusFormed(Long teamId) throws TeamException, AccessException {

        Team team = findTeamById(teamId);
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();

        if (isInTeam(user.getId(), teamId) || isMentor(user.getId(), teamId)) {
            team.setFormed(!team.isFormed());
            teamRepository.save(team);
            return teamToTeamDto(team);
        } else throw new AccessException("User not in team cannot modify team status");
    }


    public void changeLeader(Long teamId, Long newLeaderId) throws TeamException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Team team = findTeamById(teamId);


        if (isLeader(teamId, user.getId()) && isInTeam(newLeaderId, teamId)) {
            team.setLeaderId(newLeaderId);
        } else throw new AccessException("Only leader can set team participant as a new leader");

    }

    public boolean isLeader(Long teamId, Long userId) throws TeamException {
        return Objects.equals(findTeamById(teamId).getLeaderId(), userId);
    }


    public boolean deleteTeam(Long teamId) throws TeamException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Team team = findTeamById(teamId);

        if (isLeader(teamId, user.getId())) {
            teamRepository.delete(team);
            return true;
        }
        return false;
    }


    public boolean hasTeam(long userId) {
        for (Team team : teamRepository.findAll()) {
            if ((team.getParticipants() != null && team.getParticipants().contains(userId))) {
                return true;
            }
        }
        return false;
    }

    public boolean isInTeam(long userId, Long teamId) throws TeamException {
        return findTeamDtoById(teamId).getParticipants() != null &&
                findTeamDtoById(teamId).getParticipants().contains(userId);
    }

    public boolean isMentor(long userId, Long teamId) throws TeamException {
        return findTeamById(teamId).getMentorId() == userId;
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
        team.getParticipants().add(userId);
        teamRepository.save(team);
    }

    public void addMentor(long mentorId, long teamId) throws TeamException {
        Team team = findTeamById(teamId);
        team.setMentorId(mentorId);
        team.setHasMentor(true);
        teamRepository.save(team);
    }


    private void deleteMentor(Team team) {
        team.setMentorId(null);
        team.setHasMentor(false);
        teamRepository.save(team);
    }

    private void deleteParticipant(Team team, Long participantId) {
        List<Long> participants = team.getParticipants();

        for (Long id : team.getParticipants()) {
            if (Objects.equals(id, participantId)) {
                participants.remove(id);
                break;
            }
        }

        team.setParticipants(participants);
        teamRepository.save(team);
    }

    private TeamDto teamToTeamDto(Team team){
        TeamDto teamDto = new TeamDto();
        teamDto.setParticipants(team.getParticipants());
        teamDto.setDescription(team.getDescription());
        teamDto.setId(team.getId());
        teamDto.setFormed(team.isFormed());
        teamDto.setName(team.getName());
        teamDto.setHasMentor(team.isHasMentor());
        teamDto.setEventId(team.getEvent().getId());
        teamDto.setTags(team.getTags());
        teamDto.setLeaderId(team.getLeaderId());
        teamDto.setProjectName(team.getProjectName());
        teamDto.setMentorId(team.getMentorId());
        return teamDto;
    }
}
