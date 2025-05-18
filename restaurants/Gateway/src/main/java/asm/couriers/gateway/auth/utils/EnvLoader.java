package asm.couriers.gateway.auth.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class EnvLoader {

    private final Dotenv dotenv;

    public EnvLoader() {
        this.dotenv = Dotenv.load();
    }

    public String getUsername() {
        return dotenv.get("USERNAME");
    }

    public String getPassword() {
        return dotenv.get("USER_PASSWORD");
    }

    public String getApiService() {
        return dotenv.get("API_SERVICE_HOST");
    }
    public String getFeService() {
        return dotenv.get("FE_SERVICE_HOST");
    }
    public String getWsService() {
        return dotenv.get("WS_SERVICE_HOST");
    }

    public String getJWTSecret(){
        return dotenv.get("JWT_SECRET");
    }
}

