package net.asg.games.service;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.SkinService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.controller.LoadingController;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.utils.Util;

@Component
public class UserInterfaceService {
    @Inject private AssetService assetService;
    @Inject private InterfaceService interfaceService;
    @Inject private SkinService skinService;
    @Inject private LoadingController controller;

    private ObjectMap<String, Actor> uiAssetMap;
    private YokelObjectFactory factory;

    @Initiate
    private void initilize() {
        this.uiAssetMap = new ObjectMap<>();
    }

    //Get Objects Factory
    public YokelObjectFactory getFactory(){
        if(factory == null){
            ObjectMap<String, Array> imageMap = buildImageNames();
            factory = new YokelObjectFactory(this, imageMap.get("imageNames"), imageMap.get("animatedImageNames"));
        }
        return this.factory;
    }

    @Destroy
    private void destroy(){
        uiAssetMap.clear();
        factory.dispose();
    }

    private ObjectMap<String, Array> buildImageNames(){
        ObjectMap<String, Array> imageMap = GdxMaps.newObjectMap();
        Array<String> images = GdxArrays.newArray();
        Array<String> animatedImages = GdxArrays.newArray();

        ObjectMap<String, Integer> regionMap = buildRegionsMap();

        for(String regionName : regionMap.keys()){
            if(regionName != null){
                int index = regionMap.get(regionName);
                if(index > 1){
                    animatedImages.add(regionName);
                } else {
                    images.add(regionName);
                }
            }
        }
        imageMap.put("imageNames", images);
        imageMap.put("animatedImageNames", animatedImages);
        return imageMap;
    }

    private ObjectMap<String, Integer> buildRegionsMap(){
        ObjectMap<String, Integer> regionsMap = GdxMaps.newObjectMap();
        for(TextureAtlas.AtlasRegion region : controller.getGameAtlas().getRegions()){
            if(region != null){
                String key = region.name;
                if(!regionsMap.containsKey(key)){
                    regionsMap.put(key, 1);
                } else {
                    int index = regionsMap.get(key);
                    regionsMap.put(key, ++index);
                }
            }
        }
        return regionsMap;
    }

    //Load actors into asset map
    public void loadActors(Iterable<? extends Actor> actors){
        if(actors != null){
            for(Actor actor : actors){
                if(actor != null){
                    uiAssetMap.put(actor.getName(), actor);
                }
            }
        }
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
            image.setDelay(GameBlock.ANIMATION_DELAY);
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
            drawable = getDrawable(regionName, ++i);
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
