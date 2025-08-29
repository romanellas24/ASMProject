package asm.couriers.courier_tracking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WSCourierConfig implements WebSocketConfigurer {

    private final WSCourierHandler wsCourierHandler;

    public WSCourierConfig(WSCourierHandler wsCourierHandler) {
        this.wsCourierHandler = wsCourierHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsCourierHandler, "/tracking/notification").setAllowedOrigins("*");
    }

//    @Bean
//    public WebSocketHandler deliveryHandler() {
//        return new WSCourierHandler();
//    }
}
