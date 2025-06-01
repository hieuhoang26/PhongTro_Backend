package vn.hhh.phong_tro.controller;




import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.ResetPasswordEmailDto;
import vn.hhh.phong_tro.dto.request.auth.LogInRequest;
import vn.hhh.phong_tro.dto.request.auth.ResetPasswordRequest;
import vn.hhh.phong_tro.dto.request.auth.SignUpRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.dto.response.auth.TokenResponse;
import vn.hhh.phong_tro.service.AuthService;
import vn.hhh.phong_tro.service.MailService;
import vn.hhh.phong_tro.util.EmailType;
import vn.hhh.phong_tro.util.Uri;

import java.io.UnsupportedEncodingException;

import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Validated
@RestController
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authenticationService;
    private final MailService mailService;

    @PostMapping(Uri.LOGIN)
    public ResponseData<TokenResponse> login(@RequestBody LogInRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "User has updated successfully",authenticationService.login(request));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }
    }
    @PostMapping(Uri.SIGNUP)
    public ResponseData SignUp(@RequestBody SignUpRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "User has create successfully",authenticationService.signUp(request));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create user fail");
        }
    }

    /**
     * Gửi link đặt lại mật khẩu qua email
     * @param  email
     * @return thông báo hoặc token (tạm thời)
     */
    @PostMapping(Uri.FORGOT)
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String token = authenticationService.forgotPassword(email);
        return ResponseEntity.ok("Reset password link has been sent to your email.");

    }

    /**
     * Đổi mật khẩu bằng token
     * @param request chứa token + mật khẩu mới
     * @return kết quả đổi mật khẩu
     */
    @PostMapping(Uri.CHANGE)
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        String result = authenticationService.changePassword(request);
        return ResponseEntity.ok(result);
    }
//    @GetMapping(Uri.REFRESH)
//    public ResponseEntity<String> demoMail() throws MessagingException, UnsupportedEncodingException {
//        ResetPasswordEmailDto dto = new ResetPasswordEmailDto("sssss", "demooa");
//        String result = mailService.sendEmailV2("hoanghuyhieu2003@gmail.com", "Reset your password", dto, EmailType.Auth);
//
//        return ResponseEntity.ok(result);
//    }

//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
//        return new ResponseEntity<>(authenticationService.refreshToken(request), OK);
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        return new ResponseEntity<>(authenticationService.logout(request), OK);
//    }
}

