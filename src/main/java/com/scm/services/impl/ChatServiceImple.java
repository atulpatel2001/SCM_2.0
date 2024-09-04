package com.scm.services.impl;

import com.scm.entities.Chat;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.repsitories.ChatRepository;
import com.scm.services.ChatService;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImple implements ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserService userService;
    @Override
    public Chat sendMessagee(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public List<Chat> getChatsBetweenUsers(String userId1, String userId2) {
        return chatRepository.findChatsBetweenUsers(userId1, userId2);
    }
    @Override
    public String sendMessage(String receiverId,String message,String senderId) {
        Optional<User> senderUser = userService.getUserById(senderId);
        Chat chat = new Chat();
        chat.setSender(senderUser.get());
        chat.setStatus(Chat.MessageStatus.SENT);
        Optional<User> receiverUser = userService.getUserById(receiverId);
        chat.setReceiver(receiverUser.get());
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());
        chatRepository.save(chat);
        return "success";

//        List<Chat> messages = chatService.getChatsBetweenUsers(senderUser.getUserId(), receiverId);
//        model.addAttribute("messages", messages);
//        model.addAttribute("chatWith", receiverId);
//        model.addAttribute("chatWithUser", receiverUser.get().getName()); // Replace with actual contact name fetching logic
//        model.addAttribute("currentUserId", senderUser.getUserId());
//        return "chat";
    }


    @Override
    public void markMessageAsDelivered(Long messageId) {
        Optional<Chat> chat = chatRepository.findById(messageId);
        chat.ifPresent(c -> {
            c.setStatus(Chat.MessageStatus.DELIVERED);
            chatRepository.save(c);
        });
    }

    @Override
    public void markMessageAsRead(Long messageId) {
        Optional<Chat> chat = chatRepository.findById(messageId);
        chat.ifPresent(c -> {
            c.setStatus(Chat.MessageStatus.READ);
            chatRepository.save(c);
        });
    }

    @Override
    public List<Chat> findSentChatsForUser(String receiverId,String senderId) {
        return chatRepository.findSentChatsForUser(receiverId,senderId);
    }


}
