package com.tl.whatapps.Controller;

import com.tl.whatapps.Entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class RealTimeChat {

    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/group/public")
    public Message recieveMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSend("/group" + message.getChat().getId().toString(), message);
        return message;
    }

}
