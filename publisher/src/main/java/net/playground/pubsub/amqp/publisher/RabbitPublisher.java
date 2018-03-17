package net.playground.pubsub.amqp.publisher;

import net.playground.pubsub.amqp.Notification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Notification notification) {
        String exchangeName = "test-exchange";
        String routingKey = "test-key";
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notification);

        // second publication to json queue
        rabbitTemplate.convertAndSend(exchangeName, "json-notification", notification);
    }
}
