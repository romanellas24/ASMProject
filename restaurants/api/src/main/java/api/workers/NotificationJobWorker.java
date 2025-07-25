package api.workers;

import api.dto.OrderDTO;
import api.dto.WebSocketEventDTO;
import api.service.RabbitService;
import api.utils.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class NotificationJobWorker extends Worker {


    private final RabbitService rabbitService;

    public NotificationJobWorker(@Value("${local.server.name}") String localName,
                                 ObjectMapper objectMapper,
                                 RabbitService rabbitService) {
        super(localName, objectMapper);
        this.rabbitService = rabbitService;
    }

//    @JobWorker(type = "notify_cook_new_order")
    public void notifyCookNewOrder(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        log.info("Update dashboard to notify cook of new order. Process #{}", job.getProcessInstanceKey());

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_WAITING, Map.of("orderId", order.getId()));

        try{
            rabbitService.publishEvent(event);

            log.info("Send event of new order waiting. Process #{}", job.getProcessInstanceKey());
            client.newCompleteCommand(job.getKey())
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

//    @JobWorker(type = "update_cook_order_timed_out")
    public void updateCookOrderTimedOut(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Notify cook of order timed out. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());

        WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_TIMED_OUT, Map.of("orderId", order.getId()));

        try {
            rabbitService.publishEvent(event);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        }catch (Exception e) {
            log.error("Failure during notification for order {} timed out : {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

//    @JobWorker(type = "notify_cook_order_todo")
    public void notifyCookOrderTodo(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Notify cook of order todo. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());

        WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_TODO, Map.of("order", order));

        try {
            rabbitService.publishEvent(event);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        }catch (Exception e) {
            log.error("Failure during notification for order {} todo : {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

//    @JobWorker(type = "notify_order_deleted")
    public void notifyCookOrderDeleted(final JobClient client, final ActivatedJob job){

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Notify cook of order deleted. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());
        WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_DELETED, Map.of("orderId", order.getId()));

        try {
            rabbitService.publishEvent(event);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        }catch (Exception e) {
            log.error("Failure during notification for order {} deleted : {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

//    @JobWorker(type = "notify_cook_can_prepare")
    public void notifyCookCanPrepare(final JobClient client, final ActivatedJob job) {

//        if (!isJobForThisWorker(job)){
//            ignoreJob(client, job);
//            return;
//        }

        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        log.info("Notify cook to prepare order. Process: #{}, Order: {}", job.getProcessInstanceKey(), order.getId());
        WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_CAN_START, Map.of("orderId", order.getId()));

        try {
            rabbitService.publishEvent(event);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        }catch (Exception e) {
            log.error("Failure during notification for order {} started : {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

}
