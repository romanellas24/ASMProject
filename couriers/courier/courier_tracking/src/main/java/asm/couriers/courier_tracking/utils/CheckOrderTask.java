package asm.couriers.courier_tracking.utils;

import asm.couriers.courier_tracking.WSCourierHandler;
import asm.couriers.courier_tracking.dao.OrdersDAO;
import asm.couriers.courier_tracking.dao.VehiclesDAO;
import asm.couriers.courier_tracking.dto.OrderDTO;
import asm.couriers.courier_tracking.entity.Order;
import asm.couriers.courier_tracking.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CheckOrderTask implements Runnable {

    private final OrdersDAO ordersDAO;
    private final VehiclesDAO vehiclesDAO;
    private final Integer courierId;
    private final WSCourierHandler courierHandler;

    /**
     * Check every minute if courierId starts deliver.
     *  Yes => send to delivery using ws and set status to unavailable.
     *  No => Do nothing.
     */
    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);;
        Order order = this.ordersDAO.findByVehicle_IdAndStartDeliveryTimeEquals(this.courierId,now);
        if (order != null) {
            OrderDTO orderdto = OrderToDtoMapper.toDto(order);

            Optional<Vehicle> courier = vehiclesDAO.findById(orderdto.getVehicleId());
            if (courier.isPresent()) {
                Vehicle vehicle = courier.get();
                vehicle.setAvailable(false);
                vehiclesDAO.save(vehicle);
            }

            try {
                courierHandler.notifyCourier(this.courierId, orderdto);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
