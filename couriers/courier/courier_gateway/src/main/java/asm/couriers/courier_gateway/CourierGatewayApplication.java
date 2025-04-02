package asm.couriers.courier_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@SpringBootApplication
public class CourierGatewayApplication {

    static final String ALLOCATION_PREFIX = "/availability";
    static final String ALLOCATION_HOST= "http://localhost:8080";

    static final String TRACKING_PREFIX = "/tracking/";
    static final String TRACKING_HOST= "http://localhost:8081";

    static final String WILDCARD = "**";

    public static void main(String[] args) {
        SpringApplication.run(CourierGatewayApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> courier_allocation_GET() {
        return route("allocationGet")
                .GET(ALLOCATION_PREFIX + WILDCARD, http(ALLOCATION_HOST))
                .before(rewritePath(ALLOCATION_PREFIX + "(?<segment>.*)", "/${segment}"))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> courier_allocation_PUT() {
        return route("allocationPut")
                .PUT(ALLOCATION_PREFIX + WILDCARD, http(ALLOCATION_HOST))
                .before(rewritePath(ALLOCATION_PREFIX + "(?<segment>.*)", "/${segment}"))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> courier_tracking_GET() {
        System.out.println("PIPOIOASDJNOASNDIOUASNDASNDUON");
        return route("trackingGet")
                .GET(TRACKING_PREFIX + WILDCARD, http(TRACKING_HOST))
                .before(rewritePath(TRACKING_PREFIX + "(?<segment>.*)", "/${segment}"))
                .build();
    }

}
