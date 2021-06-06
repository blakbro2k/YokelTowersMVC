package net.asg.games.game.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;

import net.asg.games.controller.LoadingController;
import net.asg.games.utils.GlobalConstants;

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
        musicService.play(assetLoader.getBlockDown());
    }

    public void playYahooSound(){
        musicService.play(assetLoader.getYahooSound());
    }

    public void playMenacingSound(){
        musicService.play(assetLoader.getMenacingSound());
    }

    public void playCycleClickSound() {
        musicService.play(assetLoader.getCycleClickSound());
    }

    public void playGameStartSound(){
        musicService.play(assetLoader.getGameStart());
    }
}