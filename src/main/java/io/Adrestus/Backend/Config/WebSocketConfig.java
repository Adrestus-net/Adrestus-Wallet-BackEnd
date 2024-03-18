package io.Adrestus.Backend.Config;

import io.Adrestus.Backend.Service.CustomUserDetailsService;
import io.Adrestus.Backend.Util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configurable
@Configuration
@EnableWebSocketMessageBroker
@EnableWebMvc
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry
                .addEndpoint("/websocket-explorer")
                .setAllowedOrigins("http://localhost:3000/")
                .withSockJS();
        stompEndpointRegistry
                .addEndpoint("/websocket-explorer")
                .setAllowedOrigins("http://localhost:3000/");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                log.info("Headers: {}", accessor);

                assert accessor != null;
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authorizationHeader == null) {
                        throw new IllegalArgumentException("Authorization with Bearer token is required");
                    }
                    assert authorizationHeader != null;
                    String token = authorizationHeader.substring(7);

                    String username = jwtTokenUtil.getUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    accessor.setUser(usernamePasswordAuthenticationToken);
                }

                return message;
            }

        });
    }
}