package asm.couriers.courier_allocation.worker;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WorkerConfiguration {
    private final ZeebeClient zeebe;
    private final String courierName;
    private final AllocationWorker allocationWorker;


    public WorkerConfiguration(ZeebeClient zeebe,
                               AllocationWorker allocationWorker,
                               @Value("${local.server.name}") String courierName) {
        this.courierName = courierName;
        this.allocationWorker = allocationWorker;
        this.zeebe = zeebe;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing worker configuration for courier {}", this.courierName);
        zeebe.newWorker()
                .jobType("calculate_distance_local_client-"+ this.courierName)
                .handler(allocationWorker::calculateDistanceLocalClient)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("check_vehicle_availability-"+ this.courierName)
                .handler(allocationWorker::checkVehicleAvailability)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("allocate_new_vehicle-"+ this.courierName)
                .handler(allocationWorker::allocateNewVehicle)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("delete_order_check_time-"+ this.courierName)
                .handler(allocationWorker::checkIfOrderCanBeDeleted)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("delete_order-"+ this.courierName)
                .handler(allocationWorker::deleteOrder)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("is_vehicle_available-"+ this.courierName)
                .handler(allocationWorker::isVehicleAvailable)
                .name(courierName)
                .open();
    }
}
