package net.playground.pubsub.amqp.publisher;

import net.playground.pubsub.amqp.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        validate(notification);
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

    private void validate(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification may not be null");
        }

        if (notification.getDomain() == null || notification.getDomain().isEmpty()) {
            throw new IllegalArgumentException("Field 'domain' may not be empty");
        }

        if (notification.getEnvironment() == null || notification.getEnvironment().isEmpty()) {
            throw new IllegalArgumentException("Field 'environment' may not be empty");
        }
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

}
