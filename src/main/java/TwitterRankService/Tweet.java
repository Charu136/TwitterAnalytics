package TwitterRankService;

import java.util.ArrayList;

/** Reduced representation of a tweet */
public class Tweet {
    public String text;
    public String hashtags;
    public int retweetCount;
    public int favoriteCount;

    public Tweet(String text, String hashtags, int retweetCount, int favoriteCount) {
        this.text = text;
        this.hashtags = hashtags;
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "text='" + text + '\'' +
                ", hashtags=" + hashtags +
                ", retweetCount=" + retweetCount +
                ", favoriteCount=" + favoriteCount +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
