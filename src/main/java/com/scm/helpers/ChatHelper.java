package com.scm.helpers;

import com.scm.entities.Chat;
import com.scm.entities.User;
import com.scm.repsitories.ChatRepository;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
//@Service
//public class ChatHelper {
//
//    @Autowired
//    private static ChatRepository chatRepository;
//    @Autowired
//    private static UserService userService;
//    public static String sendMessage(String receiverId,String message,String senderId) {
//
//
//        Optional<User> senderUser = userService.getUserById(senderId);
//        Chat chat = new Chat();
//        chat.setSender(senderUser.get());
//        Optional<User> receiverUser = userService.getUserById(receiverId);
//        chat.setReceiver(receiverUser.get());
//        chat.setMessage(message);
//        chat.setTimestamp(LocalDateTime.now());
//        chatRepository.save(chat);
//        return "success";
//    }
//}



// src/main/java/com/scm/services/ChatHelper.java


import com.scm.entities.Chat;
import com.scm.entities.User;
import com.scm.repsitories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ChatHelper {

    private static ChatHelper instance;

    private final ChatRepository chatRepository;
    private final UserService userService;

    // Constructor-based Dependency Injection
    @Autowired
    public ChatHelper(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
        instance = this; // Assign instance on bean initialization
    }

    // Instance Method to Send a Message
    public String sendMessageInstance(String receiverId, String message, String senderId) {
        Optional<User> senderUser = userService.getUserById(senderId);
        Optional<User> receiverUser = userService.getUserById(receiverId);

        if (senderUser.isPresent() && receiverUser.isPresent()) {
            Chat chat = new Chat();
            chat.setSender(senderUser.get());
            chat.setReceiver(receiverUser.get());
            chat.setMessage(message);
            chat.setTimestamp(LocalDateTime.now());
            chat.setStatus(Chat.MessageStatus.SENT);
             Chat save = chatRepository.save(chat);// Save chat message to the database
//            System.out.println(save.getMessage()+"========="+save.getStatus());
            return "success";
        } else {
            return "Failed to find users.";
        }
    }

    // Static Method to Access Instance Method
    public static String sendMessage(String receiverId, String message, String senderId) {
        if (instance == null) {
            throw new IllegalStateException("ChatHelper instance not initialized");
        }
        return instance.sendMessageInstance(receiverId, message, senderId);
    }
}

