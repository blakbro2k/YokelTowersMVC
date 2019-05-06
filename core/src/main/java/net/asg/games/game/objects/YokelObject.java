package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Disposable;

import net.asg.games.utils.Util;

public abstract class YokelObject implements Disposable {
    @Override
    public String toString() {
        return Util.getJsonString(this);
    }

    public abstract YokelObject fromString(String strArr);

    @Override
    public abstract void dispose();
}
