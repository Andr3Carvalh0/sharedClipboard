package pt.andre.projecto.WebSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocket implements WebSocketConfigurer {

    @Autowired
    protected WebSockerHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/desktop").setAllowedOrigins("*");
    }

    @Bean
    public WebSockerHandler getWebSocketHandler(){
        return new WebSockerHandler();
    }
}