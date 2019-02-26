package net.asg.games.provider;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {
    protected Animation<TextureRegion> animation = null;
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
    public void act(float delta)
    {
        getTextureRegionDrawable().setRegion(animation.getKeyFrame(stateTime += delta, looping));
        super.act(delta);
    }

    public TextureRegionDrawable getTextureRegionDrawable(){
        return (TextureRegionDrawable) getDrawable();
    }
}
