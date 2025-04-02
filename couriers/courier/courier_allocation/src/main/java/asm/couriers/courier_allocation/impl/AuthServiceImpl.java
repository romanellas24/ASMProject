package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dao.CompaniesDAO;
import asm.couriers.courier_allocation.dto.CompanyDTO;
import asm.couriers.courier_allocation.entity.Company;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    CompaniesDAO companiesDAO;

    /*

    private String generateSHA256Hash(String input) throws NoSuchAlgorithmException {

        // Create a MessageDigest instance for SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Perform the hash computation
        byte[] encodedhash = digest.digest(input.getBytes());

        // Convert byte array into a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

     */

    @Override
    public Boolean isHashValid(CompanyDTO companyAuth) throws NotFoundException {

        Company company = companiesDAO.findCompanyByName(companyAuth.getName());

        if (company == null) {
            throw new NotFoundException("Company name");
        }

        return Objects.equals(companyAuth.getHash(), company.getHash());
    }
}
