package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.utils.UIUtil;
import net.asg.games.utils.YokelUtilities;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * Created by Blakbro2k on 3/15/2018.
 */

public class GameBlock extends Table implements Pool.Poolable, GameObject, Cloneable {
    private final static float DEFAULT_ANIMATION_DELAY = 0.12f;
    private final static float DEFENSE_ANIMATION_DELAY = 0.32f;
    private final static float MEDUSA_ANIMATION_DELAY = 0.22f;
    private final static float MIDAS_ANIMATION_DELAY = 0.2f;

    private AnimatedImage uiBlock;
    private boolean isActive;
    private boolean isPreview;

    //No-arg constructor required for Pools
    public GameBlock() {}

    //New block via image name
    public GameBlock(Skin skin, String blockName, boolean isPreview) {
        super(skin);
        reset();
        this.isPreview = isPreview;
        setImage(blockName);
        add(uiBlock);
    }

    //New block via block type
    public GameBlock(Skin skin, int block, boolean isPreview) {
        super(skin);
        reset();
        this.isPreview = isPreview;
        setImage(block);
        add(uiBlock);
    }

    public void setImage(Image image) {
        setDrawable(image);
    }

    public void setImage(String blockName) {
        if(isPreview){
            setImage(UIUtil.getInstance().getPreviewBlockImage(blockName));
        } else {
            setImage(UIUtil.getInstance().getBlockImage(blockName));
        }
    }

    public void setImage(int blockValue) {
        if(isPreview){
            setImage(UIUtil.getInstance().getPreviewBlockImage(blockValue));
        } else {
            setImage(UIUtil.getInstance().getBlockImage(blockValue));
        }
    }

    public AnimatedImage getImage() {
        return this.uiBlock;
    }

    public void setActive(boolean b) {
        this.isActive = b;
    }

    public boolean isActive() {
        return uiBlock != null && isActive;
    }

    public void setPreview(boolean b) {
        this.isPreview = b;
    }

    public boolean isPreview() {
        return isPreview;
    }

    private float getDelay(Image image){
        if(image != null){
            if(StringUtils.containsIgnoreCase(image.getName(), "defense")){
                return DEFENSE_ANIMATION_DELAY;
            } else if(StringUtils.containsIgnoreCase(image.getName(), "medusa")){
                return MEDUSA_ANIMATION_DELAY;
            } else if(StringUtils.containsIgnoreCase(image.getName(), "midas")){
                return MIDAS_ANIMATION_DELAY;
            }
        }
        return DEFAULT_ANIMATION_DELAY;
    }

    private void setDrawable(Image image) {
        if (uiBlock == null) {
            uiBlock = new AnimatedImage();
        }
        uiBlock.setDelay(getDelay(image));

        if(image == null) return;
        Drawable drawable;
        if (image instanceof AnimatedImage) {
            drawable = ((AnimatedImage) image).getFrames().get(0);
            uiBlock.setFrames(YokelUtilities.getAniImageFrames((AnimatedImage) image));
        } else {
            drawable = image.getDrawable();
            uiBlock.setFrames(GdxArrays.newArray(drawable));
        }

        setName(image.getName());

        uiBlock.setDrawable(drawable);
        if(StringUtils.containsIgnoreCase(getName(), "broken")){
            uiBlock.setPlayOnce(true);
        }
        YokelUtilities.setSizeFromDrawable(uiBlock, drawable);
        YokelUtilities.setSizeFromDrawable(this, drawable);
    }

    @Override
    public void reset() {
        setX(0);
        setY(0);
        uiBlock = null;
        setPreview(false);
        setActive(false);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        uiBlock.setName(name);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        uiBlock.setPosition(x, y);
    }

    @Override
    public void setData(String data) {
        YokelBlock block = YokelUtilities.getObjectFromJsonString(YokelBlock.class, data);
        if (block != null) setImage(YokelUtilities.otoi(block));
    }

    public void setCurrentFrame(int frame){
        uiBlock.setCurrentFrame(frame);
    }

    public int getCurrentFrame() {
        return uiBlock.getCurrentFrame();
    }

    @Override
    public float getPrefWidth() {
        Image image = getImage();
        if(image != null){
            return image.getWidth();
        } else {
            return 20;
        }
    }

    @Override
    public float getPrefHeight() {
        Image image = getImage();
        if(image != null){
            return image.getHeight();
        } else {
            return 20;
        }
    }

    @Override
    public float getWidth() {
        return getPrefWidth();
    }

    @Override
    public float getHeight() {
        return getPrefHeight();
    }

    public void update(int block, boolean isPreview) {
        if(needsUpdate(block, isPreview)){
            GameBlock blockUi = YokelUtilities.getBlock(block, isPreview);

            if(blockUi != null){
                AnimatedImage clone = blockUi.clone().getImage();
                setImage(clone);
                setName(blockUi.getName());
                //YokelUtilities.freeBlock(blockUi);
            }
        }
    }

    private boolean needsUpdate(int block, boolean isPreview) {
        String blockName = "";
        Image blockImage;
        boolean isBroken = YokelBlockEval.hasBrokenFlag(block);
        block = YokelUtilities.getTrueBlock(block);
        if(isBroken) block = YokelBlockEval.addBrokenFlag(block);

        if (isPreview) {
            blockImage = UIUtil.getInstance().getPreviewBlockImage(block);
        } else {
            blockImage = UIUtil.getInstance().getBlockImage(block);
        }

        if (blockImage != null) {
            blockName = blockImage.getName();
        }

        return !StringUtils.equalsIgnoreCase(blockName, uiBlock.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameBlock gameBlock = (GameBlock) o;
        return uiBlock.getName().equals(gameBlock.getName()) && isPreview == gameBlock.isPreview();
    }

    @Override
    public int hashCode() {
        return Objects.hash(uiBlock, isPreview);
    }

    @Override
    public GameBlock clone(){
        try {
            Object c = super.clone();

            if(c instanceof GameBlock){
                GameBlock g = (GameBlock) c;
                GameBlock r = new GameBlock(getSkin(), YokelBlock.CLEAR_BLOCK, g.isPreview);
                //GameBlock r = Pools.obtain(GameBlock.class);
                //System.out.println("r=" + r);
                r.setPreview(g.isPreview);
                r.setImage(g.getImage());
                r.setName(g.getName());
                r.setImage(g.getImage());
                r.setActive(g.isActive());
                return r;
            }
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to clone " + this.getClass().getSimpleName() + " not an instance of " + GameBlock.class.getSimpleName());
        }
    }
}