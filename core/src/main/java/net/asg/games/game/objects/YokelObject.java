package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;

public interface YokelObject extends Disposable, Json.Serializable {
    /* Sets the Object ID*/
    void setId(String id);

    /* Returns the Object ID*/
    String getId();
}
