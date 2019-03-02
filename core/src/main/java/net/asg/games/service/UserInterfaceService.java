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
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.provider.actors.GameBlock;


@Component
public class UserInterfaceService {
    @Inject private AssetService assetService;
    @Inject private InterfaceService interfaceService;
    @Inject private SkinService skinService;

    private ObjectMap<String, Animation<TextureRegion>> animationMap;

    @Initiate
    private void initilize() {
        this.animationMap = new ObjectMap<>();
    }

    public GameBlock getGameBlock(YokelBlock yokelBlock){
        return null;
    }

    public void loadDrawable(Actor actor){
        if(actor != null){
            String actorName = actor.getName();
            loadDrawable(actor, actorName);
        }
    }

    private void loadDrawable(Actor actor, String name){
        if(actor instanceof Image){
            Image image = (Image) actor;
            try{
                image.setDrawable(interfaceService.getSkin(), name);
            } catch(GdxRuntimeException ge){
                ge.printStackTrace();
            }
        }
        if(actor instanceof AnimatedImage){
            AnimatedImage image = (AnimatedImage) actor;
            try{
                image.setFrames(getDrawableFrames(name));
            } catch(GdxRuntimeException ge){
                ge.printStackTrace();
            }
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
