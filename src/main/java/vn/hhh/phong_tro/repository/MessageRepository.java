package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.hhh.phong_tro.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverId(String receiverId);

//    @Query("""
//    SELECT m FROM Message m
//    WHERE (m.senderId = :userA AND m.receiverId = :userB)
//       OR (m.senderId = :userB AND m.receiverId = :userA)
//    ORDER BY m.sentAt ASC
//""")
//    List<Message> findConversationBetween(@Param("userA") Long userA, @Param("userB") Long userB);
@Query("""
    SELECT m FROM Message m
    WHERE (m.senderId = :userA AND m.receiverId = :userB)
       OR (m.senderId = :userB AND m.receiverId = :userA)
    ORDER BY m.sentAt ASC
""")
List<Message> findConversationBetween(@Param("userA") String userA, @Param("userB") String userB);

}
