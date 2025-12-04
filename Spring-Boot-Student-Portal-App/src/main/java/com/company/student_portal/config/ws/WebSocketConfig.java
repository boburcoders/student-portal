package com.company.student_portal.config.ws;

import com.company.student_portal.config.jwt.JwtService;
import com.company.student_portal.service.CustomUserDetailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtService jwtService;
    private final CustomUserDetailService userDetailService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "http://localhost:*",
                        "http://127.0.0.1:5500/test.html",
                        "http://127.0.0.1:5500/")
                .withSockJS();

        // SockJS bo'lmagan endpoint ham qo'shamiz
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:*", "https://yourdomain.com");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // JWT tokenni headerdan olish
                    String authToken = accessor.getFirstNativeHeader("Authorization");

                    if (authToken != null && authToken.startsWith("Bearer ")) {
                        String token = authToken.substring(7);

                        try {
                            // Tokendan username olish
                            String username = jwtService.extractUsername(token);
                            System.out.println(username);
                            if (username != null) {
                                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                                // Token validligini tekshirish
                                if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                                    UsernamePasswordAuthenticationToken authentication =
                                            new UsernamePasswordAuthenticationToken(
                                                    userDetails,
                                                    null,
                                                    userDetails.getAuthorities()
                                            );

                                    // Userga authentication o'rnatish
                                    accessor.setUser(authentication);
                                    log.info("WebSocket authenticated user: {}", username);
                                } else {
                                    log.warn("Invalid JWT token for WebSocket connection");
                                }
                            }
                        } catch (Exception e) {
                            log.error("Error authenticating WebSocket connection", e);
                        }
                    } else {
                        log.warn("No Authorization header found in WebSocket CONNECT frame");
                    }
                }

                return message;
            }
        });
    }
}