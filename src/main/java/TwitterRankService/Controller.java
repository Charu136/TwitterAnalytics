package TwitterRankService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    String sessionId = UUID.randomUUID().toString();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/subscribe")
    public SubscriptionStatus subscribe(@RequestParam(value="topic") String topic) {
        Subscription subscription = new Subscription(topic);
        subscription.startTracking();
        return new SubscriptionStatus(topic);
    }

    @RequestMapping("/retweets")
    public List<Tweet> trendingByRetweets(@RequestParam(value="topic") String topic) {
        TrendingTweets tt = null;
        try {
            tt = new TrendingTweets(topic, sessionId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Tweet> result = tt.getTweetsByRetweets();
        if (result.isEmpty()) System.out.println("No new tweets!!");
        return result;
    }

    @RequestMapping("/favorites")
    public List<Tweet> trendingByFavorites(@RequestParam(value="topic") String topic) {
        TrendingTweets tt = null;
        try {
            tt = new TrendingTweets(topic, sessionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Tweet> result = tt.getTweetsByFavorites();
        if (result.isEmpty()) System.out.println("No new tweets!!");
        return result;
    }

}
