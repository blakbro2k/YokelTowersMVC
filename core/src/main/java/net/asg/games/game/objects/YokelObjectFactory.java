package net.asg.games.game.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Pool;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.provider.actors.GameClock;
import net.asg.games.service.UserInterfaceService;

public class YokelObjectFactory {
    @Inject private UserInterfaceService userInterfaceService;

    public YokelObjectFactory(){
        userInterfaceService.loadDrawables();
    }

    // YokelBlock pool.
    private final Pool<YokelBlock> yokelBlockPool = new Pool<YokelBlock>() {
        @Override
        protected YokelBlock newObject() {
            return new YokelBlock();
        }
    };

    public YokelObject getObject(){
        return yokelBlockPool.obtain();
    }

    public void freeObject(YokelBlock block){
        if(block != null){
            yokelBlockPool.free(block);
        }
    }
}
