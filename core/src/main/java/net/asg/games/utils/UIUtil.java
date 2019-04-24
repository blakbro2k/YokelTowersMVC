package net.asg.games.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.asg.games.game.objects.YokelObjectFactory;

public class UIUtil {
    private static UIUtil myInstance = new UIUtil();
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
}
