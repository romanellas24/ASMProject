package gateway.auth.service;

import gateway.auth.utils.JwtUtil;
import gateway.auth.dto.AuthenticationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class JwtService {

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public Mono<String> createJwtToken(AuthenticationRequestDTO authenticationRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        return authenticationManager.authenticate(authToken)
                .flatMap(auth -> userDetailsService.findByUsername(authenticationRequest.getUsername()))
                .map(userDetails -> jwtUtil.generateToken(userDetails.getUsername()))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid Credentials")));
    }
}