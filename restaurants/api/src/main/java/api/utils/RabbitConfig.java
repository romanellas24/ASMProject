package api.utils;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public final static String ORDER_EXCHANGE="orders.exchange";
    public final static String WAITING_ORDERS_QUEUE="orders.waiting.queue";
    public final static String DECISIONS_QUEUE="orders.decisions.queue";
    public final static String DELETED_ORDERS_QUEUE="orders.deleted.queue";
    public final static String NEW_ORDERS_QUEUE="orders.new.queue";
    public final static String WAITING_ORDERS_ROUTING_KEY="order.new_waiting";
    public final static String DECISIONS_ROUTING_KEY ="order.new_decision";
    public final static String DELETED_ORDERS_ROUTING_KEY="order.deleted";
    public final static String NEW_ORDERS_ROUTING_KEY="order.new";
    public final static String TIMEOUT_ORDERS_QUEUE="orders.timeout";
    public final static String TIMEOUT_ORDERS_ROUTING_KEY="orders.timeout.key";



    @Bean
    public TopicExchange orderExchange() {return new TopicExchange(ORDER_EXCHANGE);}

    @Bean
    public Queue waitingOrdersQueue() {return new Queue(WAITING_ORDERS_QUEUE);}

    @Bean
    public Queue decisionsQueue() {return new Queue(DECISIONS_QUEUE);}

    @Bean
    public Queue deletedOrdersQueue() {return new Queue(DELETED_ORDERS_QUEUE);}

    @Bean
    public Queue newOrdersQueue() {return new Queue(NEW_ORDERS_QUEUE);}

    @Bean
    public Queue timeoutQueue() {return new Queue(TIMEOUT_ORDERS_QUEUE);}

    @Bean
    public Binding waitingOrderBinding(Queue waitingOrdersQueue, TopicExchange orderExchange){
        return BindingBuilder.bind(waitingOrdersQueue).to(orderExchange).with(WAITING_ORDERS_ROUTING_KEY);
    }

    @Bean
    public Binding decisionBinding(Queue decisionsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(decisionsQueue).to(exchange).with(DECISIONS_ROUTING_KEY);
    }

    @Bean
    public Binding deletedOrderBinding(Queue deletedOrdersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deletedOrdersQueue).to(exchange).with(DELETED_ORDERS_ROUTING_KEY);
    }

    @Bean
    public Binding newOrdersBinding(Queue newOrdersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(newOrdersQueue).to(exchange).with(NEW_ORDERS_ROUTING_KEY);
    }

    @Bean
    public Binding timeoutOrder(Queue timeoutQueue, TopicExchange exchange) {
        return BindingBuilder.bind(timeoutQueue).to(exchange).with(TIMEOUT_ORDERS_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        converter.setClassMapper(new DefaultJackson2JavaTypeMapper() {{
            setTrustedPackages("api.dto");
        }});

        return converter;
    }

    @Bean
    public MessageConverter messageConverter() {
        return jackson2JsonMessageConverter();
    }
}
