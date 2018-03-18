package net.playground.pubsub.amqp;

public class Notification {
    private String domain;
    private String environment;
    private String message;
    private long received;

    public Notification() {
    }

    public Notification(String domain, String environment, String message) {
        this.domain = domain;
        this.environment = environment;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "domain='" + domain + '\'' +
                ", environment='" + environment + '\'' +
                ", message='" + message + '\'' +
                ", received=" + received +
                '}';
    }

    // getters and setters
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getReceived() {
        return received;
    }

    public void setReceived(long received) {
        this.received = received;
    }
}
