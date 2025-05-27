package gateway.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication request containing username and password")
public class AuthenticationRequestDTO {

    @Schema(description = "Username", example = "pippo")
    private String username;
    @Schema(description = "Password", example = "password123")
    private String password;
}
