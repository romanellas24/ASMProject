package asm.couriers.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class GatewayConfig {
    @Value("${service.tracking}")
    private String serviceTracking;

    @Value("${service.allocation}")
    private String serviceAllocation;

    @Value("${service.secure}")
    private Boolean secure;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("tracking_route_ws", r->r
                                .order(0)
                        .path("/tracking/notification")
                        .uri((secure ? "wss://" : "ws://")+ serviceTracking)
                )
                .route("tracking_route_api", r -> r
                        .order(1)
                        .path("/tracking","/tracking/**")
                        .filters(f -> f
                                .rewritePath("/tracking/(?<segment>.*)", "/${segment}")
                        )
                        .uri((secure ? "https://" : "http://")+ serviceTracking)
                )
                .route("allocation_route", r -> r
                        .path("/api", "/api/**")
                        .filters(f -> f
                                .rewritePath("/api(?:/(?<segment>.*))?", "/${segment}")
                        )
                        .uri((secure ? "https://" : "http://") + serviceAllocation)
                )
                .build();
    }
}
