package asm.couriers.courier_allocation.dao;

import asm.couriers.courier_allocation.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompaniesDAO extends JpaRepository<Company, Integer> {

    Company findCompanyByName(String name);
}
