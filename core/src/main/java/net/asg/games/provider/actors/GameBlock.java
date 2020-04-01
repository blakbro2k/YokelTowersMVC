package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pool;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.UIUtil;
import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 3/15/2018.
 */

public class GameBlock extends Table implements Pool.Poolable, GameObject{
    final public static float ANIMATION_DELAY = 0.15f;

    private AnimatedImage uiBlock;
    private boolean isActive;

    public GameBlock(Skin skin, String blockName){
        super(skin);
        reset();
        setImage(blockName);
        add(uiBlock);
    }

    public GameBlock(Skin skin, int block){
        super(skin);
        reset();
        setImage(block);
        add(uiBlock);
    }

    public void setImage(Image image){
        setDrawable(image);
    }

    public void setImage(String blockName){
        setImage(UIUtil.getInstance().getBlockImage(blockName));
    }

    public void setImage(int blockValue){
        setImage(UIUtil.getInstance().getBlockImage(blockValue));
    }

    public AnimatedImage getImage(){
        return this.uiBlock;
    }

    public void setActive(boolean b){
        this.isActive = b;
    }

    public boolean isActive(){
        return uiBlock != null && isActive;
    }

    @Override
    public void reset() {
        setX(0);
        setY(0);
        uiBlock = null;
        setActive(false);
    }

    private void setDrawable(Image image){
        if(uiBlock == null){
            uiBlock = new AnimatedImage();
            uiBlock.setDelay(ANIMATION_DELAY);
        }

        if(image != null) {
            Drawable drawable = image.getDrawable();
            setName(image.getName());

            Util.setSizeFromDrawable(uiBlock, drawable);
            Util.setSizeFromDrawable(this, drawable);

            uiBlock.setDrawable(drawable);

            if (image instanceof AnimatedImage) {
                uiBlock.setFrames(Util.getAniImageFrames((AnimatedImage) image));
            } else {
                uiBlock.setFrames(GdxArrays.newArray(image.getDrawable()));
            }
        }
    }

    @Override
    public void setName(String name){
        super.setName(name);
        uiBlock.setName(name);
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        uiBlock.setPosition(x, y);
    }

    @Override
    public void setData(String data) {
        YokelBlock block = Util.getObjectFromJsonString(YokelBlock.class, data);
        if(block != null) setImage(Util.otoi(block.toString()));
    }
}