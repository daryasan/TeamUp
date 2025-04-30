package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.models.Chat;
import org.example.services.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestParam boolean isGroupChat,
                                           @RequestBody List<Long> participantIds) {
        Chat chat = chatService.createChat(isGroupChat, participantIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<Chat>> getChatsForUser(@PathVariable Long userId) {
        List<Chat> chats = chatService.getChatsForUser(userId);
        return ResponseEntity.ok(chats);
    }
}

