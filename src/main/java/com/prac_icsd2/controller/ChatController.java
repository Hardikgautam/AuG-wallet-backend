package com.prac_icsd2.controller;

import java.util.Map;

import org.springframework.ai.chat.model.ChatModel; // Generic interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "http://localhost:3000") // Matches your React port
public class ChatController {

    private final ChatModel chatModel;

    @Autowired
    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // Changed to PostMapping to match your Axios service
    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("query");
        return chatModel.call(message);
    }
}

// Simple DTO to capture the JSON body
@Data
class ChatRequest {
    private String query;
}