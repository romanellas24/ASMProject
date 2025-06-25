package restaurant.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecisionOrderDTO implements Serializable {
    private Integer id;
    private Boolean accepted;
}
