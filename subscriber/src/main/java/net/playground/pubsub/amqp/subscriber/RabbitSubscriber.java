package net.playground.pubsub.amqp.subscriber;

import net.playground.pubsub.amqp.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitSubscriber {
    private final Logger logger = LoggerFactory.getLogger(RabbitSubscriber.class);

    @RabbitListener(queues = "${rabbitmqSubscription.queue}")
    public void receiveMessage(Notification notification) {
        logger.info("Object: " + notification.toString());
    }

    @RabbitListener(queues = "${rabbitmqSubscription.json-queue}")
    public void receiveMessage(final Message message) {
        logger.info("Json: " + new String (message.getBody()));
    }

}
