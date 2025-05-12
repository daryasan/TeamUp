package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.AuthException;
import org.example.exceptions.ChatException;
import org.example.models.Chat;
import org.example.models.ChatParticipant;
import org.example.services.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Chat createChat(@RequestParam boolean isGroupChat,
                           @RequestBody List<Long> participantIds) {
        return chatService.createChat(isGroupChat, participantIds);
    }

    @GetMapping("/id")
    public Chat getChatById(
            @RequestParam Long chatId
    ) throws ChatException {
        return chatService.getChatById(chatId);
    }

    @GetMapping("/participants")
    public List<ChatParticipant> getChatParticipants(
            @RequestParam Long chatId
    ) throws ChatException {
        return chatService.getChatParticipants(chatId);
    }


    @PostMapping("/add")
    public Chat addChatParticipant(
            @RequestParam Long chatId,
            @RequestParam Long participantId
    ) throws ChatException {
        return chatService.addParticipant(chatId, participantId);
    }




    // TODO debug
    @GetMapping("/user")
    public List<Chat> getChatsForUser(@RequestParam Long userId) throws AuthException {
        return chatService.getChatsForUser(userId);
    }
}

