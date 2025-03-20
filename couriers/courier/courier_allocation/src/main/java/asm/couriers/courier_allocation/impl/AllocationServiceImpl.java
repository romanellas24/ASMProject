package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dao.OrdersDAO;
import asm.couriers.courier_allocation.dao.VehiclesDAO;
import asm.couriers.courier_allocation.dto.AvailabilityDTO;
import asm.couriers.courier_allocation.dto.VehicleDTO;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;
import asm.couriers.courier_allocation.service.AllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllocationServiceImpl implements AllocationService {

    @Autowired
    VehiclesDAO vehiclesDAO;

    @Autowired
    OrdersDAO ordersDAO;

    @Override
    public Integer vehicle_available() throws VehicleNotAvailableException {
        Integer vehicles = vehiclesDAO.countVehiclesByStateIsFalse();
        if (vehicles == 0) {
            throw new VehicleNotAvailableException("Vehicles not available");
        }

        Integer id = 3;

        return id;
    }

    @Override
    public VehicleDTO getVehicleById(Long id) {
        return null;
    }

    @Override
    public VehicleDTO changeStateById(Long id) {
        return null;
    }
}
