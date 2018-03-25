package TwitterRankService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrendingTweets {

    List<Tweet> totalTweetsConsumed;
    String topic;


    public TrendingTweets(String topic, String sessionId) throws IOException {
        TwitterConsumer consumer = new TwitterConsumer(sessionId);
        totalTweetsConsumed = consumer.getLatestTweets().stream()
                .filter(t -> t.getText().contains(topic))
                .collect(Collectors.toList());
    }

    public List<Tweet> getTweetsByRetweets(){
        long POPULARITY_THRESHOLD = 0;
        return totalTweetsConsumed.stream()
                .filter(t -> t.getRetweetCount() >= POPULARITY_THRESHOLD)
                .collect(Collectors.toList());
    }

    public List<Tweet> getTweetsByFavorites(){
        long POPULARITY_THRESHOLD = 0;
        return totalTweetsConsumed.stream()
                .filter(t -> t.getFavoriteCount() >= POPULARITY_THRESHOLD)
                .collect(Collectors.toList());
    }
}
