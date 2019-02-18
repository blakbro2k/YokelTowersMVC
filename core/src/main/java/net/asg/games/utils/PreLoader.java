package net.asg.games.utils;

public class PreLoader {
    private static PreLoader myInstance = new PreLoader();
    private String preLoader;

    public PreLoader(){
        clearPreLoader();
    }

    public static PreLoader getInstance(){
        return myInstance;
    }

    public void clearPreLoader(){
        this.preLoader = null;
    }

    public void setUIPreLoader(){
        this.preLoader = "uitester";
    }

    public void setDebugPreloader(){
        this.preLoader = "preload";
    }

    public String getPreLoader(){
        return this.preLoader;
    }
}