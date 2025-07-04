package restaurant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.dto.*;
import io.camunda.tasklist.exception.TaskListException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.dto.OrderDTO;
import restaurant.dto.TaskDecisionOrderDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskListServiceImpl implements TaskListService {

    private final CamundaTaskListClient camundaTaskListClient;

    @Autowired
    private ObjectMapper objectMapper;

    final private Pagination pagination = new Pagination().setPageSize(50);

    final private TaskSearch query = new TaskSearch()
            .setState(TaskState.CREATED)
            .setPagination(pagination);

    final private String FORM_ID_WAITING_ORDER = "accept_or_not_order";
    final private String FORM_ID_ORDER_PREPARE = "prepare_order";

    final private Integer maxRetries = 5;
    final private Integer msRetry = 1000;

    public TaskListServiceImpl(CamundaTaskListClient camundaTaskListClient, ObjectMapper objectMapper) {
        this.camundaTaskListClient = camundaTaskListClient;
        this.objectMapper = objectMapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;
    }

    @Override
    public void completeTask(String taskId, boolean accepted) {
        log.info("Task {} completed with decision: {}", taskId, accepted);
        try {
            Map<String, Object> variables = Map.of("accepted", accepted);
            camundaTaskListClient.completeTask(taskId, variables);
            log.info("Task {} completed with success on Camunda.", taskId);
        } catch (TaskListException e) {
            log.error("Errore during loading decision {} on Camunda", taskId, e);
        }
    }

    @Override
    public List<TaskDecisionOrderDTO> getWaitingOrderTasks() {
        return getTasksByForm(FORM_ID_WAITING_ORDER);
    }

    @Override
    public List<TaskDecisionOrderDTO> getOrderToPrepareTasks() {
        return getTasksByForm(FORM_ID_ORDER_PREPARE);
    }

    @Override
    public TaskDecisionOrderDTO getOrderToPrepareFromId(Integer id) {
        return this.getOrderToPrepareTasks().stream().filter(task -> task.getOrderData().getId().equals(id)).findFirst().orElse(null);
    }

    private List<TaskDecisionOrderDTO> getTasksByForm(String formIdOrderPrepare) {
        try {
            TaskList tasks = this.getOpenTasks();
            List<TaskDecisionOrderDTO> list = tasks
                    .getItems()
                    .stream()
                    .filter(
                            task -> isCookTask(task) /* && task.getFormId() != null && task.getFormId().equals(formIdOrderPrepare)*/
                    )
                    .map(this::convertToCookTaskNotification)
                    .toList();

            return tasks
                    .getItems()
                    .stream()
                    .filter(
                            task -> isCookTask(task) && task.getFormId() != null && task.getFormId().equals(formIdOrderPrepare)
                    )
                    .map(this::convertToCookTaskNotification)
                    .toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
    }

    private TaskList getOpenTasks(){
        log.info("Retrieve Open cook tasks");
        try {
            return camundaTaskListClient.getTasks(query);
        } catch (Exception e) {
            log.error("Retrieve Open cook tasks failed", e);
            throw new RuntimeException(e);
        }
    }

    private boolean isCookTask(Task task) {
        return task!=null && task.getVariables().stream().anyMatch(variable -> "order".equals(variable.getName()));
    }

    private TaskDecisionOrderDTO convertToCookTaskNotification(Task task) {

        Variable orderVariable = task.getVariables().stream()
                .filter(v -> "order".equals(v.getName()))
                .findFirst()
                .orElse(null);

        if (orderVariable == null) {
            log.warn("Task {} non contiene la variabile 'orderRequest'.", task.getId());
            return null;
        }


        OrderDTO orderData = objectMapper.convertValue(orderVariable.getValue(), OrderDTO.class);

        return new TaskDecisionOrderDTO(task.getId(), orderData);
    }



    @Override
    public List<TaskDecisionOrderDTO> getWaitingOrderTasksWithId(Integer id) throws InterruptedException {
        for (int i = 0; i < maxRetries; i++) {
            List<TaskDecisionOrderDTO> tasks = this.getWaitingOrderTasks();

            if (!tasks.isEmpty() && isOrderIdInTasks(tasks,id)) {
                return tasks;
            }

            Thread.sleep(msRetry);
        }
        return List.of();
    }


    private boolean isOrderIdInTasks(List<TaskDecisionOrderDTO> tasks, Integer id) {
        return tasks.stream().anyMatch(task ->
            task.getOrderData() != null && id.equals(task.getOrderData().getId())
        );
    }



}