package gateway.auth;

import gateway.auth.dto.AuthenticationRequestDTO;
import gateway.auth.dto.AuthenticationResponseDTO;
import gateway.auth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "login page",responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTML login page",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)
            )
    })
    @GetMapping("/login")

    public Mono<String> loginPage(ServerWebExchange exchange, Model model) {
        String scheme = exchange.getRequest().getURI().getScheme();
        String serverName = exchange.getRequest().getURI().getHost();
        int serverPort = exchange.getRequest().getURI().getPort();

        String beUrl = scheme + "://" + serverName +
                ((serverPort == 80 || serverPort == 443 || serverPort == -1) ? "" : ":" + serverPort);

        model.addAttribute("beurl", beUrl);
        return Mono.just("login");
    }

    @Operation(summary = "Authenticate user and return a JWT")
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