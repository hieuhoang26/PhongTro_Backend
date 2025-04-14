package vn.hhh.phong_tro.controller;




import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.auth.LogInRequest;
import vn.hhh.phong_tro.dto.request.auth.SignUpRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.auth.TokenResponse;
import vn.hhh.phong_tro.service.AuthService;
import vn.hhh.phong_tro.util.Uri;

import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Validated
@RestController
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authenticationService;

    @PostMapping(Uri.LOGIN)
    public ResponseEntity<TokenResponse> login(@RequestBody LogInRequest request) {
        return new ResponseEntity<>(authenticationService.login(request), OK);
    }
    @PostMapping(Uri.SIGNUP)
    public ResponseEntity<ResponseData> SignUp(@RequestBody SignUpRequest request) {
        return new ResponseEntity<>(authenticationService.signUp(request), OK);
    }

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

