package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.hhh.phong_tro.model.Conversation;
import vn.hhh.phong_tro.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    Optional<Message> findTopByConversationOrderBySentAtDesc(Conversation conversation);

    long countByConversationAndIsReadFalseAndSenderIdNot(Conversation conversation, Long senderId);

    List<Message> findByConversationIdAndIsReadFalseAndSenderIdNot(Long conversationId, Long senderId);


//    List<Message> findByReceiverId(String receiverId);


//@Query("""
//    SELECT m FROM Message m
//    WHERE (m.senderId = :userA AND m.receiverId = :userB)
//       OR (m.senderId = :userB AND m.receiverId = :userA)
//    ORDER BY m.sentAt ASC
//""")
//List<Message> findConversationBetween(@Param("userA") String userA, @Param("userB") String userB);

}
