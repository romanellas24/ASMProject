package asm.couriers.gateway.auth.service;

import asm.couriers.gateway.auth.utils.JwtUtil;
import asm.couriers.gateway.auth.dto.AuthenticationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
                .flatMap(auth -> userDetailsService.findByUsername(authenticationRequest.getUsername())) // Mono<UserDetails>
                .map(userDetails -> jwtUtil.generateToken(userDetails.getUsername())) // Mono<String>
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid Credentials"))); // errore se nessun utente trovato
    }
}