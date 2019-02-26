package net.asg.games.service;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.SkinService;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.provider.actors.AnimatedImage;
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

    public AnimatedImage createAnimatedImage(String name, boolean looping){
        return new AnimatedImage(createAnimation(name), looping);
    }

    public Animation<TextureRegion> createAnimation(String regionName) {
        Array<TextureRegion> regions = getTextureRegions(regionName);
        if(regions != null){
            return createAnimation(regionName, regions.size);
        }
        return null;
    }

        public Animation<TextureRegion> createAnimation(String textureName, int maxIndex) {
        if(maxIndex < -1 || maxIndex == 0){
            throw new ArrayIndexOutOfBoundsException("maxIndex: " + maxIndex);
        }
        Animation<TextureRegion> animation = animationMap.get(textureName);

        if(animation == null){
            TextureRegion[] runningFrames;
            if(maxIndex == -1){
                runningFrames = new TextureRegion[1];
                runningFrames[0] = getTextureRegion(textureName,-1);
            } else {
                runningFrames = new TextureRegion[maxIndex];
                for (int i = 1; i < maxIndex + 1; i++) {
                    runningFrames[i-1] = getTextureRegion(textureName,i);
                }
            }

            animation = new Animation<TextureRegion>(1.0f, runningFrames);
            animationMap.put(textureName, animation);
            return animation;
        } else {
            return animation;
        }
    }

    public TextureRegion getTextureRegion(String regionName, int index) {
        return getRegionIndex(getTextureRegions(regionName), index);
    }

    private TextureRegion getRegionIndex(Array<TextureRegion> regions, int index){
        if(regions != null){
            try{
                return regions.get(index);
            } catch (IndexOutOfBoundsException iobe){
                return null;
            }
        }
        return null;
    }

    public Array<TextureRegion> getTextureRegions(String regionName){
        return interfaceService.getSkin().getRegions(regionName);
    }
}
