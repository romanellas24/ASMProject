package gateway.filter;

import gateway.auth.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class JwtWebSocketFilter extends AuthenticationFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> tokenList = exchange.getRequest().getQueryParams().get("token");

        if (tokenList == null || tokenList.isEmpty()) {
            return handleUnauthorized(exchange);
        }

        String jwt = tokenList.get(0);
        log.info("request from token: {}", jwt);
        if (!jwtUtil.isJwtValid(jwt))
            return handleUnauthorized(exchange);

        log.info("token validated");

        String role = jwtUtil.extractRoleFromToken(jwt);

        log.info("role: {}", role);

        if ("ROLE_ACMEAT".equals(role)) {
            return handleUnauthorized(exchange);
        }

        return chain.filter(exchange);
    }
}