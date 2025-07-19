package com.commerce.catalos.core.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Messager {

    private final SimpMessagingTemplate messagingTemplate;

    public void send(final String topic, final Object message) {
        messagingTemplate.convertAndSend("/topic/"+ topic, message);
    }
}
