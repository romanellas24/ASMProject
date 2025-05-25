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

    @Value("${userRest.name}")
    private String username;

    @Value("${userRest.psswd}")
    private String password;

    @Value("${acmeatuser.name}")
    private String acmeUsername;

    @Value("${acmeatuser.password}")
    private String acmePassword;

    private User user;
    private User acmeUser;

    @Bean
    public User getUser(){
        if(user == null){
            String password = passwordEncoder.encode(this.password);
            user = new User(username, password, new ArrayList<>());
        }
        return user;
    }

    @Bean
    public User getAcmeUser(){
        if(acmeUser == null){
            String password = passwordEncoder.encode(this.acmePassword);
            acmeUser = new User(username, password, new ArrayList<>());
        }
        return acmeUser;
    }
}
