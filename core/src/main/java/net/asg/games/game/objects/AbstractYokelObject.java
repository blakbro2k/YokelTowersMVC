package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Disposable;

import net.asg.games.utils.Util;

public abstract class AbstractYokelObject implements YokelObject {
    protected String id;

    AbstractYokelObject(){ setId();}

    @Override
    public String toString() { return Util.getJsonString(this);}

    @Override
    public abstract void dispose();

    public void setId(){ setId(Util.IDGenerator.getID());}

    public void setId(String id){ this.id = id;}

    public String getId(){ return id;}
}
