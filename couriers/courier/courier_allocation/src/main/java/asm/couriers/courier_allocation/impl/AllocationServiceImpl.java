package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dao.CompaniesDAO;
import asm.couriers.courier_allocation.dao.IdMappingDAO;
import asm.couriers.courier_allocation.dao.OrdersDAO;
import asm.couriers.courier_allocation.dao.VehiclesDAO;
import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.entity.Company;
import asm.couriers.courier_allocation.entity.IdMapping;
import asm.couriers.courier_allocation.entity.Order;
import asm.couriers.courier_allocation.entity.Vehicle;
import asm.couriers.courier_allocation.exception.BadData;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.utils.CompanyToDtoMapper;
import asm.couriers.courier_allocation.utils.RequestDTOtoOrderMapper;
import asm.couriers.courier_allocation.utils.VehicleToDtoMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
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
    CompaniesDAO companiesDAO;

    @Autowired
    IdMappingDAO idMappingDAO;

    @Autowired
    OrdersDAO ordersDAO;
    private Ordered ordered;

    @Override
    public Boolean isVehicleAvailable(Integer id, Integer deliveryTime, LocalDateTime expectedDelivery) throws BadData {
        int deliveryTimeSeconds = deliveryTime * 60;
        LocalDateTime startTime = expectedDelivery.minusSeconds(deliveryTimeSeconds);
        List<Vehicle> vehicles = vehiclesDAO.findAvailableVehicles(expectedDelivery,startTime);

        return vehicles.stream().anyMatch(vehicle -> Objects.equals(vehicle.getId(), id));
    }

    @Override
    public Integer vehicle_available(Integer deliveryTime, LocalDateTime expectedDelivery) throws VehicleNotAvailableException {

        int deliveryTimeSeconds = deliveryTime * 60;

        LocalDateTime startTime = expectedDelivery.minusSeconds(deliveryTimeSeconds);
        List<Vehicle> vehicles = vehiclesDAO.findAvailableVehicles(expectedDelivery,startTime);

        if(vehicles.isEmpty()){
            throw new VehicleNotAvailableException("Vehicles not available");
        }

        return vehicles.get(0).getId();
    }

    @Transactional
    @Override
    public OrderInfoDTO allocate_vehicle(RequestAllocateDTO request, CompanyDTO company) throws NotFoundException {

        Optional<Vehicle> vehicle = vehiclesDAO.findById(request.getVehicle());

        if (vehicle.isEmpty()) {
            throw new NotFoundException("Vehicle ID");
        }

        Order order = RequestDTOtoOrderMapper.convertToOrder(request, company);
        ordersDAO.save(order);

        Company companyEntity = companiesDAO.findCompanyByName(company.getName());
        IdMapping mapping = new IdMapping();
        mapping.setCompany(companyEntity);
        mapping.setOrder(order);
        mapping.setCompanyOrderId(request.getOrderId());

        idMappingDAO.save(mapping);

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

        orderInfoDTO.setVehicle(VehicleToDtoMapper.toDto(vehicle.get()));
        orderInfoDTO.setEndDeliveryTime(order.getEnd_delivery_time());
        orderInfoDTO.setStartDeliveryTime(order.getStart_delivery_time());
        orderInfoDTO.setOrderId(order.getOrderId());
        orderInfoDTO.setToAddress(order.getTo_address());
        orderInfoDTO.setFromAddress(order.getFrom_address());

        return orderInfoDTO;
    }


    @Override
    @Transactional
    public Boolean delete_order(Integer orderId) throws NotFoundException {
        if (!ordersDAO.existsById(orderId)){
            throw new NotFoundException("Order ID");
        }
        IdMapping mapping = idMappingDAO.findFirstByOrderId(orderId);
        Order order = ordersDAO.findByOrderId(orderId);

        try {
            idMappingDAO.delete(mapping);
            ordersDAO.delete(order);
            return true;
        } catch (Exception e) {
            log.atDebug().log(e.getMessage());
            return false;
        }
    }

    @Override
    public OrderInfoDTO getOrder(Integer id) throws NotFoundException {
        Optional<Order> order = ordersDAO.findById(id);
        if (order.isEmpty()) {
            throw new NotFoundException("Order ID");
        }
        return OrderInfoDTO.fromEntity(order.get());
    }

    @Override
    public OrderInfoDTO getOrder(Integer id, CompanyDTO company) throws NotFoundException {
        if (!idMappingDAO.existsByCompanyOrderIdAndCompanyId(id, company.getId())) {
            throw new NotFoundException("Order ID for company");
        }
        IdMappingDTO idMappingDTO = IdMappingDTO.fromEntity(idMappingDAO.findFirstByCompanyOrderIdAndCompanyId(id, company.getId()));
        return this.getOrder(idMappingDTO.getCourierOrderId());
    }

    @Override
    public CompanyDTO getCompanyFromName(String companyName) throws NotFoundException {
        companyName = companyName.toLowerCase();
        if (!companiesDAO.existsByName(companyName)) {
            throw new NotFoundException("Company not found");
        }

        Company company = companiesDAO.findCompanyByName(companyName);

        return CompanyToDtoMapper.toDto(company);
    }

    @Override
    public Boolean isOrderCompanyValid(Integer orderId, CompanyDTO company) {
        return !idMappingDAO.existsByCompanyOrderIdAndCompanyId(orderId, company.getId());
    }

}
