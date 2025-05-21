package gateway.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "jwt token for authentication")
public class AuthenticationResponseDTO {

    @Schema(description = "jwt token")
    private final String jwt;
}