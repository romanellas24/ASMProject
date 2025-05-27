package asm.couriers.courier_allocation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@RequiredArgsConstructor
@Schema(description = "json returned when exception occur")
public class ExceptionDTO {
    @NonNull private String message;
    @NonNull private Integer code;
}
