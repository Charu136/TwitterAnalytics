package TwitterRankService;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Subscription {
    private enum STATUS_VALUES {TRACKING, FAILED, STARTING};
    private STATUS_VALUES status;
    private String topic;

    public Subscription(String topic) {
        this.topic = topic;
    }

    public void startTracking(){
        status = STATUS_VALUES.STARTING;
        try {
            trackInterest(topic);
        } catch (InterruptedException e) {
            status = STATUS_VALUES.FAILED;
            e.printStackTrace();
        }
    }
    @JsonIgnore
    public void trackInterest(String interest) throws InterruptedException {
        String consumerKey = "duIhPwW3F2kaz9mXzwNxxN2KJ",
                consumerSecret = "xfhtaUodglWyvieAMj0aGphwC1F7CxbTkMnrbADBkDqOEAg2Sh",
                token = "65652415-3uklQGPIH9xlatWVs1O30mxoY6KQxnfG6qUx5V65u",
                secret = "WFfyM004fhimxEFJNkg82v0FT0IpxHjxAa08qV0oZVU6v";
        Runnable r = () -> {
            try {
                new TwitterProducer().run(consumerKey, consumerSecret, token, secret, interest);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(r);
        status = STATUS_VALUES.TRACKING;
    }
}
