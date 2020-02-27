package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.utils.Util;

public class GameLounge extends Table implements GameObject {
    private YokelLounge lounge;
    private Array<Button> buttons;

    public GameLounge(Skin skin) {
        super(skin);
        buttons = new Array<>();
    }

    public void setLounge(YokelLounge lounge){
        if(lounge != null){
            this.lounge = lounge;
            add(new Label(lounge.getName(), getSkin())).colspan(2).row();
        }
    }

    public OrderedMap<String, YokelRoom> getAllRooms(){
        if(lounge != null){
            return lounge.getAllRooms();
        }
        return GdxMaps.newOrderedMap();
    }

    public YokelLounge getLounge(){
        return this.lounge;
    }

    @Override
    public void setData(String data){
        setLounge(Util.getObjectFromJsonString(YokelLounge.class, Util.stringToJson(data)));
    }
}
