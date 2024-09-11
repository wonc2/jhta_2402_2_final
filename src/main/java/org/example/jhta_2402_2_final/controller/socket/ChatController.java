package org.example.jhta_2402_2_final.controller.socket;

import org.example.jhta_2402_2_final.model.dto.socket.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessageDto send(ChatMessageDto message) {
        // 메시지를 받은 후 가공해서 반환
        return new ChatMessageDto(message.getSender(), message.getContent());
    }
}