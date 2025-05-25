package gateway.auth.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final User user;
    private final User acmeUser;

    public CustomUserDetailsService(@Qualifier("getUser") User user,@Qualifier("getAcmeUser") User acmeUser) {
        this.acmeUser = acmeUser;
        this.user = user;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        if (!username.equals(user.getUsername()) || !username.equals(acmeUser.getUsername())) {
            return Mono.error(new UsernameNotFoundException("User not found with username: " + username));
        }
        return Mono.just(acmeUser.getUsername().equals(username) ? acmeUser : user);
    }
}