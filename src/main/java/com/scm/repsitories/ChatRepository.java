package com.scm.repsitories;

import com.scm.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("""
    SELECT c 
    FROM Chat c 
    WHERE 
        (c.sender.userId = :loggedInUserId AND c.receiver.userId = :otherUserId) OR 
        (c.sender.userId = :otherUserId AND c.receiver.userId = :loggedInUserId)
    ORDER BY c.timestamp ASC
    """)
    List<Chat> findChatsBetweenUsers(@Param("loggedInUserId") String loggedInUserId,
                                     @Param("otherUserId") String otherUserId);


    @Query("SELECT c FROM Chat c WHERE (c.receiver.userId = :receiverId AND c.sender.userId = :senderId AND c.status = 'SENT') ORDER BY c.timestamp")
    List<Chat> findSentChatsForUser(@Param("receiverId") String receiverId,@Param("senderId") String senderId);

}
