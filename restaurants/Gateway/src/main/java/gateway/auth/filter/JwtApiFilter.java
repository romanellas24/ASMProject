package gateway.auth.filter;

import gateway.auth.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtApiFilter extends AuthenticationFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getPath().value().startsWith("/auth/login")) {
            return chain.filter(exchange);
        }
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return handleUnauthorized(exchange);

        String jwt = authHeader.substring(7);
        if (!jwtUtil.isJwtValid(jwt))
            return handleUnauthorized(exchange);

        String role = jwtUtil.extractRoleFromToken(jwt);

        if ("ACMEAT".equals(role)) {
            if (!request.getPath().value().equals("/api/order") || request.getMethod() != HttpMethod.POST) {
                return handleUnauthorized(exchange);
            }
        }
        return  chain.filter(exchange);
    }

}
