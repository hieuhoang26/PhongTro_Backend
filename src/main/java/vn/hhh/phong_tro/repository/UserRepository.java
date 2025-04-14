package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserByPhone(String phone);
    Optional<User> findUserByEmail(String email);

    Boolean existsUserByPhone(String phone);
    Optional<User> findByName(String username);

//    @Query(value = "select r.name from Role r inner join  ur on r.id = ur.user.id where ur.id= :userId")
//    List<String> findAllRolesByUserId(Long userId);
}
