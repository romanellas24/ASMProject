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

//    @Value("${rabbitmq.exchange}")
//    public static String ORDER_EXCHANGE;
//
//    @Value("${rabbitmq.queue.waiting_orders}")
//    public String WAITING_ORDERS_QUEUE;
//
//    @Value("${rabbitmq.queue.decisions}")
//    public static String DECISIONS_QUEUE;
//
//    @Value("${rabbitmq.routing.waiting_orders}")
//    public static String WAITING_ORDERS_ROUTING_KEY;
//
//    @Value("${rabbitmq.routing.decisions}")
//    public static String DECISIONS_ROUTING_KEY;


    public final static String ORDER_EXCHANGE="orders.exchange";
    public final static String WAITING_ORDERS_QUEUE="orders.waiting.queue";
    public final static String DECISIONS_QUEUE="orders.decisions.queue";
    public final static String WAITING_ORDERS_ROUTING_KEY="order.new_waiting";
    public final static String DECISIONS_ROUTING_KEY ="order.new_decision";



    @Bean
    public TopicExchange orderExchange() {return new TopicExchange(ORDER_EXCHANGE);}

    @Bean
    public Queue waitingOrdersQueue() {return new Queue(WAITING_ORDERS_QUEUE);}

    @Bean
    public Queue decisionsQueue() {return new Queue(DECISIONS_QUEUE);}

    @Bean
    public Binding waitingOrderBinding(Queue waitingOrdersQueue, TopicExchange orderExchange){
        return BindingBuilder.bind(waitingOrdersQueue).to(orderExchange).with(WAITING_ORDERS_ROUTING_KEY);
    }

    @Bean
    public Binding decisionBinding(Queue decisionsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(decisionsQueue).to(exchange).with(DECISIONS_ROUTING_KEY);
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
