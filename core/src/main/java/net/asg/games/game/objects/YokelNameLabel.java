package net.asg.games.game.objects;

/**
 * Created by Blakbro2k on 12/29/2017.
 */

public class YokelNameLabel extends AbstractYokelObject {
    private int icon;

    //Empty Constructor required for Json.Serializable
    public YokelNameLabel() {}

    public YokelNameLabel(String name, int icon) {
        this.icon = icon;
        setName(name);
    }

    @Override
    public void dispose() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}