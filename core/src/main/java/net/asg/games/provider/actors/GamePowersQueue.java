package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Queue;

public class GamePowersQueue extends Table implements GameObject{
    private Queue<GameBlock> powers;
    private VerticalGroup powersDisplay;

    public GamePowersQueue(Skin skin){
        super(skin);
        initialize();
    }

    private void initialize(){
        powers = new Queue<GameBlock>();
        powersDisplay = new VerticalGroup();
        setSize(getPrefWidth(), getPrefHeight());
        add(powersDisplay);
    }

    public void update(Queue<GameBlock> powerUps){
        if(powerUps != null) {
            for (GameBlock block : powerUps) {
                if (block != null){
                    powers.addFirst(block);
                }
            }
            powersDisplay.clear();
            int maxIndex = 9;
            int index = 0;
            for(GameBlock block : powers){
                if(index < maxIndex){
                    powersDisplay.addActor(block);
                }
                index++;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void clearAllPowerUps(){
        //TODO: keep special power ups
        powers.clear();
    }

    public float getPrefWidth() {
        float width = super.getPrefWidth();
        float localWidth = 3f;

        if (localWidth > 0){
            width = localWidth * 1;
        }
        return width;
    }

    public float getPrefHeight() {
        float height = super.getPrefHeight();
        float localHeight = 3f;

        if (localHeight > 0){
            height = localHeight * 9;
        }
        return height;
    }

    @Override
    public void setData(String data) {

    }
}
