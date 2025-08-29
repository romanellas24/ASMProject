package api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    public final static String ORDER_EXCHANGE="orders.exchange";
    public final static String EVENT_QUEUE = "orders.queue";
    public final static String EVENT_ROUTING_KEY = "orders.key";

    @Bean
    public Queue ordersQueue() {
        return new Queue(EVENT_QUEUE);
    }

    @Bean
    public TopicExchange orderExchange() {return new TopicExchange(ORDER_EXCHANGE);}

    @Bean
    public Binding orderBinding(Queue ordersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(ordersQueue).to(exchange).with(EVENT_ROUTING_KEY);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
    @Bean
    public MessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
