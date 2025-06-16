package gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public abstract class AuthenticationFilter implements GatewayFilter {

    protected Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    protected Mono<Void> redirectToLogin(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
        exchange.getResponse().getHeaders().setLocation(URI.create("/auth/login"));
        return exchange.getResponse().setComplete();
    }

    protected Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        String acceptHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.ACCEPT);
        // browser request -> redirect to login  --- else return only unauthorized
        return (acceptHeader != null && acceptHeader.contains("text/html"))
                ? redirectToLogin(exchange) :
                unauthorized(exchange);
    }

    @Override
    public abstract Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);
}
