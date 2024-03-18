package io.Adrestus.Backend.implementation;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;

public class SocketController {
    @SendToUser("/topic/user")
    public String send(@Payload String message) {
        return message;
    }
}
