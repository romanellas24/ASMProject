package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Date of menu updated")
public class UpdateMenuResponseDTO {
    @Schema(description = "Local Date of date of menu updated", example = "24-05-2025")
    private LocalDate dateMenuChanged;
}
