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
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.provider.actors.GameClock;


@Component
public class UserInterfaceService {
    @Inject private AssetService assetService;
    @Inject private InterfaceService interfaceService;
    @Inject private SkinService skinService;

    @LmlActor("Y_block") public Image yBlockImage;
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
    @LmlActor("gameClock") private GameClock gameClock;

    @LmlAction("toggleGameStart")
    public void toggleGameStart(){
        System.out.println("gameClock=" + gameClock);
    }

    private ObjectMap<String, Actor> uiAssetMap;

    @Initiate
    private void initilize() {
        this.uiAssetMap = new ObjectMap<>();
    }

    public GameBlock getGameBlock(YokelBlock yokelBlock){
        return null;
    }

    public void loadDrawable(Actor actor){
        if(actor != null){
            loadDrawable(actor, actor.getName());
        }
    }

    private void loadDrawable(Actor actor, String name){
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
