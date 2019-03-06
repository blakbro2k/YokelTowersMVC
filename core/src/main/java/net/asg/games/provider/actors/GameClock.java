package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.TimeUtils;

public class GameClock extends Table {
    private long start;
    private long elapsed;
    private boolean isRunning;
    private Image digit_1;
    private Image digit_2;
    private Image digit_3;
    private Image digit_4;
    private Image colon;

    public GameClock(Skin skin){
        super(skin);

        resetTimer();
        add(getDigit_1());
        add(getDigit_2());
        add(getColon());
        add(getDigit_3());
        add(getDigit_4());
    }
    public GameClock start(){
        isRunning = true;
        start = TimeUtils.millis();
        return this;
    }

    public void stop(){
        resetTimer();
    }

    public void pause(){
        this.isRunning = !isRunning;
    }

    public boolean isRunning(){
        return this.isRunning;
    }

    public int getElapsedSeconds(){
        return (int) ((TimeUtils.millis() - start) / 1000);
    }

    public int getSeconds(){
        if(isRunning()){
            return Math.round(getElapsedSeconds()) % 60;
        }
        return -1;
    }

    public int getMinutes(){
        if(isRunning()){
            return Math.round(getElapsedSeconds()) / 60;
        }
        return -1;
    }

    private void resetTimer(){
        start = elapsed = 0;
        this.isRunning = false;
    }

    public void act(float delta){
        setDigit_1();
        setDigit_2();
        setDigit_3();
        setDigit_4();
    }

    //public void draw(Batch batch, float parentAlpha){}

    public Drawable getDigitImage(String imageName){
        if(imageName != null){
            return getSkin().getDrawable(imageName);
        }
        return null;
    }

    public Image getDigit_1(){
        if(digit_1 == null){
            digit_1 = new Image();
        }
        //setDigit_1();
        return digit_1;
    }

    public Image getDigit_2(){
        if(digit_2 == null){
            digit_2 = new Image();
        }
        //setDigit_2();
        return digit_2;
    }

    public Image getDigit_3(){
        if(digit_3 == null){
            digit_3 = new Image();
        }
        //setDigit_3();
        return digit_3;
    }

    public Image getDigit_4(){
        if(digit_4 == null){
            digit_4 = new Image();
        }
        //setDigit_4();
        return digit_4;
    }

    public Image getColon(){
        if(colon == null){
            colon = new Image();
        }
        colon.setDrawable(getDigitImage("colon"));
        return colon;
    }

    public Drawable getDigit(int i){
        if(!isRunning){
            return getDigitImage("no_digit");
        } else {
            return getDigitImage(i + "_digit");
        }
    }

    private void setDigit_1(){
        digit_1.setDrawable(getDigit(getMinutes() / 10));
    }

    private void setDigit_2(){
        digit_2.setDrawable(getDigit(getMinutes() % 10));
    }

    private void setDigit_3(){
        digit_3.setDrawable(getDigit(getSeconds() / 10));
    }

    private void setDigit_4(){
        digit_4.setDrawable(getDigit(getSeconds() % 10));
    }
}
