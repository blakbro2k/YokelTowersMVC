package net.asg.games.game.objects;

import net.asg.games.utils.Util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelPlayer extends YokelObject {
    private final static int DEFAULT_RATING_NUMBER = 1500;
    private String name;
    private int rating;
    private String logo;
    private String sessionId;

    //Empty Contructor required for Json.Serializable
    public YokelPlayer(){}

    public YokelPlayer(String name){
        this.name = name;
        setId(Util.IDGenerator.getID());
        rating = DEFAULT_RATING_NUMBER;
    }

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
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!this.getClass().equals(o.getClass())) return false;
        YokelPlayer player = getPlayer(o);
        return isNameSame(player);
    }

    private YokelPlayer getPlayer(Object o){
        return (YokelPlayer) o;
    }

    private boolean isNameSame(YokelPlayer player){
        if(player == null) return false;
        return StringUtils.equalsIgnoreCase(name, player.getName());
    }

    @Override
    public void dispose() {}
}
