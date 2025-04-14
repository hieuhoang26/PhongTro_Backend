package vn.hhh.phong_tro.service;

import vn.hhh.phong_tro.dto.request.user.UserRequest;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.user.UserDetailResponse;
import vn.hhh.phong_tro.model.User;

public interface UserService {
//    UserDetailsService userDetailsService();
    void save(User user);
    long addUser(UserRequest user);
    void updateUser(long userId, UserRequest user);
//    void changeStatus(long userId, UserStatus status);
    void changeStatus(long userId);

    void deleteUser(long userId);

    UserDetailResponse getUserById(long userId);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);

    Boolean existPhone(String phone);

    User getByPhone(String phone);
    User getByEmail(String email);
}
