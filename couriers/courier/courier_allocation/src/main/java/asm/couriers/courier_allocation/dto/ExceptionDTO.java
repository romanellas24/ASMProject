package asm.couriers.courier_allocation.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@RequiredArgsConstructor
public class ExceptionDTO {
    @NonNull String message;
    @NonNull Integer code;
}
