package restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WSConfig implements WebSocketConfigurer {

    @Autowired
    private WSHandler wsHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(restaurantHandler(), "/ws")
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler restaurantHandler() {return wsHandler;}
}
