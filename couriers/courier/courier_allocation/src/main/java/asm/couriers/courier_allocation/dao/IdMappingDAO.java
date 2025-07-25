package asm.couriers.courier_allocation.dao;

import asm.couriers.courier_allocation.entity.IdMapping;
import asm.couriers.courier_allocation.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdMappingDAO extends JpaRepository<IdMapping, Integer> {

    IdMapping findFirstByOrderId(Integer orderId);
    IdMapping findFirstByCompanyOrderIdAndCompanyId(Integer companyOrderId, Integer companyId);
    boolean existsByCompanyOrderIdAndCompanyId(Integer companyOrderId, Integer companyId);
}
