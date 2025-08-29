package asm.couriers.courier_allocation.worker;

import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.service.MapsService;
import asm.couriers.courier_allocation.utils.Const;
import asm.couriers.courier_allocation.utils.StringToLocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class AllocationWorker {

    private final MapsService mapsService;
    private final AllocationService allocationService;
    private final ObjectMapper objectMapper;
    private final String courierName;
    private final ZeebeClient zeebeClient;

    public AllocationWorker(@Value("${local.server.name}") String courierName,
                            ObjectMapper objectMapper,
                            MapsService mapsService,
                            AllocationService allocationService, ZeebeClient zeebeClient) {
        this.mapsService = mapsService;
        this.allocationService = allocationService;
        this.objectMapper = objectMapper;
        this.courierName = courierName;
        this.zeebeClient = zeebeClient;
    }

    public void calculateDistanceLocalClient(JobClient client, final ActivatedJob job) {

        final RequestAvailDTO request = objectMapper.convertValue(job.getVariablesAsMap().get("request"), RequestAvailDTO.class);

        try {
            AvailabilityDTO availabilityDTO = mapsService.getInfoDelivery(request.getLocalAddress(),request.getUserAddress());
            client.newCompleteCommand(job.getKey())
                    .variable("availability", availabilityDTO)
                    .send()
                    .join();

        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    public void checkVehicleAvailability(JobClient client, final ActivatedJob job) {

        final AvailabilityDTO availDTO = objectMapper.convertValue(job.getVariablesAsMap().get("availability"), AvailabilityDTO.class);
        final LocalDateTime dateTime = objectMapper.convertValue(job.getVariablesAsMap().get("dateTime"), LocalDateTime.class);

        try {


            Integer vehicleId = allocationService.vehicle_available(availDTO.getTime(), dateTime);
            availDTO.setIsVehicleAvailable(true);
            availDTO.setVehicleId(vehicleId);
        } catch (VehicleNotAvailableException e) {
            log.info("vehicles are not available");
            availDTO.setIsVehicleAvailable(false);
        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
            return;
        }

        availDTO.setPrice(availDTO.getDistance() * Const.PRICE_PER_KM);

        client.newCompleteCommand(job.getKey())
                .variables(Map.of("availability", availDTO))
                .send()
                .join();
    }

    public void allocateNewVehicle(JobClient client, final ActivatedJob job) {

        final CompanyDTO companyDTO = objectMapper.convertValue(job.getVariablesAsMap().get("company"), CompanyDTO.class);
        final RequestAllocateDTO requestAllocateDTO = objectMapper.convertValue(job.getVariablesAsMap().get("request"), RequestAllocateDTO.class);

        try {
            OrderInfoDTO order = allocationService.allocate_vehicle(requestAllocateDTO, companyDTO);

            client.newCompleteCommand(job.getKey())
                    .variable("order", order)
                    .send()
                    .join();

        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }

    }

    public void checkIfOrderCanBeDeleted(JobClient client, final ActivatedJob job) {
        final Integer idToDelete = objectMapper.convertValue(job.getVariablesAsMap().get("idToDelete"), Integer.class);

        try {
            OrderInfoDTO order = allocationService.getOrder(idToDelete);
            long deliveryTime =   Duration.between(order.getEndDeliveryTime(), order.getStartDeliveryTime()).toMinutes();
            boolean canBeDeleted;
            canBeDeleted = !LocalDateTime.now().isAfter(order.getStartDeliveryTime().minusMinutes(deliveryTime));

            client.newCompleteCommand(job.getKey())
                    .variable("canBeDeleted", canBeDeleted)
                    .send()
                    .join();

        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    public void deleteOrder(JobClient client, final ActivatedJob job) {
        final Integer idToDelete = objectMapper.convertValue(job.getVariablesAsMap().get("idToDelete"), Integer.class);
        try {
            boolean deleted = allocationService.delete_order(idToDelete);

            DeleteInfoDTO deleteInfoDTO = new DeleteInfoDTO();
            deleteInfoDTO.setOrderId(idToDelete);
            deleteInfoDTO.setDeleted(deleted);

            client.newCompleteCommand(job.getKey())
                    .variable("result", deleteInfoDTO)
                    .send()
                    .join();

        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    public void isVehicleAvailable(JobClient client, final ActivatedJob job) {
        final RequestAllocateDTO requestAllocateDTO = objectMapper.convertValue(job.getVariablesAsMap().get("request"), RequestAllocateDTO.class);
        final Integer vehicleId = requestAllocateDTO.getVehicle();
        LocalDateTime dateTimeDelivery = StringToLocalDateTime.convertStringToLocalDateTime(requestAllocateDTO.getExpectedDeliveryTime());

        try {
            boolean available = allocationService.isVehicleAvailable(vehicleId, requestAllocateDTO.getTimeMinutes(), dateTimeDelivery);
            if (available){
                client.newCompleteCommand(job.getKey())
                        .variable("available", true)
                        .send()
                        .join();
            } else {
                zeebeClient.newSetVariablesCommand(job.getElementInstanceKey())
                        .variables(Map.of("available", false))
                        .send()
                        .join();

                client.newThrowErrorCommand(job.getKey())
                        .errorCode("INVALID_VEHICLE_ID")
                        .errorMessage("Invalid vehicle id")
                        .send()
                        .join();
            }


        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
