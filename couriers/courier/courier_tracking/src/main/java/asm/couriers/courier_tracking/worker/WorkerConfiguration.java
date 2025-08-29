package asm.couriers.courier_tracking.worker;


import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WorkerConfiguration {

    private final ZeebeClient zeebe;
    private final TrackingWorker trackingWorker;
    private final String courierName;

    public WorkerConfiguration(ZeebeClient zeebe,
                               TrackingWorker trackingWorker,
                               @Value("${local.server.name}") String courierName) {
        this.zeebe = zeebe;
        this.trackingWorker = trackingWorker;
        this.courierName = courierName;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing worker configuration for courier {}", courierName);

        zeebe.newWorker()
                .jobType("get_lists_rider_starting-"+courierName)
                .handler(trackingWorker::checkDeliveryStart)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("notify_order-"+courierName)
                .handler(trackingWorker::notifyRider)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("prepare_notification-"+courierName)
                .handler(trackingWorker::prepareNotification)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("set_rider_status_available-"+courierName)
                .handler(trackingWorker::setRiderStatusAvailable)
                .name(courierName)
                .open();

        zeebe.newWorker()
                .jobType("set_rider_status_not_available-"+courierName)
                .handler(trackingWorker::setRiderStatusNotAvailable)
                .name(courierName)
                .open();
    }
}
