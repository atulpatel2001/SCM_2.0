package com.scm.controllers;

import com.scm.entities.Chat;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ChatService;
import com.scm.services.ContactService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/chats")
public class ChatController {
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    @Autowired
    private ContactService contactService;

    @Value("${wesocket.custom.path}")
    private String socket_url;
    @GetMapping("/chat")
    public String chatWithContact(@RequestParam String chatWith, Model model, Authentication authentication, HttpSession session) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        Contact contact = contactService.getById(chatWith);
        User isRegister = userService.getUserByEmail(contact.getEmail());

        if(isRegister == null){
            session.setAttribute("message",
                    Message.builder().content("Your Friends is not Register With Our PlateForm !!")
                            .type(MessageType.yellow).build());
            return "redirect:/user/contacts";
        }
        else {
            User chatWithUser = userService.getUserByEmail(isRegister.getEmail());
            List<Chat> messages = chatService.getChatsBetweenUsers(user.getUserId(), chatWithUser.getUserId());
//            Map<LocalDate, List<Chat>> messages = messagess.stream()
//                    .collect(Collectors.groupingBy(message -> message.getTimestamp().toLocalDate()));
            final List<Chat> receivedChat = chatService.findSentChatsForUser(user.getUserId(),chatWithUser.getUserId());
            for (Chat c:receivedChat
                 ) {
                chatService.markMessageAsRead(c.getId());
            }
            for (Chat m:messages
                 ) {
                System.out.println(m.getTimestamp());
            }
            model.addAttribute("messages", messages);
            model.addAttribute("chatWith", chatWithUser.getUserId());
            model.addAttribute("chatWithUser", chatWithUser); // Replace with actual contact name fetching logic
            model.addAttribute("currentUserId", user.getUserId());
            model.addAttribute("contactImage",contact.getPicture());
            model.addAttribute("socket_url",socket_url);
            return "user/chat";
        }
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam String receiverId, @RequestParam String message, Model model,Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User senderUser = userService.getUserByEmail(username);
        Chat chat = new Chat();
        chat.setSender(senderUser);

        Optional<User> receiverUser = userService.getUserById(receiverId);
        chat.setReceiver(receiverUser.get());
        chat.setMessage(message);
        chat.setTimestamp(LocalDateTime.now());
        chatService.sendMessagee(chat);

        List<Chat> messages = chatService.getChatsBetweenUsers(senderUser.getUserId(), receiverId);
        model.addAttribute("messages", messages);
        model.addAttribute("chatWith", receiverId);
        model.addAttribute("chatWithUser", receiverUser.get().getName()); // Replace with actual contact name fetching logic
        model.addAttribute("currentUserId", senderUser.getUserId());
        return "user/chat";
    }

}
