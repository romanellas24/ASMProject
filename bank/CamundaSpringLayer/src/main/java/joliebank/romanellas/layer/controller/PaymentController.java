package joliebank.romanellas.layer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import joliebank.romanellas.layer.dto.PaymentRequest;
import joliebank.romanellas.layer.dto.PaymentResponse;
import joliebank.romanellas.layer.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PutMapping("/{token}")
    public ResponseEntity<PaymentResponse> put(
            @PathVariable("token") String token,
            @Valid @RequestBody PaymentRequest body) throws JsonProcessingException {

        PaymentResponse resp = service.upsert(token, body);
        return ResponseEntity.ok(resp);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

