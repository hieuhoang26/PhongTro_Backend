package vn.hhh.phong_tro.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.security.Imp.UserDetailServiceImp;
import vn.hhh.phong_tro.security.JwtService;
import vn.hhh.phong_tro.service.UserService;

import java.security.Principal;
import java.util.Collections;

import static vn.hhh.phong_tro.util.TokenType.ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    final UserDetailServiceImp userDetailServiceImp;
    private final JwtService jwtTokenProvider;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
                String phone = jwtTokenProvider.extractUsername(token,ACCESS_TOKEN);

                if (jwtTokenProvider.validateToken(token,ACCESS_TOKEN)) {
//                    Authentication auth = new UsernamePasswordAuthenticationToken(phone, null, Collections.emptyList());
                    Principal auth = new UsernamePasswordAuthenticationToken(phone, null);
                    accessor.setUser(auth);
//                    System.out.println("phone filter " + auth.getName());
                }
            }
        }
        return message;
    }

}
