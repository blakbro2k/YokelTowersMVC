package net.asg.games.utils;

import net.asg.games.game.objects.YokelObjectFactory;

public class UIUtil {
    private static UIUtil myInstance = new UIUtil();
    private YokelObjectFactory factory;
    public static UIUtil getInstance(){
        return myInstance;
    }

    public void setFactory(YokelObjectFactory factory){
        System.out.println("init factory=" + factory);
        this.factory = factory;
    }

    public YokelObjectFactory getFactory(){
        return factory;
    }
}
