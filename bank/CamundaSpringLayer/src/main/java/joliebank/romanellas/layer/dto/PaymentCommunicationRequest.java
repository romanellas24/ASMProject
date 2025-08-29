package joliebank.romanellas.layer.dto;

import jakarta.validation.constraints.*;

public record PaymentCommunicationRequest(
        @NotBlank String status
) {}
