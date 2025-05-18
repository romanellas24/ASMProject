package asm.couriers.gateway.auth.config;

import asm.couriers.gateway.auth.filter.JwtAuthenticationGatewayFilter;
import asm.couriers.gateway.auth.filter.JwtWebSocketAuthFilter;
import asm.couriers.gateway.auth.utils.EnvLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    private final String apiService;
    private final String feService;
    private final String wsService;

    public GatewayConfig(EnvLoader envLoader) {
        this.apiService = envLoader.getApiService();
        this.feService = envLoader.getFeService();
        this.wsService = envLoader.getWsService();
    }

    @Autowired
    private JwtAuthenticationGatewayFilter jwtFilter;

    @Autowired
    private JwtWebSocketAuthFilter jwtWSFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("get_api_route", r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/api/**")
                        .filters(f -> f
                                .rewritePath("/api/(?<segment>.*)", "/${segment}") // rimuove /api
                        )
                        .uri(apiService)
                )
                .route("secured_api_route", r -> r
                        .path("/api/**")
                        .and()
                        .method(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .filter(jwtFilter) // il tuo filtro custom per JWT
                                .rewritePath("/api/(?<segment>.*)", "/${segment}") // rimuove /api
                        )
                        .uri(apiService)
                )
                .route("ws", r-> r
                        .path("/ws","/ws/**")
                        .filters(f -> f
                                .filter(jwtWSFilter) // il tuo filtro custom per JWT
                        )
                        .uri(wsService)
                )
                .route("fe_unprotected_get", r->r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/**")
                        .uri(feService)
                )
                .build();
    }
}
