package joliebank.romanellas.layer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import joliebank.romanellas.layer.dto.PaymentCommunicationRequest;
import joliebank.romanellas.layer.dto.PaymentCommunicationResponse;
import joliebank.romanellas.layer.dto.PaymentRequest;
import joliebank.romanellas.layer.dto.PaymentResponse;
import joliebank.romanellas.layer.util.CamundaDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final CamundaDispatcher dispatcher;
    private final RestClient rest;
    private final Map<String, CompletableFuture<PaymentCommunicationRequest>> inFlight;

    public PaymentService(@Qualifier("inFlight")
                          Map<String, CompletableFuture<PaymentCommunicationRequest>> inFlight) {
        this.dispatcher = new CamundaDispatcher();
        this.rest = RestClient.builder()
                .baseUrl("https://joliebank.romanellas.cloud")
                .build();
        this.inFlight = inFlight;
    }

    public String getPaymentByToken(String token) {
        try {
            ResponseEntity<String> resp = rest.get()
                    .uri("/payments/{token}", token)
                    .retrieve()
                    .toEntity(String.class);
            return resp.getBody();
        } catch (org.springframework.web.client.RestClientResponseException e) {
            // 4xx/5xx qui
            throw new RuntimeException("HTTP " + e.getStatusCode() + " -> " + e.getResponseBodyAsString(), e);
        }
    }

    public PaymentResponse payTokenPut(String pathToken, PaymentRequest req) throws JsonProcessingException {
        if (!pathToken.equals(req.token()))
            return new PaymentResponse(400, "Token del path e del body non coincidono");

        String jsonResponse = this.getPaymentByToken(req.token());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonResponse, new TypeReference<>() {
        });
        String status = (String) map.get("status");

        if (!"Not paid".equals(status)) {
            return new PaymentResponse(400, "Token non valido");
        }

        CompletableFuture<PaymentCommunicationRequest> future =
                inFlight.computeIfAbsent(req.token(), k -> new CompletableFuture<>());

        this.dispatcher.startPaymentProcess(req.token());
        this.dispatcher.insertPaymentData(req);

        try {
            PaymentCommunicationRequest callback = future.get(50, TimeUnit.SECONDS);
            String callbackStatus = callback.status();
            System.out.println("TOKEN: " + req.token() + "   STATUS: " + callbackStatus);

            if (callbackStatus.equals("PAYMENT_SUCCESS"))
                return new PaymentResponse(200, "Ok.");
            return new PaymentResponse(400, callbackStatus);
        } catch (java.util.concurrent.TimeoutException te) {
            return new PaymentResponse(202, "In elaborazione");
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.getCause().printStackTrace();
            return new PaymentResponse(400, "Errore (callback)");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return new PaymentResponse(500, "Interrotto");
        } catch (Exception ignored) {
            return new PaymentResponse(400, "Errore nel processamento");
        }finally {
            inFlight.remove(req.token());

        }

    }


    public PaymentCommunicationResponse communicateTokenPut(String token,
                                                            @Valid PaymentCommunicationRequest body) {
        CompletableFuture<PaymentCommunicationRequest> f = inFlight.get(token);
        if (f != null && !f.isDone()) {
            f.complete(body);
            inFlight.remove(token, f);
            return new PaymentCommunicationResponse(200, "OK");
        }

        return new PaymentCommunicationResponse(202, "No waiter");
    }
}

