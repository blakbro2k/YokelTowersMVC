package net.asg.games.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.controller.LoadingController;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.service.UserInterfaceService;
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

    public void startMenacingMusic(){
        //musicService.playCurrentTheme(assetLoader.getMenacing(), false);
        Music yahoo = Gdx.audio.newMusic(Gdx.files.internal(GlobalConstants.MENACING_PATH));
        yahoo.setLooping(true);
        musicService.playCurrentTheme(yahoo);
    }

    public void playYahooSound(){
        //Music yahoo = Gdx.audio.newMusic(Gdx.files.internal(GlobalConstants.YAHOO_PATH));
        //yahoo.setLooping(false);
        //Music yahoo = Gdx.audio.newMusic(Gdx.files.internal(GlobalConstants.YAHOO_PATH));

        musicService.playCurrentTheme(assetLoader.getYahooMusic());
        //musicService.playCurrentTheme(yahoo);
    }

    public void stopMenacingMusic(){
        musicService.clearCurrentTheme();
    }

    public void playCycleClickSound() {
        musicService.play(assetLoader.getCycleClickSound());
    }

    public void playGameStartSound(){
        musicService.play(assetLoader.getGameStart());
    }
}