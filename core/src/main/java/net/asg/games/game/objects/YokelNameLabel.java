package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Pool;

import static net.asg.games.game.objects.YokelBlock.CLEAR_BLOCK;

/**
 * Created by Blakbro2k on 12/29/2017.
 */

public class YokelNameLabel extends AbstractYokelObject {
    private int icon;

    //Empty Contructor required for Json.Serializable
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