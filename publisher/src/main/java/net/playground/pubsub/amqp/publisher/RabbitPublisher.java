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
        validate(notification);
        String exchangeName = "x_dochub";
        String routingKey = assembleRoutingKey(notification);
        logger.info("routing-key: {}", routingKey );
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notification);

        // second publication to json queue
        //rabbitTemplate.convertAndSend(exchangeName, "json-notification", notification);
    }

    private String assembleRoutingKey(Notification notification) {
        StringBuffer sb = new StringBuffer(1024);
        sb.append(notification.getEnvironment().toLowerCase())
                .append("_")
                .append(notification.getDomain().replace("-", "").toLowerCase());
        return sb.toString();
    }

    private void validate(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification may not be null");
        }

        if (notification.getDomain() == null || notification.getDomain().isEmpty()) {
            throw new IllegalArgumentException("Filed 'domain' may not be empty");
        }

        if (notification.getEnvironment() == null || notification.getEnvironment().isEmpty()) {
            throw new IllegalArgumentException("Filed 'environment' may not be empty");
        }
    }
}
