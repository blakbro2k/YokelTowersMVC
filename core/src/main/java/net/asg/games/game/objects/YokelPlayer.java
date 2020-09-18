package net.asg.games.game.objects;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelPlayer extends AbstractYokelObject {
    private final static int DEFAULT_RATING_NUMBER = 1500;
    private int rating;
    private int icon;
    private YokelNameLabel nameLabel;

    //Empty Constructor required for Json.Serializable
    public YokelPlayer(){}

    public YokelPlayer(String name){
        this(name, DEFAULT_RATING_NUMBER, 1);
    }

    public YokelPlayer(String name, int rating){
        this(name, rating, 1);
    }

    public YokelPlayer(String name, int rating, int icon){
        setName(name);
        setRating(rating);
        setIcon(icon);
        nameLabel = new YokelNameLabel(name, icon);
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public int getIcon(){
        return icon;
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

    public YokelNameLabel getNameLabel(){
        return nameLabel;
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
    public void dispose() { nameLabel.dispose();}
}