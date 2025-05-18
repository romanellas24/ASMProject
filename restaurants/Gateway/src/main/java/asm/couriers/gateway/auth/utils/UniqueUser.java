package asm.couriers.gateway.auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class UniqueUser {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @Bean
    public User getUser(EnvLoader envLoader){
        if(user == null){
            String password = passwordEncoder.encode(envLoader.getPassword());
            user = new User(envLoader.getUsername(), password, new ArrayList<>());
        }
        return user;
    }
}
