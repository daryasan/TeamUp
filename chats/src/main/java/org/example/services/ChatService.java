package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.AttachmentDto;
import org.example.dto.MessageDto;
import org.example.models.Attachment;
import org.example.models.Chat;
import org.example.models.ChatParticipant;
import org.example.models.Message;
import org.example.redis.ChatRedisPublisher;
import org.example.repositories.AttachmentRepository;
import org.example.repositories.ChatParticipantRepository;
import org.example.repositories.ChatRepository;
import org.example.repositories.MessageRepository;
import org.springframework.data.domain.Pageable;
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
    private final AttachmentRepository attachmentRepository;


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
    public Message sendMessage(MessageDto messageDto) {
        Chat chat = chatRepository.findById(messageDto.getChatId())
                .orElseThrow(() -> new RuntimeException("Чат с id " + messageDto.getChatId() + " не найден!"));
        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(messageDto.getSenderId());
        message.setContent(messageDto.getContent());
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        redisPublisher.publish("chat-channel:" +
                        messageDto.getChatId(),
                        messageDto.getContent());

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

    public List<Attachment> getAttachmentsByChat(Long chatId) {
        return attachmentRepository.findByMessageId(chatId);
    }
}
