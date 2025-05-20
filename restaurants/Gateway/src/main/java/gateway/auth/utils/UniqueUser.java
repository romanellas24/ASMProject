package gateway.auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class UniqueUser {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.name}")
    private String username;

    @Value("${user.psswd}")
    private String password;

    private User user;

    @Bean
    public User getUser(){
        if(user == null){
            String password = passwordEncoder.encode(this.password);
            user = new User(username, password, new ArrayList<>());
        }
        return user;
    }
}
