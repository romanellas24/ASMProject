package api.workers;

import api.dto.CreateOrderDTO;
import api.dto.DishDTO;
import api.dto.OrderDTO;
import api.dto.OrderMappingDTO;
import api.exception.CompanyIdException;
import api.exception.InvalidDishId;
import api.service.DishService;
import api.service.MenuService;
import api.service.OrderService;
import api.utils.OrderStatus;
import api.utils.StringToDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderStatusJobWorker extends Worker {

    private final OrderService orderService;
    private final DishService dishService;

    public OrderStatusJobWorker(ObjectMapper objectMapper,
                                DishService dishService,
                                OrderService orderService,
                                @Value("${local.server.name}") String localName){
        super(localName, objectMapper);
        this.dishService = dishService;
        this.orderService = orderService;
    }

//    @JobWorker(type = "create_pending_order")
    public void handleCreatePendingOrder(final JobClient client, final ActivatedJob job) {
//
//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        log.info("Execution of create order in pending job for process #{}", job.getProcessInstanceKey());

        final CreateOrderDTO body = objectMapper.convertValue(job.getVariablesAsMap().get("body"), CreateOrderDTO.class);
        LocalDateTime dateTime = StringToDate.convertStringToLocalDateTime(body.getDeliveryTime());

        try {
            final Integer orderId = orderService.createOrder(body.getDishes(), dateTime);
            DishDTO[] dishes = dishService.getDishes(body.getDishIds());
            OrderDTO orderPendingDTO = OrderDTO.fromOrderRequest(body, dateTime, dishes, orderId);

            client.newCompleteCommand(job.getKey())
                    .variable("order", orderPendingDTO)
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

//    @JobWorker(type = "set_order_status_timed_out")
    public void handleSetOrderStatusTimedOut(final JobClient client, final ActivatedJob job) {
//
//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }
        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Execution of order timeout exception. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());

        try {
            orderService.updateOrderStatus(order.getId(), OrderStatus.TIMED_OUT);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure updating status for order {}: {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

//    @JobWorker(type = "set_order_status_rejected")
    public void handleSetOrderStatusRejected(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Order rejected. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());

        try {
            orderService.updateOrderStatus(order.getId(), OrderStatus.REJECTED);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure updating status for order {}: {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

//    @JobWorker(type = "set_order_status_accepted")
    public void handleSetOrderStatusAccepted(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Order accepted. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());

        try {
            orderService.updateOrderStatus(order.getId(), OrderStatus.ACCEPTED);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure updating status for order {}: {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

//    @JobWorker(type = "set_order_status_completed")
    public void handleSetOrderStatusCompleted(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Order completed. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());

        try {
            orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure updating status for order {}: {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

//    @JobWorker(type = "delete_order_company")
    public void deleteOrder(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final CreateOrderDTO body = objectMapper.convertValue(job.getVariablesAsMap().get("body"), CreateOrderDTO.class);
        final Integer id = body.getId();
        String companyName = body.getCompanyName();
        companyName = companyName.toLowerCase();
        log.info("Order deletion. Process: #{}, Order (company): {}, Company:{}", job.getProcessInstanceKey(), id, companyName);

        try {
            OrderMappingDTO mapping = orderService.getMapping(companyName, id);
            Boolean deleted = orderService.deleteOrder(mapping);
            client.newCompleteCommand(job.getKey())
                    .variable("deleted", deleted)
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure during deletion of order(company) {}: {}", id, e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
