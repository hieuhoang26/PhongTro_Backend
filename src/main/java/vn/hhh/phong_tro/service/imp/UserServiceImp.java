package vn.hhh.phong_tro.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hhh.phong_tro.dto.request.user.UserRequest;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.user.UserDetailResponse;
import vn.hhh.phong_tro.model.Role;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.repository.UserRepository;
import vn.hhh.phong_tro.service.RoleService;
import vn.hhh.phong_tro.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImp implements UserService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final RoleService roleService;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Save new user to DB
     *
     * @param request
     * @return userId
     */
    @Override
    public long addUser(UserRequest request) {
        Role role = roleService.getByRoleId(Integer.valueOf(request.getRole()));
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(role)
                .avatarUrl(request.getAvatarUrl())
                .build();
        userRepository.save(user);
        log.info("User has added successfully, userId={}", user.getId());
        return user.getId();

    }
    /**
     * Update user by userId
     *
     * @param userId
     * @param request
     */
    @Override
    public void updateUser(long userId,UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());

        if (request.getRole() != null) {
            Role role = roleService.getByRoleId(Integer.parseInt(request.getRole()));
            user.setRole(role);
        }

        userRepository.save(user);
        log.info("User has updated successfully, userId={}", userId);
    }
    /**
     * Change status of user by userId
     *
     * @param userId
//     * @param status
     */
    @Override
    public void changeStatus(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
        log.info("User status has changed successfully, userId={}", userId);
    }

    /**
     * Delete user by userId
     *
     * @param userId
     */
    @Override
    public void deleteUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    /**
     * Get user detail by userId
     *
     * @param userId
     * @return
     */
    @Override
    public UserDetailResponse getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserDetailResponse.builder()
                .id(userId)
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().getName())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
    /**
     * Get all user per pageNo and pageSize
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageResponse<?> getAllUsers(int pageNo, int pageSize) {
        Page<User> page = userRepository.findAll(PageRequest.of(pageNo, pageSize));

        List<UserDetailResponse> list = page.stream().map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .avatarUrl(user.getAvatarUrl())
                        .role(user.getRole().getName())
                        .isActive(user.getIsActive())
                        .createdAt(user.getCreatedAt())
                        .build())
                .toList();

        return PageResponse.builder()
                .page(pageNo)
                .size(pageSize)
                .total(page.getTotalPages())
                .items(list)
                .build();
    }


    @Override
    public Boolean existPhone(String phone) {
        return userRepository.existsUserByPhone(phone);
    }

    @Override
    public User getByPhone(String phone) {
        return userRepository.findUserByPhone(phone).orElse(null);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }
}
