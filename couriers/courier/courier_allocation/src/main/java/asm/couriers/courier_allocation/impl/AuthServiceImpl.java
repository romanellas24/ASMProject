package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dao.CompaniesDAO;
import asm.couriers.courier_allocation.dto.CompanyDTO;
import asm.couriers.courier_allocation.entity.Company;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.exception.UnauthorizedException;
import asm.couriers.courier_allocation.service.AuthService;
import asm.couriers.courier_allocation.utils.CompanyToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    CompaniesDAO companiesDAO;


    @Override
    public CompanyDTO getCompanyFromNameAndHash(String name, String hash) throws NotFoundException, UnauthorizedException {
        if (!companiesDAO.existsByName(name)){
            throw new NotFoundException("Company name");
        }

        Company company = companiesDAO.findCompanyByName(name);
        if(!Objects.equals(hash, company.getHash())){
            throw new UnauthorizedException("Invalid hash");
        }

        return CompanyToDtoMapper.toDto(company);
    }
}
