package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.models.Chat;
import org.example.models.Message;
import org.example.redis.ChatRedisPublisher;
import org.example.repositories.AttachmentRepository;
import org.example.repositories.ChatRepository;
import org.example.repositories.MessageRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRedisPublisher redisPublisher;
    private final ChatRepository chatRepository;

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

}
