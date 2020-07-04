package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.SnapshotArray;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.YokelUtilities;

import org.apache.commons.lang.StringUtils;

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
        add(powersDisplay).bottom();
    }

    public void updateQueue(Queue<GameBlock> powerUps){
        if(powerUps != null) {
            this.powers = powerUps;
        }
        SnapshotArray<Actor> children = powersDisplay.getChildren();
        //Actor[] actors = children.begin();
        for(int i = 0; i < powers.size;  i++){
            if(i < children.size){
                if(children.get(i) != null) {
                    updateGameBlock(children.get(i), powers.get(i));
                }
            } else {
                powersDisplay.addActor(powers.get(i));
            }
        }
        //children.end();
        //flush the display
        for(int f = powers.size; f < children.size; f++){
            children.removeIndex(f);
        }
    }

    private void updateGameBlock(Actor gameBlock, GameBlock block) {
        if(gameBlock == null || block == null || !gameBlock.getClass().equals(GameBlock.class)) return;
        GameBlock gameBlock1 = (GameBlock) gameBlock;
        String imageName = gameBlock1.getName();
        if(!StringUtils.equalsIgnoreCase(imageName, block.getName())){
            gameBlock1.setImage(block.clone().getImage());
            //YokelUtilities.freeBlock(block);
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