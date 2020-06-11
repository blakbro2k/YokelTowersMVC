package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelNameLabel;
import net.asg.games.utils.YokelUtilities;

public class GameNameLabel extends Table implements GameObject {
    private GameIcon icon;
    private Label playerName;

    public GameNameLabel(Skin skin){
        super(skin);
        padTop(4);
        padBottom(4);
        icon = new GameIcon(GameIcon.getGameDefaultIconStyle(skin), skin);
        icon.setIconNumber(0);
        playerName = new Label(" ", skin);
    }

    @Override
    public void setData(String data) {
        YokelNameLabel nameTag = YokelUtilities.getObjectFromJsonString(YokelNameLabel.class, data);
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
        float iconHeight = icon == null ? 28 : icon.getPrefHeight();
        float playerHeight = playerName == null ? 28 : playerName.getPrefHeight();
        float superHeight = super.getPrefHeight();
        return YokelUtilities.maxFloat(iconHeight, playerHeight, superHeight);
    }

    @Override
    public float getPrefWidth() {
        float iconWidth = icon == null ? 32 : icon.getPrefWidth();
        float playerWidth = 75;
        float superWidth = super.getPrefWidth();
        return YokelUtilities.maxFloat(iconWidth, playerWidth, superWidth);
    }
}