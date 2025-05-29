package vn.hhh.phong_tro.service.imp;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.request.auth.LogInRequest;
import vn.hhh.phong_tro.dto.request.auth.ResetPasswordRequest;
import vn.hhh.phong_tro.dto.request.auth.SignUpRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.auth.TokenRefreshResponse;
import vn.hhh.phong_tro.dto.response.auth.TokenResponse;
import vn.hhh.phong_tro.exception.InvalidDataException;
import vn.hhh.phong_tro.exception.ResourceNotFoundException;
import vn.hhh.phong_tro.model.Role;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.security.Imp.UserDetailServiceImp;
import vn.hhh.phong_tro.security.JwtService;
import vn.hhh.phong_tro.service.AuthService;
import vn.hhh.phong_tro.service.RoleService;
import vn.hhh.phong_tro.service.UserService;
import vn.hhh.phong_tro.service.VerifyService;
import vn.hhh.phong_tro.util.TokenType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.REFERER;
import static vn.hhh.phong_tro.util.TokenType.RESET_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImp implements AuthService {

    final AuthenticationManager authenticationManager;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;


    final UserService userService;
    final UserDetailServiceImp userDetailServiceImp;
    final RoleService roleService;
    final VerifyService verifyService;


    @Override
    public TokenResponse login(LogInRequest logInRequest) {
        // xac thuc user
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInRequest.getPhone(), logInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        System.out.println("Authorities:");
//        user.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));

        //Xóa các refresh token cũ (nếu không hỗ trợ đa thiết bị)
//        refreshTokenService.deleteAllByUser(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
//        List<String> roleNames = user.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();
        // Lấy ROLE
        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst()
                .orElse("ROLE_USER");
        // Lấy danh sách permissions (bỏ các ROLE_)
//        List<String> permissions = user.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .filter(auth -> !auth.startsWith("ROLE_"))
//                .toList();
        boolean isVerify;
        System.out.println(user.getRole().getName());
        if (Objects.equals(user.getRole().getName(), "ADMIN")){
            isVerify = true;
        }
        else {
            isVerify= verifyService.checkIfUserVerified(user.getId());
        }
        TokenResponse.TokenResponseBuilder responseBuilder = TokenResponse.builder()
                .id(String.valueOf(user.getId()))
                .role(role)
                .phone(user.getPhone())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .verify(isVerify)
                .message("Login success");

        //save token to db
        return responseBuilder.build();
    }

    @Override
    public ResponseData signUp(SignUpRequest request) {
        if (userService.existPhone(request.getPhone())) {
            return new ResponseData(HttpStatus.BAD_REQUEST.value(), "Phone is already taken!");
        }

        Role role = roleService.getByRoleId(request.getRole());

        User user = User.builder()
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "1234"))
                .name(request.getName())
                .role(role)
                .build();

        userService.save(user);
        return new ResponseData(HttpStatus.OK.value(), "Sign Up successful");
    }


    @Override
    public TokenRefreshResponse refresh(HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new ResourceNotFoundException("Token must be not blank");
        }
        // Check trong DB
//        RefreshToken storedToken = refreshTokenService.findByToken(refreshToken)
//                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
//
//        if (storedToken.isExpired()) {
//            refreshTokenService.deleteByToken(refreshToken);
//            throw new RuntimeException("Refresh token expired");
//        }
        String phone = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        UserDetails user = userDetailServiceImp.loadUserByUsername(phone);
        if (jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            String newToken = jwtService.generateToken(user);
            // process new token
            return TokenRefreshResponse.builder()
                    .tokenType("Bearer")
                    .accessToken(newToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            return null;
        }

    }

    @Override
    public String logout(HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            return "Refresh token not found";
        }

//        refreshTokenService.deleteByToken(refreshToken);
        return "Logout successful";
    }

    /**
     * Forgot password
     *
     * @param email
     */
    @Override
    public String forgotPassword(String email) {
        // check email exists or not
        User user = userService.getByEmail(email);
        // generate reset token
        String resetToken = jwtService.generateResetToken(user);

        // save to db
//        tokenService.save(Token.builder().username(user.getUsername()).resetToken(resetToken).build());

        // TODO send email to user
        String confirmLink = String.format("curl --location 'http://localhost:80/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("--> confirmLink: {}", confirmLink);

        return resetToken;
//        return null;
    }

    @Override
    public String changePassword(ResetPasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Passwords do not match");
        }

        // get user by reset token
        var user = validateToken(request.getSecretKey());

        // update password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.save(user);

        return "Changed";
    }

    /**
     * Validate user and reset token
     *
     * @param token
     * @return
     */
    private User validateToken(String token) {
        // validate token
        var phone = jwtService.extractUsername(token, RESET_TOKEN);

        // validate user is active or not
        var user = userService.getByPhone(phone);

        if (userService.existPhone(phone)) {
            throw new InvalidDataException("User not active");
        }
        return user;
    }


}
