package api.workers;

import api.dto.CreateOrderDTO;
import api.exception.CompanyIdException;
import api.exception.InvalidDishId;
import api.service.DishService;
import api.service.MenuService;
import api.service.OrderService;
import api.utils.StringToDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderStatusJobWorker {

    @Autowired
    private OrderService orderService;
    @Autowired
    private DishService dishService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    @JobWorker(type = "create_pending_order")
    public void handleCreatePendingOrder(final JobClient client, final ActivatedJob job) {
        log.info("Execution of create order in pending job for process #{}", job.getProcessInstanceKey());

        final CreateOrderDTO body = objectMapper.convertValue(job.getVariablesAsMap(), CreateOrderDTO.class);
        LocalDateTime dateTime = StringToDate.convertStringToLocalDateTime(body.getDeliveryTime());

        try {
            final Integer orderId = orderService.createOrder(body.getDishes(), dateTime);
            client.newCompleteCommand(job.getKey())
                    .variable("orderId", orderId)
                    .send()
                    .join();
        }catch (Exception e) {
            log.error("Error during creation of order for process: #{}: {}", job.getProcessInstanceKey(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
