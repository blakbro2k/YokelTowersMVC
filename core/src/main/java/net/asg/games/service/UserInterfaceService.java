package net.asg.games.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.SkinService;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.utils.Util;

@Component
public class UserInterfaceService {
    @Inject private AssetService assetService;
    @Inject private InterfaceService interfaceService;
    @Inject private SkinService skinService;

    private ObjectMap<String, Actor> uiAssetMap;
    private YokelObjectFactory factory;

    @LmlActor("Y_block") public Actor yBlockImage;
    @LmlActor("O_block") public Image oBlockImage;
    @LmlActor("K_block") public Image kBlockImage;
    @LmlActor("E_block") public Image eBlockImage;
    @LmlActor("L_block") public Image lBlockImage;
    @LmlActor("Bash_block") public Image bashBlockImage;
    @LmlActor("defense_Y_block") public AnimatedImage defenseYBlockImage;
    @LmlActor("defense_O_block") public AnimatedImage defenseOBlockImage;
    @LmlActor("defense_K_block") public AnimatedImage defenseKBlockImage;
    @LmlActor("defense_E_block") public AnimatedImage defenseEBlockImage;
    @LmlActor("defense_L_block") public AnimatedImage defenseLBlockImage;
    @LmlActor("defense_Bash_block") public AnimatedImage defenseBashBlockImage;
    @LmlActor("power_Y_block") public AnimatedImage powerYBlockImage;
    @LmlActor("power_O_block") public AnimatedImage powerOBlockImage;
    @LmlActor("power_K_block") public AnimatedImage powerKBlockImage;
    @LmlActor("power_E_block") public AnimatedImage powerEBlockImage;
    @LmlActor("power_L_block") public AnimatedImage powerLBlockImage;
    @LmlActor("power_bash_block") public AnimatedImage powerBashBlockImage;
    @LmlActor("Y_block_Broken") public AnimatedImage brokenYBlockImage;
    @LmlActor("O_block_Broken") public AnimatedImage brokenOBlockImage;
    @LmlActor("K_block_Broken") public AnimatedImage brokenKBlockImage;
    @LmlActor("E_block_Broken") public AnimatedImage brokenEBlockImage;
    @LmlActor("L_block_Broken") public AnimatedImage brokenLBlockImage;
    @LmlActor("Bash_block_Broken") public AnimatedImage brokenBashBlockImage;
    @LmlActor("stone") public Image stoneBlockImage;
    @LmlActor("gameClock") public GameClock gameClock;
    @LmlActor("clear_block") public Image clearBlock;
    @LmlActor("area1") public GameBlockArea area1;
    @LmlActor("area2") public GameBlockArea area2;

    @Initiate
    private void initilize() {
        this.uiAssetMap = new ObjectMap<>();
    }

    //Get Objects Factory
    public YokelObjectFactory getFactory(){
        if(factory == null){
            factory = new YokelObjectFactory(this);
        }
        return this.factory;
    }

    @Destroy
    private void destroy(){
        uiAssetMap.clear();
    }

    //Load actors into asset map
    public void loadActors(Iterable<? extends Actor> actors){
        if(actors != null){
            for(Actor actor : actors){
                if(actor != null){
                    uiAssetMap.put(actor.getName(),actor);
                }
            }
        }
    }

    public void setActors(){
        yBlockImage = getActor("Y_block");/*
        @LmlActor("O_block") public Image oBlockImage;
        @LmlActor("K_block") public Image kBlockImage;
        @LmlActor("E_block") public Image eBlockImage;
        @LmlActor("L_block") public Image lBlockImage;
        @LmlActor("Bash_block") public Image bashBlockImage;
        @LmlActor("defense_Y_block") public AnimatedImage defenseYBlockImage;
        @LmlActor("defense_O_block") public AnimatedImage defenseOBlockImage;
        @LmlActor("defense_K_block") public AnimatedImage defenseKBlockImage;
        @LmlActor("defense_E_block") public AnimatedImage defenseEBlockImage;
        @LmlActor("defense_L_block") public AnimatedImage defenseLBlockImage;
        @LmlActor("defense_Bash_block") public AnimatedImage defenseBashBlockImage;
        @LmlActor("power_Y_block") public AnimatedImage powerYBlockImage;
        @LmlActor("power_O_block") public AnimatedImage powerOBlockImage;
        @LmlActor("power_K_block") public AnimatedImage powerKBlockImage;
        @LmlActor("power_E_block") public AnimatedImage powerEBlockImage;
        @LmlActor("power_L_block") public AnimatedImage powerLBlockImage;
        @LmlActor("power_bash_block") public AnimatedImage powerBashBlockImage;
        @LmlActor("Y_block_Broken") public AnimatedImage brokenYBlockImage;
        @LmlActor("O_block_Broken") public AnimatedImage brokenOBlockImage;
        @LmlActor("K_block_Broken") public AnimatedImage brokenKBlockImage;
        @LmlActor("E_block_Broken") public AnimatedImage brokenEBlockImage;
        @LmlActor("L_block_Broken") public AnimatedImage brokenLBlockImage;
        @LmlActor("Bash_block_Broken") public AnimatedImage brokenBashBlockImage;
        @LmlActor("stone") public Image stoneBlockImage;
        @LmlActor("gameClock") public GameClock gameClock;
        @LmlActor("clear_block") public Image clearBlock;*/
    }

    public Skin getSkin(){
        return skinService.getSkin();
    }

    public Stage getStage() {
        return interfaceService.getCurrentController().getStage();
    }

    public Image getImage(String name){
        if(name != null){
            Image image = new Image();
            image.setName(name);
            return image;
        }
        return null;
    }

    public AnimatedImage getAnimatedImage(String name){
        if(name != null){
            AnimatedImage animatedImage = new AnimatedImage();
            animatedImage.setName(name);
            return animatedImage;
        }
        return null;
    }

    public void loadDrawables(){
        for(Actor asset : getAssets()){
            if(asset != null){
                loadDrawable(asset);
            }
        }
    }

    public void loadDrawable(Actor actor){
        if(actor != null){
            loadDrawable(actor, actor.getName());
        }
    }

    public void loadDrawable(Actor actor, String name){
        if(actor instanceof AnimatedImage){
            AnimatedImage image = (AnimatedImage) actor;
            image.setFrames(getDrawableFrames(name));
        } else if(actor instanceof Image){
            Image image = (Image) actor;
            image.setDrawable(interfaceService.getSkin(), name);
        }
    }

    public Array<Drawable> getDrawableFrames(String regionName) {
        int i = 1;
        regionName +=  "_";
        Drawable drawable = getDrawable(regionName, i);

        Array<Drawable> drawables = new Array<>();
        while(drawable != null){
            drawables.add(drawable);
            i++;
            drawable = getDrawable(regionName, i);
        }
        return drawables;
    }

    public Drawable getDrawable(String regionNames, int i){
        try{
            return interfaceService.getSkin().getDrawable(regionNames + i);
        } catch(GdxRuntimeException e) {
            return null;
        }
    }

    public Actor getActor(String name) {
        return uiAssetMap.get(name);
    }

    public ObjectMap.Values<Actor> getAssets(){
        if(this.uiAssetMap != null){
            return uiAssetMap.values();
        } else {
            return GdxMaps.<String, Actor>newObjectMap().values();
        }
    }
}
