package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChangeMeetingDto;
import org.example.dto.CreateMeetingDto;
import org.example.dto.UserDetailsFromTokenDto;
import org.example.exceptions.AccessException;
import org.example.exceptions.DataException;
import org.example.exceptions.MeetingException;
import org.example.exceptions.TeamException;
import org.example.models.Meeting;
import org.example.repositories.MeetingRepository;
import org.example.security.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final  MeetingRepository meetingRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final Utils utils;

    public Meeting createMeeting(Long teamId, CreateMeetingDto createMeetingDto) throws TeamException, DataException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        if (utils.isOrganizer(user)
                || !teamService.isInTeam(user.getId(), teamId)
                || !teamService.isMentor(user.getId(), teamId)) {
            throw new AccessException("Meetings can be created only by team participants");
        }

        Meeting meeting = new Meeting();

        if (createMeetingDto.getLink().isEmpty()
                && createMeetingDto.getStartTime() == null
                && createMeetingDto.getEndTime() == null)
            throw new DataException("Set either link or time");

        meeting.setLink(createMeetingDto.getLink());
        meeting.setTeam(teamService.findTeamById(teamId));
        meeting.setEndTime(createMeetingDto.getEndTime());
        meeting.setStartTime(createMeetingDto.getStartTime());
        meetingRepository.save(meeting);
        return meeting;
    }

    public Meeting findMeetingById(Long meetingId) throws MeetingException {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        if (meeting.isEmpty()) throw new MeetingException("No such meeting");
        return meeting.get();
    }

    public Meeting changeMeeting(Long meetingId, ChangeMeetingDto changeMeetingDto) throws AccessException, MeetingException, TeamException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Meeting meeting = findMeetingById(meetingId);

        if (teamService.isInTeam(user.getId(), meeting.getTeam().getId()) &&
                teamService.isMentor(user.getId(), meeting.getTeam().getId())) {
            meeting.setStartTime(changeMeetingDto.getStartTime());
            meeting.setEndTime(changeMeetingDto.getEndTime());
            meeting.setLink(changeMeetingDto.getLink());
            meetingRepository.save(meeting);
            return meeting;
        } else throw new AccessException("Only team participants can modify meeting");
    }


    public boolean deleteMeeting(Long meetingId) throws MeetingException, TeamException, AccessException {
        UserDetailsFromTokenDto user = userService.getDetailsFromToken();
        Meeting meeting = findMeetingById(meetingId);

        if (teamService.isInTeam(user.getId(), meeting.getTeam().getId()) &&
                teamService.isMentor(user.getId(), meeting.getTeam().getId())) {
            meetingRepository.delete(meeting);
            return true;
        } else return false;
    }


    public List<Meeting> getTeamMeetings(Long teamId) throws TeamException {
        return teamService.findTeamById(teamId).getMeetings();
    }

}
