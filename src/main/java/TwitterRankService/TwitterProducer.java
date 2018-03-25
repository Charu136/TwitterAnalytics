package TwitterRankService;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * TwitterRankService.TwitterProducer class that connects to twitter API and creates Kafka messages to the topic
 */
public class TwitterProducer {

    public static final String topic = "tweets";

    public void run(String consumerKey, String consumerSecret,
                           String token, String secret, String interest) throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Queue capacity for buffering tweets
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        // track tweets with specific hashtags.
        // TODO: should be optional
        endpoint.trackTerms(Lists.newArrayList(interest));

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();

        client.connect();
        Boolean keepRunning = true;
        // Send first 1000 messages from the twitter end point
        while(keepRunning){
        for (int msgRead = 0; msgRead < 1000; msgRead++) {
            String message = "";
            try {
                message = queue.take();
                System.out.println("Message --> " + message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                keepRunning = false;
            }
            producer.send(new ProducerRecord<>(topic, message));
        }
            Thread.sleep(10000);
        }
        producer.close();
        client.stop();
    }
}

