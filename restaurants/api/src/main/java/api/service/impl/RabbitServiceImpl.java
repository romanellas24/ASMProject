package api.service.impl;

import api.dto.*;
import api.service.OrderService;
import api.service.RabbitService;
import api.utils.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitServiceImpl implements RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderService orderService;

    @Override
    public void publishEvent(WebSocketEventDTO webSocketEventDTO) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.EVENT_ROUTING_KEY,
                webSocketEventDTO
        );
    }
}
