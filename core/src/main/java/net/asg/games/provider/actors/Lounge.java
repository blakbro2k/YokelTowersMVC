package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;

import net.asg.games.game.objects.YokelLounge;

public class Lounge extends Table {
    private YokelLounge lounge;

    public Lounge(Skin skin) {
        super(skin);
    }

    public void setLounge(YokelLounge lounge){
        if(lounge != null){
            this.lounge = lounge;
        }
    }

    public YokelLounge getLounge(){
        return this.lounge;
    }
}
