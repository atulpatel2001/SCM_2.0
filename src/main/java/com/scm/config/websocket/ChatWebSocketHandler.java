package com.scm.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.scm.entities.Chat;
import com.scm.helpers.ChatHelper;
import com.scm.services.ChatService;
import com.scm.services.UserService;
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
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
@Configuration
public class ChatWebSocketHandler extends TextWebSocketHandler{
/*

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
*/
private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        System.out.println(userId);
        if (userId != null) {
            sessions.put(userId, session); // Map the userId to the WebSocket session
            System.out.println("User connected: " + userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        Map<String, Object> messageMap = objectMapper.readValue(payload, Map.class);

        String receiverId = (String) messageMap.get("receiverId");
        String senderId = (String) messageMap.get("senderId");
        String messageText = (String) messageMap.get("message");
        String status = (String) messageMap.get("status");

        if ("TYPING".equals(status)) {
            sendTypingNotification(receiverId, senderId,messageText);
        } else {
            ChatHelper.sendMessage(receiverId, messageText, senderId);
            broadcastMessage(receiverId, senderId, messageText, status);
        }
    }

    private void sendTypingNotification(String receiverId, String senderId,String messageText) throws IOException {
        WebSocketSession recipientSession = sessions.get(senderId);

        if (recipientSession != null && recipientSession.isOpen()) {
            for (WebSocketSession s : sessions.values()) {
                if (s.isOpen()) {
                    Map<String, Object> chatResponse = new HashMap<>();
                    chatResponse.put("status", "TYPING");
                    chatResponse.put("receiverId", receiverId);
                    chatResponse.put("senderId", senderId);
                    chatResponse.put("message", messageText);

                    chatResponse.put("senderName", ChatHelper.getUserName_(senderId));
                    String responsePayload = objectMapper.writeValueAsString(chatResponse);
                    s.sendMessage(new TextMessage(responsePayload));
                }
            }
        }
    }

    private void broadcastMessage(String receiverId, String senderId, String messageText, String status) throws IOException {
        WebSocketSession recipientSession = sessions.get(senderId);

        if (recipientSession != null && recipientSession.isOpen()) {

            for (WebSocketSession s : sessions.values()) {
                if (s.isOpen()) {
                    Map<String, Object> chatResponse = new HashMap<>();
                    chatResponse.put("message", messageText);
                    chatResponse.put("senderId", senderId);
                    chatResponse.put("receiverId", receiverId);
                    chatResponse.put("timestamp", LocalDateTime.now().toString());
                    chatResponse.put("status", status);
                    String responsePayload = objectMapper.writeValueAsString(chatResponse);
                    s.sendMessage(new TextMessage(responsePayload));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }
    private String getUserIdFromSession(WebSocketSession session) {
        // Extract userId from session URL or headers
        // This assumes the URL contains the userId as a query parameter
        String userId = session.getUri().getQuery();
        if (userId != null) {
            return userId.split("=")[1]; // Extract userId from query string
        }
        return null;
    }

}