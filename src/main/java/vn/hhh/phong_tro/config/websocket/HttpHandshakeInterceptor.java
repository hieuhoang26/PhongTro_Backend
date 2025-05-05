package vn.hhh.phong_tro.config.websocket;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.security.JwtService;
import vn.hhh.phong_tro.service.UserService;

import java.util.Map;

import static vn.hhh.phong_tro.util.TokenType.ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtTokenProvider;
    private final UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token     = req.getParameter("token");
            if (token != null && jwtTokenProvider.validateToken(token, ACCESS_TOKEN)) {
                String username = jwtTokenProvider.extractUsername(token, ACCESS_TOKEN); // phone
                User user = userService.getByPhone(username);
                attributes.put("userId", user.getId().toString());
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
