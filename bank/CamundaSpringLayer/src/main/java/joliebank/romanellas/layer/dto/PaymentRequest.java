package joliebank.romanellas.layer.dto;

import jakarta.validation.constraints.*;

public record PaymentRequest(
        @NotBlank String token,
        @NotBlank @Pattern(regexp = "\\d{13,19}", message = "PAN deve contenere 13-19 cifre") String pan,
        @NotNull @Min(0) @Max(9999) Integer cvv, // int nel JSON; qui Integer per @NotNull
        @NotBlank String card_holder_first_name,
        @NotBlank String card_holder_last_name,
        @NotNull @Min(1) @Max(12) Integer expire_month,
        @NotNull @Min(2000) Integer expire_year
) {}
