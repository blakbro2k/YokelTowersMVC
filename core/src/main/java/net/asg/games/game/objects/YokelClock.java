package net.asg.games.game.objects;

import com.badlogic.gdx.utils.TimeUtils;

public class YokelClock extends AbstractYokelObject {
    private long start = -1;
    private boolean isRunning = false;

    //Empty Contructor required for Json.Serializable
    public YokelClock(){}

    public void start(){
        isRunning = true;
        start = TimeUtils.millis();
    }

    public int getElapsedSeconds(){
        return (int) ((TimeUtils.millis() - start) / 1000);
    }

    public void stop(){
        resetTimer();
    }

    private void resetTimer(){
        start = 0;
        this.isRunning = false;
    }

    @Override
    public void dispose() {}
}