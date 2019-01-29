package net.asg.games.provider;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 3/15/2018.
 */

public class GameBlock extends Actor implements Pool.Poolable{
    private Animation animation;
    private YokelBlock myBlock;
    private int currentKeyFrame;
    private float time;
    private static final float ANIMATION_SPEED = 12.0f;
    private boolean isActive = false;
    private Rectangle bounds;

    public GameBlock(){
        super();
        this.currentKeyFrame = 0;
        this.time = 0;
        this.bounds = new Rectangle();
    }

    public YokelBlock getBlock(){
        return myBlock;
    }

    public void setBlock(YokelBlock block, Animation animation){
        if(block == null){
            throw new GdxRuntimeException("Block cannot be null");
        }

        if(animation == null){
            throw new GdxRuntimeException("Animation cannot be null");
        }

        this.myBlock = block;
        this.animation = animation;

        setSize(getPrefWidth(), getPrefHeight());
        setBounds();
        //invalidateHierarchy();
        isActive = true;
    }

    private int getMaxFrame(){
        Object[] maxFrames = animation.getKeyFrames();
        if(maxFrames != null){
            return maxFrames.length;
        } else {
            return -1;
        }
    }

    private float getAnimationPeriod(){
        if(isActive){
            return animation.getFrameDuration();
        }
        return 0;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(isActive){
            time += (delta * ANIMATION_SPEED);

            float aniDuration = getAnimationPeriod();
            if (time > aniDuration) {
                time = aniDuration;
                currentKeyFrame += 1;

                if (currentKeyFrame >= getMaxFrame()) {
                    //TODO: Use loop or noLoop instead of type of block
                    if(Util.isBrokenBlock(myBlock)){
                        currentKeyFrame = getMaxFrame();
                    } else {
                        currentKeyFrame = 0;
                    }
                }
            }
        }
    }

    private void setBounds() {
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
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = Util.get2DAnimationFrame(animation, currentKeyFrame);

        if(frame != null){
            batch.draw(frame, getX(), getY());
        }
    }

    public float getPrefWidth () {
        float width = 0;
        TextureRegion frame = Util.get2DAnimationFrame(animation, 0);
        if (frame != null){
            width = frame.getRegionWidth();
        }
        return width;
    }

    public float getPrefHeight () {
        float height = 0;
        TextureRegion frame = Util.get2DAnimationFrame(animation, 0);
        if (frame != null){
            height = frame.getRegionHeight();
        }
        return height;
    }

    public String toString(){
        return myBlock.toString();
    }

    @Override
    public void reset() {
        setPosition(0,0);
        bounds.setPosition(0,0);
        isActive = false;
    }
}
