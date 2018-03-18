package net.playground.pubsub.amqp.publisher;

import net.playground.pubsub.amqp.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/send")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    final RabbitPublisher publisher;

    public NotificationController(RabbitPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/notification")
    public @ResponseBody Notification sendNotification(@RequestBody Notification notification) {
        notification.setReceived(System.currentTimeMillis());
        publisher.publish(notification);
        logger.info(notification.toString());
        return notification;
    }

    @GetMapping("example")
    public @ResponseBody
    Notification getExampleNotification() {
        return new Notification("BO-OP", "dev", "Ajax Kampioen!");
    }

}
