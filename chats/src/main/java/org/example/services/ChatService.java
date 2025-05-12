package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.AuthException;
import org.example.exceptions.ChatException;
import org.example.models.Chat;
import org.example.models.ChatParticipant;
import org.example.repositories.ChatParticipantRepository;
import org.example.repositories.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserService userService;


    @Transactional
    public Chat createChat(boolean isGroupChat, List<Long> participantIds) {
        Chat chat = new Chat();
        chat.setGroup(isGroupChat);
        chat.setCreatedAt(LocalDateTime.now());

        chatRepository.save(chat);

        participantIds.forEach(userId ->

        {
            ChatParticipant participant = new ChatParticipant();
            participant.setChat(chat);
            participant.setUserId(userId);
            participant.setJoinedAt(LocalDateTime.now());
            chatParticipantRepository.save(participant);
        });


        return chat;
    }

    public Chat getChatById(Long chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) throw new ChatException("No such chat!");
        return chat.get();
    }


    public List<Chat> getChatsForUser(Long userId) throws AuthException {
        if (userService.getDetailsFromToken().getId() != userId)
            throw new AuthException("User can only see their chats");
        List<Chat> chats = new ArrayList<>();
        for (ChatParticipant c : chatParticipantRepository.findAll()) {
            if (Objects.equals(c.getUserId(), userId)) {
                chats.add(c.getChat());
            }
        }
        return chats;
    }


    public List<ChatParticipant> getChatParticipants(Long chatId) throws ChatException {
        return getChatById(chatId).getParticipants();
    }


    public Chat addParticipant(Long chatId, Long participantId) throws ChatException {

        Chat chat = getChatById(chatId);
        if (!chat.isGroup()) throw new ChatException("This chat is private! Can't add more participants");
        if (chat.getParticipants().stream().anyMatch(o -> Objects.equals(o.getId(), participantId)))
            throw new ChatException("This user is already in chat!");


        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUserId(participantId);
        participant.setJoinedAt(LocalDateTime.now());
        chatParticipantRepository.save(participant);

        return chat;
    }

}
