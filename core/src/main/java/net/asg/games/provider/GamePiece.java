package net.asg.games.provider;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.asg.games.utils.Util;

public class GamePiece extends Actor {
    /*
    private GameBlock top;
    private GameBlock middle;
    private GameBlock bottom;
    private Rectangle bounds = new Rectangle();


    public GamePiece(GameBlock bottom, GameBlock mid, GameBlock top){
        setSize(getPrefWidth(), getPrefHeight());
        setBounds();

        setTopBlock(top);
        setMiddleBlock(mid);
        setBottomBlock(bottom);
        setDebug(true);
    }

    public GameBlock getTopBlock() {
        return top;
    }

    public void setTopBlock(GameBlock top) {
        if(top != null){
            top.setPosition(getX(), getY() + (Util.getClearBlockHeight() * 2));
        }
        this.top = top;
    }

    public GameBlock getMiddleBlock() {
        return middle;
    }

    public void setMiddleBlock(GameBlock middle) {
        if(middle != null){
            middle.setPosition(getX(), getY() + (Util.getClearBlockHeight()));
        }
        this.middle = middle;
    }

    public GameBlock getBottomBlock() {
        return bottom;
    }

    public void setBottomBlock(GameBlock bottom) {
        if(bottom != null){
            bottom.setPosition(getX(), getY());
        }
        this.bottom = bottom;
    }

    @Override
    public void act(float delta){
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        if(topBlock != null){
            topBlock.act(delta);
        }

        if(midBlock != null){
            midBlock.act(delta);
        }

        if(bottomBlock != null){
            bottomBlock.act(delta);
        }
    }

    public void setBounds() {
        bounds.set(getX(), getY(), getWidth(), getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    protected void positionChanged () {
        setBounds();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        float localX = getX();
        float localY = getY();
        float localHeight = Util.getClearBlockHeight();

        if(topBlock != null){
            topBlock.setPosition(localX, localY + (localHeight * 2));
            topBlock.draw(batch, parentAlpha);
        }
        if(midBlock != null){
            midBlock.setPosition(localX, localY + (localHeight));
            midBlock.draw(batch, parentAlpha);
    }
        if(bottomBlock != null){
            bottomBlock.setPosition(localX, localY);
            bottomBlock.draw(batch, parentAlpha);
        }
    }

    public float getPrefWidth() {
        float width = 0;
        float localWidth = Util.getClearBlockWidth();

        if (localWidth > 0){
            width = localWidth * 1;
        }
        return width;
    }

    public float getPrefHeight() {
        float height = 0;
        float localHeight = Util.getClearBlockHeight();

        if (localHeight > 0){
            height = localHeight * 3;
        }
        return height;
    }

    public String toString(){
        return "[" + getTopBlock() + "]\n" +
                "[" + getMiddleBlock() + "]\n" +
                "[" + getBottomBlock() + "]";
    }

    public void cycleClockWise(){
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        setTopBlock(bottomBlock);
        setMiddleBlock(topBlock);
        setBottomBlock(midBlock);
    }

    public void cycleCounterClockWise(){
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        setTopBlock(midBlock);
        setMiddleBlock(bottomBlock);
        setBottomBlock(topBlock);
    }
    */
}
