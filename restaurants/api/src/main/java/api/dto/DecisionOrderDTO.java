package api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public class DecisionOrderDTO implements Serializable {
    private Integer id;
    private Boolean accepted;
}
