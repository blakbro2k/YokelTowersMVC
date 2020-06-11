package net.asg.games.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.provider.actors.GameBlock;

public class UIUtil {
    private static UIUtil myInstance = new UIUtil();
    static final private String PREVIEW_TAG = "_preview";

    private YokelObjectFactory factory;
    public static UIUtil getInstance(){
        return myInstance;
    }

    public void setFactory(YokelObjectFactory factory){
        this.factory = factory;
    }

    public YokelObjectFactory getFactory(){
        return factory;
    }

    public Image getBlockImage(String blockName){
        return (Image) factory.getUserInterfaceService().getActor(blockName);
    }

    public Image getBlockImage(int blockId){
        return getBlockImage(factory.getBlockImageName(blockId));
    }

    public Image getPreviewBlockImage(int blockId){
        return getBlockImage(factory.getBlockImageName(blockId) + PREVIEW_TAG);
    }

    public GameBlock getGameBlock(int blockId, boolean preview){
        return getFactory().getGameBlock(blockId, preview);
    }

    public void freeObject(GameBlock block) {
        factory.freeObject(block);
    }
}