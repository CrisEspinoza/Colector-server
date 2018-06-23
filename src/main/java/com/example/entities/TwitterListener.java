package com.example.entities;


import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import twitter4j.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@Configurable
public class TwitterListener {
    @Autowired
    private TwitterStream twitterStream;
    @Autowired
    private MongoTemplate mongo;

    MongoCredential credential = MongoCredential.createCredential("TBDG7", "TBDG7", "Antihakers".toCharArray());
    MongoClient mongoClient = new MongoClient();

    DB database = mongoClient.getDB("twitter7");
    DBCollection collection = database.getCollection("futbol");
    @PostConstruct
    public void run() {
        twitterStream.addListener(new StatusListener() {
            public void onStatus(Status status) {

                String ubicacion=status.getUser().getLocation();


                if (ubicacion.indexOf("Chile")>0) {
//                    System.out.println(status.toString());


                    BasicDBObject tweet;
                    tweet = new BasicDBObject("id", status.getId())
                            .append("text", status.getText())
                            .append("like", status.getFavoriteCount())
                            .append("geoLocation", status.getGeoLocation())
                            .append("retweet", status.getRetweetCount())
                            .append("locationUser", status.getUser().getLocation())
                            .append("name", status.getUser().getName())
                            .append("followers", status.getUser().getFollowersCount());
                    collection.insert(tweet);

                    //System. out. println(ubicacion);

                }

            }

            @Override
            public void onException(Exception arg0) {

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {

            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {

            }

            @Override
            public void onStallWarning(StallWarning arg0) {

            }

            @Override
            public void onTrackLimitationNotice(int arg0) {

            }
        });
        //System.out.println("llegue aca ");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Club[]> response =
                restTemplate.getForEntity("http://159.65.128.52:8080/TBD-G7/club",Club[].class);
        Club[] clubs=response.getBody();
        //System.out.println(clubs[1]);
        ArrayList<String> acumulador = new ArrayList<String>();
//        System.out.println("llegue aca 1");
        for (Club c: clubs) {
//           System.out.println("llegue aca 1.5");
            acumulador.add(c.getName());
//            System.out.println(c.getName());
            for (Keyword k:c.getKeywords()) {
                acumulador.add(k.getName_keyword());
             //   System.out.println(k.getName_keyword());

            }

        }
        //System.out.println("llegue aca 2");
        String[] filtro = new String[acumulador.size()];
        filtro= acumulador.toArray(filtro);
//        for (String c: filtro
//             ) {
//            System.out.println(c);
//
//        }
        FilterQuery filter=new FilterQuery();
        filter.track( filtro);
        filter.language(new String[]{"es"});
        twitterStream.filter(filter);
    }

    public TwitterStream getTwitterStream() {
        return twitterStream;
    }

    public void setTwitterStream(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    public MongoTemplate getMongo() {
        return mongo;
    }

    public void setMongo(MongoTemplate mongo) {
        this.mongo = mongo;
    }
}

