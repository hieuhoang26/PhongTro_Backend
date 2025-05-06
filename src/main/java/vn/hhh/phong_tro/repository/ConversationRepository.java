package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.hhh.phong_tro.model.Conversation;
import vn.hhh.phong_tro.model.User;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByUser1AndUser2(User user1, User user2);

    Optional<Conversation> findByUser1AndUser2OrUser2AndUser1(User u1, User u2, User u3, User u4);


    // Tìm các conversation của 1 user cụ thể
    @Query("SELECT c FROM Conversation c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<Conversation> findAllByUserId(@Param("userId") Long userId);

//    @Query("SELECT c FROM Conversation c WHERE c.user1.id = :userId OR c.user2.id = :userId")
//    Page<Conversation> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    // Tìm conversation giữa 2 user cụ thể
    @Query("SELECT c FROM Conversation c WHERE (c.user1.id = :user1Id AND c.user2.id = :user2Id) OR (c.user1.id = :user2Id AND c.user2.id = :user1Id)")
    Optional<Conversation> findBetweenUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

}
