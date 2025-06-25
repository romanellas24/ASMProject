package gateway.auth;

import gateway.Config;
import gateway.auth.dto.AuthenticationRequestDTO;
import gateway.auth.dto.AuthenticationResponseDTO;
import gateway.auth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Login/logout handling and JWT")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    private final Config applicationConfig;

    public AuthController(Config applicationConfig) {
        this.applicationConfig = applicationConfig;
        log.info("Config bean injected. Server name is: {}", this.applicationConfig.getName());
    }

    @Operation(summary = "login page",responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTML login page",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
            )
    })
    @GetMapping("/login")
    public Mono<String> loginPage(ServerWebExchange exchange, Model model) {
        String beUrl = "https://" + applicationConfig.getName() + ".romanellas.cloud";
        model.addAttribute("beUrl", beUrl);
        return Mono.just("login");
    }

    @Operation(summary = "Authenticate user and return a JWT")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "authentication done",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDTO.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "authentication failed",
                    content = @Content)
    })
    @PostMapping("/login")
    @ResponseBody
    public Mono<ResponseEntity<AuthenticationResponseDTO>> createAuthenticationToken(
            @RequestBody AuthenticationRequestDTO authenticationRequest) {
        return jwtService.createJwtToken(authenticationRequest)
                .map(jwt -> {
                    log.info("JWT creato con successo per: {} ", authenticationRequest.getUsername());
                    return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
                })
                .onErrorResume(e -> {
                    log.info("Autenticazione fallita per {}: {}", authenticationRequest.getUsername(), e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public Mono<String> logout(ServerWebExchange exchange) {
        return Mono.just("redirect:/auth/login");
    }
}