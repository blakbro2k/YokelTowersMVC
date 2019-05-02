package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.utils.UIUtil;
import net.asg.games.utils.Util;


/**
 * Created by Blakbro2k on 3/15/2018.
 */

public class GameBlock extends Table implements Pool.Poolable{
    private AnimatedImage uiBlock;
    private boolean isActive;

    public GameBlock(Skin skin, String blockName){
        super(skin);
        reset();
        setName(blockName);
        uiBlock = new AnimatedImage();
        uiBlock.setName(blockName);
        setImage(blockName);
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
        if(uiBlock != null){
            Drawable drawable = image.getDrawable();

            Util.setSizeFromDrawable(uiBlock, drawable);
            Util.setSizeFromDrawable(this, drawable);

            this.setName(image.getName());
            uiBlock.setDrawable(drawable);
            uiBlock.setName(image.getName());

            if(image instanceof AnimatedImage){
                uiBlock.setFrames(getFrames((AnimatedImage)image));
            } else {
                uiBlock.setFrames(GdxArrays.newArray(image.getDrawable()));
            }
        }
    }

    private Array<Drawable> getFrames(AnimatedImage image){
        Array<Drawable> drawables = new Array<>();
        if(image != null){
            for(Drawable frame : Util.toIterable(image.getFrames())){
                if(frame != null){
                    drawables.add(frame);
                }
            }
        }
        return drawables;
    }
}
