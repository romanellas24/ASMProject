package asm.couriers.gateway.auth.service;

import asm.couriers.gateway.auth.utils.UniqueUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final User user;

    public CustomUserDetailsService(User user) {
        this.user = user;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        if (!username.equals(user.getUsername())) {
            return Mono.error(new UsernameNotFoundException("User not found with username: " + username));
        }
        return Mono.just(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        ));
    }
}