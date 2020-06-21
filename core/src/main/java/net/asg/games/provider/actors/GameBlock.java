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
import net.asg.games.utils.YokelUtilities;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Blakbro2k on 3/15/2018.
 */

public class GameBlock extends Table implements Pool.Poolable, GameObject {
    private final static float DEFAULT_ANIMATION_DELAY = 0.12f;
    private final static float DEFENSE_ANIMATION_DELAY = 0.32f;

    private AnimatedImage uiBlock;
    private boolean isActive;

    public GameBlock(Skin skin, String blockName) {
        super(skin);
        reset();
        setImage(blockName);
        add(uiBlock);
    }

    public GameBlock(Skin skin, int block) {
        super(skin);
        reset();
        setImage(block);
        add(uiBlock);
    }

    public void setImage(Image image) {
        setDrawable(image);
    }

    public void setImage(String blockName) {
        setImage(UIUtil.getInstance().getBlockImage(blockName));
    }

    public void setImage(int blockValue) {
        setImage(UIUtil.getInstance().getBlockImage(blockValue));
    }

    public void setPreviewImage(int blockValue) {
        setImage(UIUtil.getInstance().getPreviewBlockImage(blockValue));
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

    private float getDelay(Image image){
        if(image != null){
            if(StringUtils.containsIgnoreCase(image.getName(), "defense")){
                return DEFENSE_ANIMATION_DELAY;
            }
        }
        return DEFAULT_ANIMATION_DELAY;
    }
    @Override
    public void reset() {
        setX(0);
        setY(0);
        uiBlock = null;
        setActive(false);
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
        YokelUtilities.setSizeFromDrawable(uiBlock, drawable);
        YokelUtilities.setSizeFromDrawable(this, drawable);
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

    @Override
    public float getPrefWidth() {
        return getImage().getWidth();
    }

    @Override
    public float getPrefHeight() {
        return getImage().getHeight();
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
                setImage(blockUi.getImage());
            }
        }
    }

    private boolean needsUpdate(int block, boolean isPreview) {
        String blockName = "";
        Image blockImage;

        if(isPreview){
            blockImage = UIUtil.getInstance().getPreviewBlockImage(block);
        } else {
            blockImage = UIUtil.getInstance().getBlockImage(block);
        }
        if(blockImage != null){
            blockName = blockImage.getName();
        }
        return !StringUtils.equalsIgnoreCase(blockName, uiBlock.getName());
    }
}