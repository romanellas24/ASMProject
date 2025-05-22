package gateway.auth.config;

import gateway.auth.filter.JwtAuthenticationGatewayFilter;
import gateway.auth.filter.JwtWebSocketAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    @Value("${api.service}")
    private String apiService;
    @Value("${fe.service}")
    private String feService;
    @Value("${ws.service}")
    private String wsService;

    @Autowired
    private JwtAuthenticationGatewayFilter jwtFilter;

    @Autowired
    private JwtWebSocketAuthFilter jwtWSFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("swagger_api_docs", r -> r
                        .path("/api/docs/**")
                        .and()
                        .method(HttpMethod.GET)
                        .filters(f -> f
                                .rewritePath("/api/(?<segment>.*)", "/${segment}")
                        )
                        .uri(apiService)
                )
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
                        .and()
                        .predicate(exchange -> !exchange.getRequest().getURI().getPath().startsWith("/docs"))
                        .uri(feService)
                )
                .build();
    }
}
