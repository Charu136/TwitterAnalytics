package TwitterRankService;

public class SubscriptionStatus {
    String topic;
    String message;

    public SubscriptionStatus(String topic) {
        this.topic = topic;
        this.message = "Thank you. You have been subscribed to topic : " + topic;
    }

    @Override
    public String toString() {
        return "SubscriptionStatus{" +
                "topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
