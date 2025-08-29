package asm.couriers.courier_tracking;

import asm.couriers.courier_tracking.dto.OrderDTO;
import asm.couriers.courier_tracking.dto.TaskOrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.dto.*;
import io.camunda.tasklist.exception.TaskListException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TaskListService {

    private final CamundaTaskListClient camundaTaskListClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${local.server.name}")
    private String courierName;

    final private Pagination pagination = new Pagination().setPageSize(50);

    final private TaskSearch query = new TaskSearch()
            .setState(TaskState.CREATED)
            .setPagination(pagination);

    final private String FORM_ID_DELIVERY_DONE = "delivery_done";

    final private Integer maxRetries = 5;
    final private Integer msRetry = 1000;

    public TaskListService(CamundaTaskListClient camundaTaskListClient, ObjectMapper objectMapper) {
        this.camundaTaskListClient = camundaTaskListClient;
        this.objectMapper = objectMapper;
    }


    public void completeTask(String taskId) {
        log.info("Task {} completed with decision: {}", taskId);
        try {
            camundaTaskListClient.completeTask(taskId, Map.of());
            log.info("Task {} completed with success on Camunda.", taskId);
        } catch (TaskListException e) {
            log.error("Errore during loading decision {} on Camunda", taskId, e);
        }
    }

    public List<TaskOrderDTO> getOrderToDeliver(Integer riderId) {
        return getTasksByForm(FORM_ID_DELIVERY_DONE, riderId);
    }

//    public TaskOrderDTO getOrderToDeliverFromId(Integer id, Integer riderId) {
//        return this.getOrderToDeliver(riderId).stream().filter(task -> task.getOrder().getOrderId().equals(id)).findFirst().orElse(null);
//    }

    private List<TaskOrderDTO> getTasksByForm(String formIdOrderPrepare, Integer riderId) {
        try {
            TaskList tasks = this.getOpenTasks(riderId);

            return tasks
                    .getItems()
                    .stream()
                    .filter(
                            task ->task.getFormId() != null && task.getFormId().equals(formIdOrderPrepare)
                    )
                    .map(this::convertToCookTaskNotification)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
    }

    private TaskList getOpenTasks(Integer riderId){
        log.info("Retrieve Open cook tasks");
        TaskSearch query = this.query.setCandidateGroup("rider "+riderId+"-courier "+courierName);
        try {
            return camundaTaskListClient.getTasks(query);
        } catch (Exception e) {
            log.error("Retrieve Open cook tasks failed", e);
            throw new RuntimeException(e);
        }
    }


    private TaskOrderDTO convertToCookTaskNotification(Task task) {

        Variable orderVariable = task.getVariables().stream()
                .filter(v -> "order_rider".equals(v.getName()))
                .findFirst()
                .orElse(null);

        if (orderVariable == null) {
            log.warn("Task {} non contiene la variabile 'order_rider'.", task.getId());
            return null;
        }


        OrderDTO orderData = objectMapper.convertValue(orderVariable.getValue(), OrderDTO.class);

        return new TaskOrderDTO(task.getId(), orderData);
    }




    public TaskOrderDTO getTaskFromOrderIdAndRiderId(Integer orderId, Integer riderId) throws InterruptedException {
        Thread.sleep(msRetry);
        for (int i = 0; i < maxRetries; i++) {
            List<TaskOrderDTO> tasks = this.getOrderToDeliver(riderId);

            if (!tasks.isEmpty() && isOrderIdInTasks(tasks,orderId)) {
                return tasks.stream().filter(task->task.getOrder().getOrderId().equals(orderId)).findFirst().get();
            }

            Thread.sleep(msRetry);
        }
        return null;
    }


    private boolean isOrderIdInTasks(List<TaskOrderDTO> tasks, Integer id) {
        return tasks.stream().anyMatch(task ->
            task.getOrder() != null && id.equals(task.getOrder().getOrderId())
        );
    }



}