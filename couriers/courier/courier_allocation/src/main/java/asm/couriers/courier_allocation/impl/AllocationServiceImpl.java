package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dao.OrdersDAO;
import asm.couriers.courier_allocation.dao.VehiclesDAO;
import asm.couriers.courier_allocation.dto.RequestAllocateDTO;
import asm.couriers.courier_allocation.dto.VehicleDTO;
import asm.couriers.courier_allocation.entity.Order;
import asm.couriers.courier_allocation.entity.Vehicle;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.utils.RequestDTOtoOrderMapper;
import asm.couriers.courier_allocation.utils.VehicleToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AllocationServiceImpl implements AllocationService {

    @Autowired
    VehiclesDAO vehiclesDAO;

    @Autowired
    OrdersDAO ordersDAO;

    @Override
    public Integer vehicle_available(Integer deliveryTime, LocalDateTime expectedDelivery) throws VehicleNotAvailableException {

        int deliveryTimeSeconds = deliveryTime * 60;

        List<Vehicle> vehicles = vehiclesDAO.findAvailableVehicles(expectedDelivery,deliveryTimeSeconds);

        if(vehicles.isEmpty()){
            throw new VehicleNotAvailableException("Vehicles not available");
        }

        return vehicles.get(0).getId();
    }

    @Override
    public VehicleDTO allocate_vehicle(RequestAllocateDTO request) throws Exception {

        Optional<Vehicle> vehicle = vehiclesDAO.findById(request.getVehicle());

        if (vehicle.isEmpty()) {
            throw new NotFoundException("Vehicle ID");
        }

        Order order = RequestDTOtoOrderMapper.convertToOrderDTO(request);
        ordersDAO.save(order);

        return VehicleToDtoMapper.toDto(vehicle.get());
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
