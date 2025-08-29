package asm.couriers.courier_tracking.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.camunda.zeebe.client.impl.ZeebeObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CamundaClientConfig {

    @Bean
    @Primary
    public ZeebeObjectMapper zeebeObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        return new ZeebeObjectMapper(objectMapper);
    }
}