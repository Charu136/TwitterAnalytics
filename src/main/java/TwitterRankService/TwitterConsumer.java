package TwitterRankService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;
import java.util.stream.Collectors;

public class TwitterConsumer {
    String sessionId;

    public TwitterConsumer(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Tweet> getLatestTweets() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", sessionId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        String topic = "tweets";
        consumer.subscribe(Arrays.asList(topic));
        List<Tweet> tweets = new LinkedList<>();
        long MAX_TWEETS = 50;
        long count = 0;
        long startTime = System.currentTimeMillis();
        Boolean timeOut = false;
        while (count <= MAX_TWEETS && !timeOut) {
            ConsumerRecords<String, String> records = consumer.poll(10);
            for (ConsumerRecord<String, String> record : records) {
                String tweetText = record.value();
                Tweet tweet = new Tweet(
                        getText(tweetText),
                        String.join(", ", getHashTags(tweetText)),
                        getRetweetCount(tweetText),
                        getFavoritesCount(tweetText)
                );
                tweets.add(tweet);
            }
            long timeElapsed = System.currentTimeMillis() - startTime;
            if (timeElapsed >= 15000) timeOut = true;
            count = tweets.size();
        }
        consumer.close();
        return tweets;
    }

    public String getText(String tweetText) {
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try {
            Map<String, Object> map = mapper.readValue(tweetText.getBytes(), Map.class);
            result = map.get("text").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public int getRetweetCount(String tweetText) {
        ObjectMapper mapper = new ObjectMapper();
        int result = 0;
        try {
            Map<String, Object> map = mapper.readValue(tweetText.getBytes(), Map.class);
            result = Integer.parseInt(map.get("retweet_count").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getFavoritesCount(String tweetText) {
        ObjectMapper mapper = new ObjectMapper();
        int result = 0;
        try {
            Map<String, Object> map = mapper.readValue(tweetText.getBytes(), Map.class);
            result = Integer.parseInt(map.get("favorite_count").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getHashTags(String tweetText) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> result = null;
        try {
            Map<String, LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>> map =
                    mapper.readValue(tweetText.getBytes(), Map.class);
            result = map.get("entities").get("hashtags")
                    .stream()
                    .map(h -> h.get("text"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null)
            return new ArrayList<>();
        else
            return result;
    }
}