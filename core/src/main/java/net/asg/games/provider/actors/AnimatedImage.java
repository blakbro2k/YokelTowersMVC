package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {
    protected Animation<TextureRegion> animation;
    private float stateTime = 0;
    private boolean looping;

    public AnimatedImage(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
        this.looping = false;
    }

    public AnimatedImage(Animation<TextureRegion> animation, boolean looping) {
        this(animation);
        this.looping = looping;
    }


    @Override
    public void act(float delta){
        setRegion(delta);
        super.act(delta);
    }

    private TextureRegionDrawable getTextureRegionDrawable(){
        return (TextureRegionDrawable) getDrawable();
    }

    private void setRegion(float delta){
        TextureRegionDrawable drawable = getTextureRegionDrawable();
        if(drawable != null){
            drawable.setRegion(animation.getKeyFrame(stateTime += delta, looping));
        }
    }
}
