package api.workers;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WorkerConfiguration {
    @Autowired
    private ZeebeClient client;
    @Autowired
    private GeneralJobWorker generalJobWorker;
    @Autowired
    private NotificationJobWorker notificationJobWorker;
    @Autowired
    private OrderMappingJobWorker orderMappingJobWorker;
    @Autowired
    private OrderStatusJobWorker orderStatusJobWorker;

    @Value("${local.server.name}")
    private String localName;

    @PostConstruct
    public void defineWorker(){
        log.info("Configuring worker for {}", this.localName);

        client.newWorker()
                .jobType("is_local_closed-" + this.localName)
                .handler(generalJobWorker::addBoolIsLocalClosedToVariables)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("prepare_menu_send-" + this.localName)
                .handler(generalJobWorker::prepareMenuSend)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("notify_cook_new_order-" + this.localName)
                .handler(notificationJobWorker::notifyCookNewOrder)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("update_cook_order_timed_out-" + this.localName)
                .handler(notificationJobWorker::updateCookOrderTimedOut)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("notify_cook_order_todo-" + this.localName)
                .handler(notificationJobWorker::notifyCookOrderTodo)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("notify_order_deleted-" + this.localName)
                .handler(notificationJobWorker::notifyCookOrderDeleted)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("notify_cook_can_prepare-" + this.localName)
                .handler(notificationJobWorker::notifyCookCanPrepare)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("save_order_mapping-" + this.localName)
                .handler(orderMappingJobWorker::saveOrderMapping)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("create_pending_order-" + this.localName)
                .handler(orderStatusJobWorker::handleCreatePendingOrder)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("set_order_status_timed_out-" + this.localName)
                .handler(orderStatusJobWorker::handleSetOrderStatusTimedOut)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("set_order_status_rejected-" + this.localName)
                .handler(orderStatusJobWorker::handleSetOrderStatusRejected)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("set_order_status_accepted-" + this.localName)
                .handler(orderStatusJobWorker::handleSetOrderStatusAccepted)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("set_order_status_completed-" + this.localName)
                .handler(orderStatusJobWorker::handleSetOrderStatusCompleted)
                .name(this.localName)
                .open();

        client.newWorker()
                .jobType("delete_order_company-" + this.localName)
                .handler(orderStatusJobWorker::deleteOrder)
                .name(this.localName)
                .open();

    }

}
