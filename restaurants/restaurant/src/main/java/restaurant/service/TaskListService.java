package restaurant.service;

import io.camunda.tasklist.dto.TaskSearch;
import restaurant.dto.TaskDecisionOrderDTO;

import java.util.List;

public interface TaskListService {
    void completeTask(String taskId, boolean complete);

    List<TaskDecisionOrderDTO> getWaitingOrderTasks() throws InterruptedException;
    List<TaskDecisionOrderDTO> getOrderToPrepareTasks() throws InterruptedException;
    TaskDecisionOrderDTO getOrderToPrepareFromId(Integer id) throws InterruptedException;
    List<TaskDecisionOrderDTO> getWaitingOrderTasksWithId(Integer id) throws InterruptedException;
}
