package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
import org.example.services.TagService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TagServiceTest {

    @InjectMocks
    TagService tagService;

    @Mock
    TagRepository tagRepository;

    @Mock
    UserService userService;

    @Mock
    TeamRepository teamRepository;

    @Mock
    EventRepository eventRepository;


    @Test
    public void get_all_tags_valid() {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(1L);


        Tag tag1 = new Tag();
        tag1.setAuthorId(1L);
        Tag tag2 = new Tag();
        tag2.setAuthorId(null);
        Tag tag3 = new Tag();
        tag3.setAuthorId(99L);
        List<Tag> allTags = List.of(tag1, tag2, tag3);


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(tagRepository.findAll()).thenReturn(allTags);


        List<Tag> result = tagService.getAllTags();


        assertEquals(2, result.size());
        assertEquals(tag1, result.get(0));
        assertEquals(tag2, result.get(1));
    }


    @Test
    public void find_tag_by_id_valid() throws Exception {
        Long tagId = 1L;
        Tag tag = new Tag();
        tag.setId(tagId);


        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));


        Tag result = tagService.findTagById(tagId);


        assertNotNull(result);
        assertEquals(tagId, result.getId());
    }


    @Test
    public void find_tag_by_id_not_found() {
        Long tagId = 1L;


        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());


        assertThrows(TagException.class, () -> tagService.findTagById(tagId));
    }


    @Test
    public void create_tag_valid() {
        UserDetailsFromTokenDto user = new UserDetailsFromTokenDto();
        user.setId(1L);


        String tagName = "tag1";


        when(userService.getDetailsFromToken()).thenReturn(user);
        when(tagRepository.save(any(Tag.class))).thenAnswer(i -> i.getArgument(0));


        Tag result = tagService.createTag(tagName);


        assertNotNull(result);
        assertEquals(tagName, result.getName());
        assertEquals(user.getId(), result.getAuthorId());
    }


    @Test
    public void assign_tags_to_team_valid() throws Exception {
        Long teamId = 1L;
        List<Tag> tags = List.of(new Tag(), new Tag());


        Team team = new Team();
        team.setId(teamId);


        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));


        tagService.assignTagsToTeam(teamId, tags);


        verify(teamRepository).save(team);
        assertEquals(tags, team.getTags());
    }


    @Test
    public void assign_tags_to_team_not_found() {
        Long teamId = 1L;
        List<Tag> tags = List.of(new Tag(), new Tag());


        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());


        assertThrows(TeamException.class, () -> tagService.assignTagsToTeam(teamId, tags));
    }


    @Test
    public void assign_tags_to_event_valid() throws Exception {
        Long eventId = 1L;
        List<Tag> tags = List.of(new Tag(), new Tag());


        Event event = new Event();
        event.setId(eventId);


        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));


        tagService.assignTagsToEvent(eventId, tags);


        verify(eventRepository).save(event);
        assertEquals(tags, event.getTags());
    }


    @Test
    public void assign_tags_to_event_not_found() {
        Long eventId = 1L;
        List<Tag> tags = List.of(new Tag(), new Tag());


        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());


        assertThrows(EventException.class, () -> tagService.assignTagsToEvent(eventId, tags));
    }


    @Test
    public void find_events_by_tags_valid() {
        Tag tag1 = new Tag();
        tag1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setName("tag2");
        List<Tag> searchTags = List.of(tag1, tag2);


        Event event1 = new Event();
        event1.setTags(List.of(tag1, tag2, new Tag()));
        Event event2 = new Event();
        event2.setTags(List.of(tag1, tag2));
        Event event3 = new Event();
        event3.setTags(List.of(tag1));
        List<Event> allEvents = List.of(event1, event2, event3);


        when(eventRepository.findAll()).thenReturn(allEvents);


        List<Event> result = tagService.findEventsByTags(searchTags);


        assertEquals(2, result.size());
        assertEquals(event1, result.get(0));
        assertEquals(event2, result.get(1));
    }


    @Test
    public void find_teams_by_tags_valid() {
        Tag tag1 = new Tag();
        tag1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setName("tag2");
        List<Tag> searchTags = List.of(tag1, tag2);


        Team team1 = new Team();
        team1.setTags(List.of(tag1, tag2, new Tag()));
        Team team2 = new Team();
        team2.setTags(List.of(tag1, tag2));
        Team team3 = new Team();
        team3.setTags(List.of(tag1));
        List<Team> allTeams = List.of(team1, team2, team3);


        when(teamRepository.findAll()).thenReturn(allTeams);


        List<Team> result = tagService.findTeamsByTags(searchTags);


        assertEquals(2, result.size());
        assertEquals(team1, result.get(0));
        assertEquals(team2, result.get(1));
    }
}
