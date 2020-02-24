package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;

import net.asg.games.game.objects.YokelLounge;

import java.util.Arrays;

public class GameLounge extends Table {
    private YokelLounge lounge;

    public GameLounge(Skin skin) {
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

    public void setData(String data){
        System.out.println("Data=" + data);
    }
}
