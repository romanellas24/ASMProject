package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dao.OrdersDAO;
import asm.couriers.courier_allocation.dao.VehiclesDAO;
import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.entity.Company;
import asm.couriers.courier_allocation.entity.Order;
import asm.couriers.courier_allocation.entity.Vehicle;
import asm.couriers.courier_allocation.exception.DeleteRequestTooLateException;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.exception.UnauthorizedException;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.utils.CompanyToDtoMapper;
import asm.couriers.courier_allocation.utils.RequestDTOtoOrderMapper;
import asm.couriers.courier_allocation.utils.VehicleToDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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
    public OrderInfoDTO allocate_vehicle(RequestAllocateDTO request, CompanyDTO company) throws NotFoundException {

        Optional<Vehicle> vehicle = vehiclesDAO.findById(request.getVehicle());

        if (vehicle.isEmpty()) {
            throw new NotFoundException("Vehicle ID");
        }

        Order order = RequestDTOtoOrderMapper.convertToOrderDTO(request, company);
        ordersDAO.save(order);

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

        orderInfoDTO.setVehicle(VehicleToDtoMapper.toDto(vehicle.get()));
        orderInfoDTO.setEndDeliveryTime(order.getEnd_delivery_time());
        orderInfoDTO.setStartDeliveryTime(order.getStart_delivery_time());
        orderInfoDTO.setOrderId(order.getOrderId());

        return orderInfoDTO;
    }

    @Override
    public Boolean delete_order(Integer orderId, CompanyDTO company) throws DeleteRequestTooLateException, UnauthorizedException, NotFoundException {
        Company compEntity = CompanyToDtoMapper.toEntity(company);

        Order order = ordersDAO.findByOrderId(orderId);
        if (order == null){
            throw new NotFoundException("Order ID");
        }

        if (!Objects.equals(order.getCompany().getId(), compEntity.getId())){
            throw new UnauthorizedException("You are not allowed to delete this order");
        }

        //delete is possible only 15 minutes before the start of delivery
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(order.getStart_delivery_time().minusMinutes(15)) && now.isBefore(order.getStart_delivery_time())){
            throw new DeleteRequestTooLateException("Too late. Impossible to delete order");
        } else if (now.isEqual(order.getStart_delivery_time()) || now.isAfter(order.getStart_delivery_time())){
            throw new UnauthorizedException("Delivery started or already finished");
        }

        try {
            ordersDAO.delete(order);
            return true;
        } catch (Exception e) {
            log.atDebug().log(e.getMessage());
            return false;
        }
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
