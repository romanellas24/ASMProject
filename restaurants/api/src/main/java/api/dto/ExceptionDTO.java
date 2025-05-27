package api.dto;

import lombok.*;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ExceptionDTO {
    String message;
    Integer code;
}
