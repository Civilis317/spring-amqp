package net.playground.pubsub.amqp.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "queueConfig")
public class RabbitConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfigurer.class);

    private String exchangeName;
    private List<String> queueNameList;

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public List<String> getQueueNameList() {
        return queueNameList;
    }

    public void setQueueNameList(List<String> queueNameList) {
        this.queueNameList = queueNameList;
    }

    private final RabbitAdmin rabbitAdmin;
    public RabbitConfigurer(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    public void createExchangesAndQueues() {
        DirectExchange x_dochub = new DirectExchange(exchangeName);
        DirectExchange dlx_dochub = new DirectExchange("dl" + exchangeName);
        rabbitAdmin.declareExchange(x_dochub);
        rabbitAdmin.declareExchange(dlx_dochub);
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", dlx_dochub.getName());
        queueNameList.stream().forEach(s -> createQueue(s, x_dochub, dlx_dochub, arguments));
    }

    private void createQueue(String queueName, DirectExchange exchange, DirectExchange deadLetterExchange, Map<String, Object> arguments) {
        String routingKey = queueName.substring(2);
        Queue deadLetterQueue = new Queue("dl" + queueName, true, false, false, null);
        rabbitAdmin.declareQueue(deadLetterQueue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(routingKey));
        arguments.put("x-dead-letter-routing-key", routingKey);
        Queue queue = new Queue(queueName, true, false, false, arguments);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey));
    }
}
