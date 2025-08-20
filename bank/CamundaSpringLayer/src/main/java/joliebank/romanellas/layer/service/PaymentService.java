package joliebank.romanellas.layer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import joliebank.romanellas.layer.dto.PaymentRequest;
import joliebank.romanellas.layer.dto.PaymentResponse;
import joliebank.romanellas.layer.util.CamundaDispatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class PaymentService {

    private final CamundaDispatcher dispatcher;
    private final RestClient rest;

    public PaymentService() {
        this.dispatcher = new CamundaDispatcher();
        this.rest = RestClient.builder()
                .baseUrl("https://joliebank.romanellas.cloud")
                .build();
    }

    public String getPaymentByToken(String token) {
        try {
            ResponseEntity<String> resp = rest.get()
                    .uri("/payments/{token}", token)  // token viene URL-encodato
                    .retrieve()
                    .toEntity(String.class);
            return resp.getBody(); // oppure resp.getStatusCode() ecc.
        } catch (org.springframework.web.client.RestClientResponseException e) {
            // 4xx/5xx qui
            throw new RuntimeException("HTTP " + e.getStatusCode() + " -> " + e.getResponseBodyAsString(), e);
        }
    }

    public PaymentResponse upsert(String pathToken, PaymentRequest req) throws JsonProcessingException {
        if (!pathToken.equals(req.token())) {
            throw new IllegalArgumentException("Token del path e del body non coincidono");
        }

        this.dispatcher.startPaymentProcess(req.token());
        String jsonResponse = this.getPaymentByToken(req.token());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonResponse, new TypeReference<>() {});
        String status = (String) map.get("status");
        if(status.equals("Not paid"))
            this.dispatcher.insertPaymentData(req);
        else {
            return new PaymentResponse(
                    400,
                    "Token non valido"
            );
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        jsonResponse = this.getPaymentByToken(req.token());
        map = mapper.readValue(jsonResponse, new TypeReference<>() {});
        status = (String) map.get("status");

        if(status.equals("Paid"))
            return new PaymentResponse(
                    200,
                    "Ok."
            );

        return new PaymentResponse(
                400,
                "Errore"
        );
    }
}

