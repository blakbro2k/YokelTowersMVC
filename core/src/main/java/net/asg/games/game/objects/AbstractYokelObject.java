package net.asg.games.game.objects;

import net.asg.games.utils.Util;

public abstract class AbstractYokelObject implements YokelObject {
    protected String id;
    protected String name;

    AbstractYokelObject(){
        setId();
        setName();
    }

    @Override
    public String toString() { return Util.getJsonString(this);}

    @Override
    public abstract void dispose();

    public void setId(){ setId(Util.IDGenerator.getID());}

    public void setId(String id){ this.id = id;}

    public String getId(){ return id;}

    public void setName(){
        setName(null);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
