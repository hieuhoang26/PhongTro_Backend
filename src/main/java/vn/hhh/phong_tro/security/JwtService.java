package vn.hhh.phong_tro.security;

import org.springframework.security.core.userdetails.UserDetails;
import vn.hhh.phong_tro.util.TokenType;

public interface JwtService {
    String generateToken(UserDetails user);
    String generateRefreshToken(UserDetails user);
    String generateResetToken(UserDetails user);
    String extractUsername(String token, TokenType type);
    boolean isValid(String token, TokenType type, UserDetails userDetails);
}
