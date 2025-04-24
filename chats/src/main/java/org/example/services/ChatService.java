package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.ChatException;
import org.example.models.Chat;
import org.example.models.ChatParticipant;
import org.example.models.Message;
import org.example.redis.ChatRedisPublisher;
import org.example.repositories.ChatParticipantRepository;
import org.example.repositories.ChatRepository;
import org.example.repositories.MessageRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MessageRepository messageRepository;
    private final ChatRedisPublisher redisPublisher;


    @Transactional
    public Chat createChat(boolean isGroupChat, List<Long> participantIds) {
        Chat chat = new Chat();
        chat.setType(isGroupChat);
        chat.setCreatedAt(LocalDateTime.now());
        Chat savedChat = chatRepository.save(chat);

        participantIds.forEach(userId -> {
            ChatParticipant participant = new ChatParticipant();
            participant.setChat(savedChat);
            participant.setUserId(userId);
            participant.setJoinedAt(LocalDateTime.now());
            chatParticipantRepository.save(participant);
        });

        return savedChat;
    }


    @Transactional
    public Message sendMessage(Long chatId, Long senderId, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат с id " + chatId + " не найден!"));
        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        redisPublisher.publish("chat-channel:" + chatId, content);

        return savedMessage;
    }


    public List<Message> getMessages(Long chatId, Pageable pageable) {
        return messageRepository.findByChatId(chatId, pageable);
    }


    public List<Chat> getChatsForUser(Long userId) {
        List<ChatParticipant> participantRecords = chatParticipantRepository.findByUserId(userId);
        return participantRecords.stream()
                .map(ChatParticipant::getChat)
                .distinct()
                .collect(Collectors.toList());
    }
}
