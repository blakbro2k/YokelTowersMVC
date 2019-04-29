package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
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
        uiBlock = new AnimatedImage();
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
        setPosition(0,0);
        uiBlock = null;
        setActive(false);
    }

    private void setDrawable(Image image){
        if(uiBlock != null){
            uiBlock.setDrawable(image.getDrawable());
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
