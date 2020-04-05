package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelNameLabel;
import net.asg.games.utils.Util;

public class GameNameLabel extends Table implements GameObject{
    private GameIcon icon;
    private Label playerName;

    public GameNameLabel(Skin skin){
        super(skin);
        icon = new GameIcon(GameIcon.getGameDefaultIconStyle(skin), skin);
        playerName = new Label("", skin);

        add(icon);
        add(playerName);
    }

    @Override
    public void setData(String data) {
        YokelNameLabel nameTag = Util.getObjectFromJsonString(YokelNameLabel.class, data);
        if(nameTag != null){
            icon.setIconNumber(nameTag.getIcon());
            playerName.setText(nameTag.getName());
        }
    }
}