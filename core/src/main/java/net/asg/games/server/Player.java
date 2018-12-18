package net.asg.games.server;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class Player implements Json.Serializable{
    private final static int DEFAULT_RATING_NUMBER = 1500;
    private String name;
    private String playerId;
    private int rating;

    public Player(String name){
        this.name = name;
        playerId = Util.IDGenerator.getID();
        rating = DEFAULT_RATING_NUMBER;
    }

    public String getName(){
        return name;
    }

    public int getRating(){
        return rating;
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

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    @Override
    public String toString(){
        return name;
    }
}
