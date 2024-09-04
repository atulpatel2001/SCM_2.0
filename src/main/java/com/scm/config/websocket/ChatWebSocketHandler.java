package com.scm.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.scm.entities.Chat;
import com.scm.helpers.ChatHelper;
import com.scm.services.ChatService;
import com.scm.services.impl.ChatServiceImple;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Configuration
public class ChatWebSocketHandler extends TextWebSocketHandler{



    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(message.getPayload());
//        Chat incomingChat = objectMapper.readValue(message.getPayload(), Chat.class);
//        incomingChat.setTimestamp(LocalDateTime.now());

        Map<String, Object> messageMap = objectMapper.readValue(payload, Map.class);
        String receiverId = (String) messageMap.get("receiverId");
        String senderId  = (String) messageMap.get("senderId");
        String messageText = (String) messageMap.get("message");

        ChatHelper.sendMessage(receiverId,messageText,senderId);



        for (WebSocketSession s : sessions.values()) {
            if (s.isOpen()) {
                Map<String, Object> chatResponse = new HashMap<>();
                chatResponse.put("message", messageText);
                chatResponse.put("senderId",senderId);
                chatResponse.put("receiverId", receiverId);
                chatResponse.put("timestamp", LocalDateTime.now().toString());
                chatResponse.put("status", Chat.MessageStatus.SENT);
                String responsePayload = objectMapper.writeValueAsString(chatResponse);
                s.sendMessage(new TextMessage(responsePayload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }


}