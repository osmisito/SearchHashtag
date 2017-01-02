package com.yaozli.searchhashtag.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yaozli.searchhashtag.dto.TweetDTO;

import java.util.ArrayList;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil
{
    public ArrayList<TweetDTO> getTweets(String ht)
    {
        ArrayList<TweetDTO> dto = new ArrayList<>();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("CMtlPU2noiVE9OgHiEGn2LhrP")
                .setOAuthConsumerSecret("lGStRQ8HII9jmwbV5JjupFleL1P32IvRMnXNJmN5xkugYPLkUf")
                .setOAuthAccessToken("814661288454483968-nuuRMe0rOdOGEzVVFpN95QRVEVIYNip")
                .setOAuthAccessTokenSecret("HzMVhHlrjd75iIMl5ull7k56hrpb0MUvCa01l5g9yYMsd");
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        Query query = new Query(ht);
        int numberOfTweets = 5;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        while (tweets.size () < numberOfTweets) {

            query.setCount(5);

            try {
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                System.out.println("Gathering " + tweets.size() + " tweets"+"\n");
                if(tweets.size() == 0){
                    break;
                }
                for (Status t: tweets)
                    if(t.getId() < lastID)
                        lastID = t.getId();

            }

            catch (TwitterException te) {
                System.out.println("No se puede conectar: " + te);
            }
            query.setMaxId(lastID-1);
        }

        for (int i = 0; i < tweets.size(); i++) {
            Status t = (Status) tweets.get(i);

            TweetDTO aux = new TweetDTO();
            aux.setContent(t.getText());
            aux.setUsername("@"+t.getUser().getScreenName());
            aux.setImageString(t.getUser().getProfileImageURL());
            aux.setImage(ImageUtil.getBitmapFromUrl(t.getUser().getProfileImageURL()));


            dto.add(aux);
        }
        return dto;
    }
}