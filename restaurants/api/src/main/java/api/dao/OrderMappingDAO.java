package api.dao;

import api.entity.OrderMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderMappingDAO extends JpaRepository<OrderMapping, Integer> {

    Optional<OrderMapping> findFirstByCompanyIdAndCompanyName(Integer companyId, String companyName);

    boolean existsByCompanyIdAndCompanyName(Integer companyId, String companyName);
}
