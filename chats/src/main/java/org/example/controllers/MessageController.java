package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.exceptions.ChatException;
import org.example.exceptions.MessageException;
import org.example.models.Chat;
import org.example.models.Message;
import org.example.services.MessageService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;


    @PostMapping("/send")
    public Message sendMessage(
            @RequestBody MessageDto messageDto
    ) {
        return messageService.sendMessage(messageDto);
    }


    @GetMapping("/chat")
    public Chat getChatByMessageId(
            @RequestParam Long id
    ) throws ChatException, MessageException {
        return messageService.getChatByMessageId(id);
    }


    @GetMapping("/get/pageable")
    public List<Message> getMessagesByChat(
            @RequestParam Long chatId,
            @RequestBody Pageable pageable
    ) {
        return messageService.getMessages(chatId, pageable);
    }

    @GetMapping("/get")
    public List<Message> getMessagesByChat(
            @RequestParam Long chatId
    ) {
        return messageService.getMessages(chatId);
    }

}
