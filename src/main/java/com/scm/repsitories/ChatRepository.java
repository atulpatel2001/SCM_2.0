package com.scm.repsitories;

import com.scm.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE (c.sender.userId = :userId1 AND c.receiver.userId = :userId2) OR (c.sender.userId = :userId2 AND c.receiver.userId = :userId1) ORDER BY c.timestamp")
    List<Chat> findChatsBetweenUsers(@Param("userId1") String userId1, @Param("userId2") String userId2);


    @Query("SELECT c FROM Chat c WHERE (c.receiver.userId = :receiverId AND c.sender.userId = :senderId AND c.status = 'SENT') ORDER BY c.timestamp")
    List<Chat> findSentChatsForUser(@Param("receiverId") String receiverId,@Param("senderId") String senderId);

}
