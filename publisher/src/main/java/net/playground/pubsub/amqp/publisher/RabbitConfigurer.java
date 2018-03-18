package net.playground.pubsub.amqp.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfigurer.class);

    private final RabbitAdmin rabbitAdmin;

    public RabbitConfigurer(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    public void createExchangesAndQueues() {
        // dead letter exchange and queues
        DirectExchange dlx_dochub = new DirectExchange("dlx_dochub");
        rabbitAdmin.declareExchange(dlx_dochub);
        createQueue(dlx_dochub, "dlq_dev_boop", "dev_boop", null);
        createQueue(dlx_dochub, "dlq_dev_botm", "dev_botm", null);
        createQueue(dlx_dochub, "dlq_dev_bods", "dev_bods", null);

        // main exchange and queues
        DirectExchange x_dochub = new DirectExchange("x_dochub");
        rabbitAdmin.declareExchange(x_dochub);
        createQueue(x_dochub, "q_dev_boop", "dev_boop", "dlx_dochub");
        createQueue(x_dochub, "q_dev_botm", "dev_botm", "dlx_dochub");
        createQueue(x_dochub, "q_dev_bods", "dev_bods", "dlx_dochub");
    }

    private void createQueue(DirectExchange exchange, String name, String routingKey, String deadLetterExchangeName) {
        Map<String, Object> arguments = new HashMap<>();
        if (deadLetterExchangeName != null) {
            arguments.put("x-dead-letter-exchange", deadLetterExchangeName);
            arguments.put("x-dead-letter-routing-key", routingKey);
        }
        Queue queue = new Queue(name, true, false, false, arguments);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey));
    }


}
