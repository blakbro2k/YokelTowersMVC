package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.utils.YokelUtilities;

public class GameNameLabel extends Table implements GameObject {
    private static final Color YAHOO_ON = new Color(1f, 0f, 0f, 1);
    private static final Color YAHOO_OFF = new Color(1f, 1f, 1f, 1);

    private GameIcon icon;
    private Label playerName;

    public GameNameLabel(Skin skin){
        super(skin);
        //padTop(4);
        //padBottom(4);
        resetData();
        setNameTagData();
    }

    @Override
    public void setData(String data) {
        YokelPlayer player = YokelUtilities.getObjectFromJsonString(YokelPlayer.class, data);
        if(player != null){
            setIcon(player.getIcon());
            setNameTag(player.getName());
        }
    }

    private void resetData(){
        setData(YokelPlayer.BLANK_PLAYER.toString());
    }

    private void setNameTagData(){
        add(icon);
        add(playerName);
    }

    private void setNameTag(String name) {
        if(playerName == null){
            playerName = new Label("", getSkin());
        }
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
        float playerWidth = 20;
        float superWidth = super.getPrefWidth();
        return YokelUtilities.maxFloat(iconWidth, playerWidth, superWidth);
    }

    public void setLabel(YokelPlayer player) {
        if(player != null){
            setNameTag(player.getName());
            setIcon(player.getIcon());
        }
    }

    void removePlayer() {
        resetData();
    }

    void setYahoo(boolean b) {
        if(b){
            playerName.setColor(YAHOO_ON);
        } else {
            playerName.setColor(YAHOO_OFF);
        }
    }
}