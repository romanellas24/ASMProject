package restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBasicInfoDTO implements Serializable {
    private Integer id;
    private Boolean deleted = false;
}
