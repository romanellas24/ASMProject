package asm.couriers.courier_allocation.service;

import asm.couriers.courier_allocation.dto.CompanyDTO;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.exception.UnauthorizedException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface AuthService {
    public CompanyDTO getCompanyFromNameAndHash(String name, String hash) throws NotFoundException, UnauthorizedException;
}
