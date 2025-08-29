package asm.couriers.courier_allocation.dto;

import asm.couriers.courier_allocation.entity.IdMapping;
import lombok.Data;

@Data
public class IdMappingDTO {
    private Integer companyId;
    private Integer companyOrderId;
    private Integer courierOrderId;

    public static IdMappingDTO fromEntity(IdMapping idMapping) {
        IdMappingDTO dto = new IdMappingDTO();
        dto.setCompanyId(idMapping.getCompany().getId());
        dto.setCompanyOrderId(idMapping.getCompanyOrderId());
        dto.setCourierOrderId(idMapping.getOrder().getOrderId());
        return dto;
    }
}
