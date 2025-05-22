package api.utils;

import api.dto.MenuDTO;
import api.exception.AcmeNotificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class HttpClient {

    private final RestTemplate restTemplate;

    public HttpClient() {
        this.restTemplate = new RestTemplate();
    }

    public void notifyAcmeMenuChanges(MenuDTO menuDTO) throws AcmeNotificationException {
        String url = "http://acme-service-base-url/acme";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, menuDTO, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AcmeNotificationException("Error response from Acme: " + response.getStatusCode());
            }

            log.info("Response from Acme: {}", response.getBody());

        } catch (RestClientException e) {
            log.warn("Error notifying acme menu changes: {}", e.getMessage(), e);
            throw new AcmeNotificationException("Error notifying acme menu changes");
        }
    }
}