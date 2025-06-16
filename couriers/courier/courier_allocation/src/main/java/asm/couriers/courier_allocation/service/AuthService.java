package asm.couriers.courier_allocation.service;

import asm.couriers.courier_allocation.dto.CompanyDTO;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.exception.UnauthorizedException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface AuthService {

        /*
    public static String generateSHA256Hash(String input) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

         */
    //public Boolean isHashValid(CompanyDTO companyAuth) throws NoSuchAlgorithmException;
    public CompanyDTO getCompanyFromNameAndHash(String name, String hash) throws NotFoundException, UnauthorizedException;
}
