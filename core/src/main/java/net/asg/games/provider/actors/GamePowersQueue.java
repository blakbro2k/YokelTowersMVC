package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.YokelUtilities;

public class GamePowersQueue extends Table implements GameObject{
    private Queue<GameBlock> powers;
    private VerticalGroup powersDisplay;

    public GamePowersQueue(Skin skin){
        super(skin);
        initialize();
    }

    private void initialize(){
        powers = new Queue<>();
        powersDisplay = new VerticalGroup();
        setSize(getPrefWidth(), getPrefHeight());
        add(powersDisplay);
    }

    public void updateQueue(Queue<GameBlock> powerUps){
        if(powerUps != null) {
            powers = powerUps;
            powersDisplay.clear();
            int maxIndex = 9;
            int index = 0;
            for(GameBlock block : powers){
                if(++index < maxIndex){
                    powersDisplay.addActor(block);
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public float getPrefWidth() {
        return YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK).getPrefWidth();
    }

    public float getPrefHeight() {
        return YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK).getPrefHeight() * 6;
    }

    @Override
    public void setData(String data) {

    }
}