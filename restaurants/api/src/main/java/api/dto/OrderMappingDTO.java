package api.dto;

import api.entity.OrderMapping;
import lombok.Data;

@Data
public class OrderMappingDTO {
    private String companyName;
    private Integer companyId;
    private Integer orderId;

    public static OrderMappingDTO fromEntity(OrderMapping orderMapping) {
        OrderMappingDTO dto = new OrderMappingDTO();
        dto.setCompanyName(orderMapping.getCompanyName());
        dto.setCompanyId(orderMapping.getCompanyId());
        dto.setOrderId(orderMapping.getId());
        return dto;
    }
}
