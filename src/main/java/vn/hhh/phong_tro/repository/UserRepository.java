package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserByPhone(String phone);

    Optional<User> findUserByEmail(String email);

    //    Optional<User> findByPhoneOrName(String phone, String name);
    @Query("SELECT u FROM User u WHERE u.phone = :phoneOrName OR u.name = :phoneOrName")
    Optional<User> findByPhoneOrName(@Param("phoneOrName") String phoneOrName);

    Boolean existsUserByPhone(String phone);

    Optional<User> findByName(String username);

    List<User> findUsersByIdNot(Long id);

//    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favoritePosts WHERE u.id = :userId")
//    Optional<User> findByIdWithFavoritePosts(@Param("userId") Long userId);


//    @Query(value = "select r.name from Role r inner join  ur on r.id = ur.user.id where ur.id= :userId")
//    List<String> findAllRolesByUserId(Long userId);
}
