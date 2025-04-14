package vn.hhh.phong_tro.security.Imp;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.model.Permission;
import vn.hhh.phong_tro.model.Role;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImp implements UserDetailsService {
    final UserRepository userRepository;

    @Override
    @Transactional
//    LazyInitializationException của Hibernate, cố gắng truy cập vào một collection (ở đây là collection permissions của entity Role)
//    được đánh dấu là lazy sau khi session đã đóng
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByPhone(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // Tạo danh sách các authorities (vai trò + quyền)
        Set<GrantedAuthority> authorities = new HashSet<>();

//        Role role = user.getRole();
//        if (role != null) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//
//            if (role.getPermissions() != null) {
//                for (Permission permission : role.getPermissions()) {
//                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
//                }
//            }
//        }
        // Lấy vai trò của người dùng
        Role role = user.getRole();
        if (role != null) {
            // Thêm vai trò vào authorities
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // Thêm quyền của vai trò vào authorities (nếu có)
            if (role.getPermissions() != null) {
                for (Permission permission : role.getPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }
        System.out.println(user.toString());
        return  user;
    }

}
