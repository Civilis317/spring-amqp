package net.playground.pubsub.amqp.publisher;

import net.playground.pubsub.amqp.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitPublisher {
    private static final Logger logger = LoggerFactory.getLogger(RabbitPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Notification notification) {
        String exchangeName = "x_dochub";
        String routingKey = assembleRoutingKey(notification);
        logger.info("routing-key: {}", routingKey );
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notification);
    }

    private String assembleRoutingKey(Notification notification) {
        StringBuffer sb = new StringBuffer(1024);
        sb.append(notification.getEnvironment().toLowerCase())
                .append("_")
                .append(notification.getDomain().replace("-", "").toLowerCase());
        return sb.toString();
    }
}
