package vn.hhh.phong_tro.service;



import jakarta.servlet.http.HttpServletRequest;
import vn.hhh.phong_tro.dto.request.auth.LogInRequest;
import vn.hhh.phong_tro.dto.request.auth.ResetPasswordRequest;
import vn.hhh.phong_tro.dto.request.auth.SignUpRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.auth.TokenRefreshResponse;
import vn.hhh.phong_tro.dto.response.auth.TokenResponse;


public interface AuthService {
    TokenResponse login(LogInRequest logInRequest);

    ResponseData signUp(SignUpRequest logInRequest);

    TokenRefreshResponse refresh(HttpServletRequest refreshToken);

    String logout(HttpServletRequest request);

    String forgotPassword(String email);


    String changePassword(ResetPasswordRequest request);



}
