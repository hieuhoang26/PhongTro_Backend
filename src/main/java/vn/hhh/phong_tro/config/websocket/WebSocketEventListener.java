//package vn.hhh.phong_tro.config.websocket;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//import vn.hhh.phong_tro.dto.chat.ChatMessage;
//
//import java.security.Principal;
//
//@Component
//public class WebSocketEventListener {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
//
//    @Autowired
//    private SimpMessageSendingOperations messagingTemplate;
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        logger.info("Received a new web socket connection");
//        Principal user = event.getUser();
//        logger.info("SessionConnectEvent - Principal: {}", user != null ? user.getName() : "null");
//
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        String username = (String) headerAccessor.getSessionAttributes().get("userId");
//        if(username != null) {
//            logger.info("User Disconnected : " + username);
//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setSenderId(Long.valueOf(username));
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }
//}
