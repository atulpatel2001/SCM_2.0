package com.scm.services;

import com.scm.entities.Chat;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChatService {



    public Chat sendMessagee(Chat chat);

    public List<Chat> getChatsBetweenUsers(String userId1, String userId2);

    public String sendMessage(String receivedId,String msg,String senderId);

    public void markMessageAsDelivered(Long messageId);
    public void markMessageAsRead(Long messageId);
    public List<Chat> findSentChatsForUser(String receiverId,String senderId);



}
