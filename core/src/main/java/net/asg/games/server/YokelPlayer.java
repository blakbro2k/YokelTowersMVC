package net.asg.games.server;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelPlayer implements Json.Serializable{
    private final static int DEFAULT_RATING_NUMBER = 1500;
    private String name;
    private String playerId;
    private int rating;
    private String logo;
    private String sessionId;


    public YokelPlayer(String name){
        this.name = name;
        playerId = Util.IDGenerator.getID();
        rating = DEFAULT_RATING_NUMBER;
    }

    public YokelPlayer(){}

    public String getName(){
        return name;
    }

    public int getRating(){
        return rating;
    }

    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void increaseRating(int inc){
        rating += inc;
    }

    public void decreaseRating(int dec){
        rating -= dec;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("playerId", playerId);
        json.writeValue("rating", rating);
        json.writeValue("logo", logo);
        json.writeValue("sessionId", sessionId);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        name = json.readValue("name", String.class, jsonData);
        playerId = json.readValue("playerId", String.class, jsonData);
        rating = json.readValue("rating", Integer.class, jsonData);
        logo = json.readValue("logo", String.class, jsonData);
        setSessionId(json.readValue("sessionId", String.class, jsonData));
    }

    @Override
    public String toString(){
        return name;
    }
}
