package joliebank.romanellas.layer.config;

import joliebank.romanellas.layer.dto.PaymentCommunicationRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
class SharedHashmapConfig {
    @Bean(name = "inFlight")
    public Map<String, CompletableFuture<PaymentCommunicationRequest>> inFlight() {
        return new ConcurrentHashMap<>(); // thread-safe
    }
}
