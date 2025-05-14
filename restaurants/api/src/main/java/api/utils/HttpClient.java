package api.utils;

import api.dto.MenuDTO;
import api.exception.AcmeNotificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class HttpClient {
    private final WebClient webClient = WebClient.create();

    public void notifyAcmeMenuChanges(MenuDTO menuDTO) throws AcmeNotificationException {
        try{
            webClient.post()
                    .uri("/acme")
                    .header("Content-Type", "application/json")
                    .bodyValue(menuDTO)
                    .retrieve();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new AcmeNotificationException("Error notifying acme menu changes");
        }
        //TODO: controllo su codice di ritorno
    }
}
