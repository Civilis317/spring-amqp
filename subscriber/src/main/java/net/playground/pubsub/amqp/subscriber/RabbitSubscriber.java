package net.playground.pubsub.amqp.subscriber;

import com.rabbitmq.client.Channel;
import net.playground.pubsub.amqp.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitSubscriber {
    private final Logger logger = LoggerFactory.getLogger(RabbitSubscriber.class);

    @Value("${msg.reject}")
    private boolean reject;

    private boolean requeue = false;

    @RabbitListener(queues = "${rabbitmqSubscription.boop-queue}")
    public void receiveBoopMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        logger.info(new String (message.getBody()));
        if (reject) {
            channel.basicReject(tag, requeue);
        } else {
            channel.basicAck(tag, requeue);
        }
    }

    @RabbitListener(queues = "${rabbitmqSubscription.botm-queue}")
    public void receiveBotmMessage(Notification notification, Channel channel,  @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        logger.info("Delivery tag: " + tag);
        logger.info(notification.toString());

        if (reject) {
            channel.basicReject(tag, requeue);
        } else {
            channel.basicAck(tag, requeue);
        }
    }

}
