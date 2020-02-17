package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class YokelBlockMove extends AbstractYokelObject {
    public int x;
    public int y;
    public int block;

    public YokelBlockMove(int x, int y, int block){
        this.x = x;
        this.y = y;
        this.block = block;
    }

    @Override
    public void dispose() {}
/*
    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("x", x);
        json.writeValue("y", y);
        json.writeValue("block", block);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.id = json.readValue("id", String.class, jsonData);
        this.x = json.readValue("x", Integer.class, jsonData);
        this.y = json.readValue("y", Integer.class, jsonData);
        this.block = json.readValue("block", Integer.class, jsonData);
    }*/
}
