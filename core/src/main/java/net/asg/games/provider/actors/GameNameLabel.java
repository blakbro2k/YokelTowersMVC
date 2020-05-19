package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelNameLabel;
import net.asg.games.utils.Util;

public class GameNameLabel extends Table implements GameObject {
    private GameIcon icon;
    private Label playerName;

    public GameNameLabel(Skin skin){
        super(skin);
        icon = null;
        playerName = new Label(" ", skin);
    }

    @Override
    public void setData(String data) {
        YokelNameLabel nameTag = Util.getObjectFromJsonString(YokelNameLabel.class, data);
        if(nameTag != null){
            setIcon(nameTag.getIcon());
            setNameTag(nameTag.getName());
        }
        setNameTagData();
    }

    private void setNameTagData(){
        add(icon);
        add(playerName);
    }

    private void setNameTag(String name) {
        playerName.setText(name);
    }

    private void setIcon(int iconNumber) {
        if(icon == null){
            icon = new GameIcon(GameIcon.getGameDefaultIconStyle(getSkin()), getSkin());
        }
        icon.setIconNumber(iconNumber);
    }

    @Override
    public float getPrefHeight() {
        float iconHeight = icon.getPrefHeight();
        float playerHeight = playerName.getPrefHeight();
        float superHeight = super.getPrefHeight();
        return Util.maxFloat(iconHeight, playerHeight, superHeight);
    }

    @Override
    public float getPrefWidth() {
        float iconWidth = icon.getPrefWidth();
        float playerWidth = icon.getPrefWidth();
        float superWidth = super.getPrefWidth();
        return Util.maxFloat(iconWidth, playerWidth, superWidth);
    }
}