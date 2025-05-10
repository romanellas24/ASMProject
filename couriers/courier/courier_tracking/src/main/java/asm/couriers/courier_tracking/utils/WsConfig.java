package asm.couriers.courier_tracking.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WsConfig {

    @Value("${ws-url}")
    private String wsUrl;

    @Bean
    public String webSocketUrl() {
        return wsUrl;
    }
}
