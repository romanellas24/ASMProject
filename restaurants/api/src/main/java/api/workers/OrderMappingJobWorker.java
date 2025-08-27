package api.workers;

import api.dto.CreateOrderDTO;
import api.dto.OrderDTO;
import api.dto.OrderMappingDTO;
import api.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderMappingJobWorker extends Worker {
    private final OrderService orderService;

    public OrderMappingJobWorker(@Value("${local.server.name}") String localName,
                                 ObjectMapper objectMapper,
                                 OrderService orderService) {
        super(localName, objectMapper);
        this.orderService = orderService;
    }

    public void saveOrderMapping(final JobClient client, final ActivatedJob job){


        final CreateOrderDTO body = objectMapper.convertValue(job.getVariablesAsMap().get("body"), CreateOrderDTO.class);
        final OrderDTO order = objectMapper.convertValue(job.getVariablesAsMap().get("order"), OrderDTO.class);
        OrderMappingDTO orderMappingDTO = new OrderMappingDTO();
        orderMappingDTO.setCompanyName(body.getCompanyName());
        orderMappingDTO.setCompanyId(body.getId());
        orderMappingDTO.setOrderId(order.getId());


        try {
            orderService.saveMapping(orderMappingDTO);
            client.newCompleteCommand(job.getKey())
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure during saving mapping for order {}: {}", order.getId(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
