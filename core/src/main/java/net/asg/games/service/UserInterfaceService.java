package net.asg.games.service;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.SkinService;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.provider.actors.GameClock;

import java.util.Collection;


@Component
public class UserInterfaceService {
    @Inject private AssetService assetService;
    @Inject private InterfaceService interfaceService;
    @Inject private SkinService skinService;

    private ObjectMap<String, Actor> uiAssetMap;

    @Initiate
    private void initilize() {
        this.uiAssetMap = new ObjectMap<>();
        loadAssets();
    }

    private void loadAssets(){
        for(Actor actor : createActors()){
           if(actor != null){System.out.println("loading asset: " + actor.getName());
               uiAssetMap.put(actor.getName(),actor);
            }
        }
    }

    private Iterable<? extends Actor> createActors() {
        Array<Actor> actors = new Array<>();

        String[] imageNames = new String[]{"Y_block","O_block","K_block","E_block","L_block","Bash_block","stone","clear_block"};
        for(String imageName : imageNames){
            actors.add(addImageName(imageName));
        }

        String[] animatedImageNames = {"defense_Y_block","defense_O_block","defense_K_block","defense_E_block","defense_L_block",
                "defense_Bash_block","power_Y_block","power_O_block","power_K_block","power_E_block","power_L_block","power_bash_block",
                "Y_block_Broken","O_block_Broken","K_block_Broken","E_block_Broken","L_block_Broken","Bash_block_Broken"};
        for(String aniImageName : animatedImageNames){
            actors.add(addAnimatedImageName(aniImageName));
        }
        return actors;
    }

    private Image addImageName(String name){
        if(name != null){
            Image image = new Image();
            image.setName(name);
            return image;
        }
        return null;
    }

    private AnimatedImage addAnimatedImageName(String name){
        if(name != null){
            AnimatedImage animatedImage = new AnimatedImage();
            animatedImage.setName(name);
            return animatedImage;
        }
        return null;
    }


    public GameBlock getGameBlock(YokelBlock yokelBlock){
        return null;
    }

    public void loadDrawables(){
        for(Actor asset : uiAssetMap.values()){
            if(asset != null){
                loadDrawable(asset);
            }
        }
    }

    public void loadDrawable(Actor actor){
        if(actor != null){
            /*String actorName = actor.getName();
            Actor cachedActore = uiAssetMap.get(actorName);
            if(cachedActore == null){
                actor = cachedActore;
            } else {*/
                loadDrawable(actor, actor.getName());
            //}
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
}
