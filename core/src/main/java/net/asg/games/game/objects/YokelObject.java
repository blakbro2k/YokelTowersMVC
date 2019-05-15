package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Disposable;

import net.asg.games.utils.Util;

public abstract class YokelObject implements Disposable {
    protected String id;

    @Override
    public String toString() {
        return Util.getJsonString(this);
    }

    @Override
    public abstract void dispose();

    public void setId(String id){ this.id = id;}

    public String getId(){ return id;}
}
