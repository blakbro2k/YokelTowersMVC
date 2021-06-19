package net.asg.games.game.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;

import net.asg.games.controller.LoadingController;

public class YokelSoundFXFactory implements Disposable {
    private MusicService musicService;
    private LoadingController assetLoader;

    public YokelSoundFXFactory(MusicService musicService, LoadingController assetLoader){
        if(musicService == null) throw new GdxRuntimeException("musicService was not initialized.");
        if(assetLoader == null) throw new GdxRuntimeException("LoadingController was not initialized.");
        this.musicService = musicService;
        this.assetLoader = assetLoader;
    }

    @Override
    public void dispose() {
    }

    public void playBlockDownSound() {
        musicService.play(assetLoader.getBlockDownSound());
    }

    public void playYahooSound(){
        musicService.play(assetLoader.getYahooSound());
    }

    public void playMenacingSound(){
        Sound menacingSound = assetLoader.getMenacingSound();
        menacingSound.loop();
        musicService.play(menacingSound);
    }

    public void stopMenacingSound(){
        //TODO: Add fade
        assetLoader.getMenacingSound().stop();
    }

    public void playCycleClickSound() {
        musicService.play(assetLoader.getCycleClickSound());
    }

    public void playGameStartSound(){
        musicService.play(assetLoader.getGameStartSound());
    }

    public void playBrokenCell() {
        musicService.play(assetLoader.getBrokenCellSound());
    }

    public void playBoardDeathSound() {
        musicService.play(assetLoader.getBBoardDeathSound());
    }
    public void playYahooBrokenCell() {
        musicService.play(assetLoader.getYahooBreakSound());
    }
    public void playGameOverSound() { musicService.play(assetLoader.getGameOverSound());}
}