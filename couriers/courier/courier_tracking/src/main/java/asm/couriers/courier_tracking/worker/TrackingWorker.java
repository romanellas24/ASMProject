package asm.couriers.courier_tracking.worker;

import asm.couriers.courier_tracking.WSCourierHandler;
import asm.couriers.courier_tracking.dao.OrdersDAO;
import asm.couriers.courier_tracking.dao.VehiclesDAO;
import asm.couriers.courier_tracking.dto.OrderDTO;
import asm.couriers.courier_tracking.entity.Order;
import asm.couriers.courier_tracking.entity.Vehicle;
import asm.couriers.courier_tracking.utils.OrderToDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TrackingWorker {
    private final ZeebeClient zeebe;
    private final OrdersDAO ordersDAO;
    private final VehiclesDAO vehiclesDAO;
    private final ObjectMapper objectMapper;
    private final WSCourierHandler courierHandler;
    private final Set<Integer> ordersInProcess = new HashSet<>();

    public TrackingWorker(ZeebeClient zeebe, ObjectMapper objectMapper, OrdersDAO ordersDAO, VehiclesDAO vehiclesDAO, WSCourierHandler courierHandler) {
        this.zeebe = zeebe;
        this.ordersDAO = ordersDAO;
        this.vehiclesDAO = vehiclesDAO;
        this.objectMapper = objectMapper;
        this.courierHandler = courierHandler;
    }

    public void checkDeliveryStart(JobClient client, final ActivatedJob job) {
        log.info("Checking delivery start");

        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0)
                .plusHours(2);
        List<Order> orderList = this.ordersDAO.findAllByStartDeliveryTimeLessThanEqual(now);
        List<OrderDTO> list = orderList.stream().map(OrderToDtoMapper::toDto).toList();

        log.info("now:" + now + "- orders: " + list);

        list = list.stream().filter(order -> !ordersInProcess.contains(order.getOrderId())).toList();

        if (!list.isEmpty()) {
            list.forEach((OrderDTO order) -> {ordersInProcess.add(order.getOrderId());});
        }

        client.newCompleteCommand(job.getKey())
                .variable("order_list", list)
                .send()
                .join();
        }

    public void prepareNotification(JobClient client, final ActivatedJob job) {

         final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order_rider"), OrderDTO.class);
        log.info("Prepare notification to rider {} for order {}", order.getVehicleId(), order.getOrderId());
         try{
             zeebe.newPublishMessageCommand()
                     .messageName("rider_starts_delivering")
                     .correlationKey("order:" + order.getOrderId() + "-rider:"+order.getVehicleId())
                     .variables(job.getVariablesAsMap())
                     .send();

             client.newCompleteCommand(job.getKey())
                     .send()
                     .join();


         } catch (Exception e) {
             log.error(e.getMessage());
             client.newFailCommand(job.getKey())
                     .retries(job.getRetries() - 1)
                     .errorMessage(e.getMessage())
                     .send()
                     .join();
         }
    }

    public void notifyRider(JobClient client, final ActivatedJob job){
        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order_rider"), OrderDTO.class);
        log.info("Notify rider {} for order {}", order.getVehicleId(), order.getOrderId());

         try {
             courierHandler.addInPendingOrder(order.getVehicleId(),order);

             client.newCompleteCommand(job.getKey())
                     .variable("rider", order.getVehicleId())
                     .send()
                     .join();
         } catch (Exception e) {
             log.error(e.getMessage());
             client.newFailCommand(job.getKey())
                     .retries(job.getRetries() - 1)
                     .errorMessage(e.getMessage())
                     .send()
                     .join();
         }
    }

    private void changeRiderStatus(Integer riderId, Boolean available) throws Exception{
        Optional<Vehicle> courier = vehiclesDAO.findById(riderId);
        if (courier.isPresent()) {
            Vehicle vehicle = courier.get();
            vehicle.setAvailable(available);
            vehiclesDAO.save(vehicle);
        } else {
            throw new Exception("rider not found");
        }
    }

    public void setRiderStatusAvailable(JobClient client, final ActivatedJob job) {
        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order_rider"), OrderDTO.class);
        log.info("set rider {} to available", order.getVehicleId());
        try {

            this.changeRiderStatus(order.getVehicleId(), true);

            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
        } catch (Exception e) {
            log.error(e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    public void setRiderStatusNotAvailable(JobClient client, final ActivatedJob job) {
        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order_rider"), OrderDTO.class);
        log.info("set rider {} to not available", order.getVehicleId());
        try {

            this.changeRiderStatus(order.getVehicleId(), false);
            ordersInProcess.remove(order.getOrderId());

            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();
        } catch (Exception e) {
            log.error(e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
