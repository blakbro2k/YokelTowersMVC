package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Disposable;

public interface YokelObject extends Disposable {
    /** Sets the Object ID*/
    void setId(String id);

    /** Returns the Object ID*/
    String getId();

    /** Sets the Object Name*/
    void setName(String name);

    /** Returns the Object Name*/
    String getName();
}
