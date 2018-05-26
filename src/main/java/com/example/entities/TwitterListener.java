package com.example.entities;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import twitter4j.*;

import javax.annotation.PostConstruct;

@Service
@Configurable
public class TwitterListener {
    @Autowired
    private TwitterStream twitterStream;
    @Autowired
    private MongoTemplate mongo;

    MongoClient mongoo = new MongoClient("138.197.128.130",27017);
    DB database = mongoo.getDB("twitter7");
    DBCollection collection = database.getCollection("futbol");
    @PostConstruct
    public void run() {
        twitterStream.addListener(new StatusListener() {
            public void onStatus(Status status) {
                String ubicacion=status.getUser().getLocation();
                if (ubicacion.indexOf("Chile")>0) {
                    //System. out. println(ubicacion);

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

        FilterQuery filter=new FilterQuery();
        filter.track(new String[]{"Católica","La UC", "cruzados", "la franja", "Azul y Blanco","Caballeros Cruzados", "la Cato",
                "El campanil", "u de conce"," la u penquista", "auricielos"," los del foro","Foreros",
                "La U", "la Chile", "el bulla"," el león", "chuncho", "azul", "leon", "bullanguero"," romántico viajero", "azules",

                "Colo Colo",
                "Cacique", "Indio", "colocolino", "albo", "garrero", "el popular", "eterno campeón","la ruca","cacike",

                "Unión la Calera",
                "Cementeros", "maquina cementera", "la calera",

                "Antofagasta",
                "Pumas", "albiceleste",
                "Unión española",
                "Hispanos", "furia roja", "rojos de Santa Laura",
                "O’higgins",
                "Minero", "celeste", "capo de provincia", "rancagüinos",
                "Huachipato",
                "Acerero", "siderúrgicos",
                "Iquique",
                "Dragón", "celeste",
                "Palestino",
                "Árabe"," paisano", "tricolore", "tino-tino",
                "Curicó unido",
                "Tortero", "albirrojo"," curi", "Cuadro Tortero",
                "San luis",
                "Canario", "amarillo",
                "Audax italiano",
                "Audino", "verde", "tano", "itálico",
                "Everton",
                "Evertoniano", "ruletero", "auriazul",
                "Temuco",
                "Albiverde", "león de ñielol", "el pije"," el histórico", "temucanos", "la cruz de malta"}

        );
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

