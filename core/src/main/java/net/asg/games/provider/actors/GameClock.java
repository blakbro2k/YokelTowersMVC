package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameClock extends Table {
    private long start;
    private long elapsed;

    private boolean isRunning;

    public GameClock(Label.LabelStyle style){
        if(style == null) throw new GdxRuntimeException("style cannot be null");
        //setSkin(style);
        resetTimer();
        add(new Label("0", style));
        add(new Label("0", style));
        add(new Label(":", style));
        add(new Label("0", style));
        add(new Label("0", style));
    }
    public void start(){
        isRunning = true;
    }

    public void stop(){
        isRunning = false;
    }

    public void pause(){
        this.isRunning = !isRunning;
    }

    private void resetTimer(){
        start = elapsed = 0;
        this.isRunning = false;
    }

    public void act(float delta){
        if(isRunning){
            elapsed = (long) (elapsed + (1 * delta));
        }
    }

    public void draw(Batch batch, float parentAlpha){

    }

    private long getElapsedTime(){
        return elapsed;
    }

    public int getSecondsDigit(long elapsedTime){
        return 1;
       // TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
    }
}
