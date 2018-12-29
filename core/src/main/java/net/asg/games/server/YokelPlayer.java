package net.asg.games.server;

import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelPlayer{
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
    //Empty Contructor required for Json.Serializable
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
    public String toString(){
        return name + ":" + getPlayerId();
    }
}
