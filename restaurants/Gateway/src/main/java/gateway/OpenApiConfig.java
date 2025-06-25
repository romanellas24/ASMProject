package gateway;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {


    private final Config applicationConfig;

    public OpenApiConfig(Config applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://" + applicationConfig.getName() + ".romanellas.cloud");

        return new OpenAPI()
                .servers(List.of(server));
    }
}