package net.asg.games.utils;

public class PostLoader {
    public static final String UI_TEST = "uitest";
    public static final String DEBUG = "debug";

    private static PostLoader myInstance = new PostLoader();
    private String preLoader;

    public PostLoader(){
        clearPreLoader();
    }

    public static PostLoader getInstance(){
        return myInstance;
    }

    public void clearPreLoader(){
        this.preLoader = null;
    }

    public void setUIPreLoader(){
        this.preLoader = UI_TEST;
    }

    public void setDebugPreloader(){
        this.preLoader = DEBUG;
    }

    public String getPreLoader(){
        return this.preLoader;
    }
}