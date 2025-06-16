package asm.couriers.courier_allocation.utils;

import asm.couriers.courier_allocation.dto.CompanyDTO;
import asm.couriers.courier_allocation.entity.Company;

public class CompanyToDtoMapper {
    public static CompanyDTO toDto(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setHash(company.getHash());
        return companyDTO;
    }

    public static Company toEntity(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setHash(companyDTO.getHash());
        return company;
    }
}
