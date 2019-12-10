package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

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

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.id = json.readValue("id", String.class, jsonData);
    }
}
