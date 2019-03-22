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

    private Image yBlockImage;
    private Image oBlockImage;
    private Image kBlockImage;
    private Image eBlockImage;
    private Image lBlockImage;
    private Image bashBlockImage;
    private AnimatedImage defenseYBlockImage;
    private AnimatedImage defenseOBlockImage;
    private AnimatedImage defenseKBlockImage;
    private AnimatedImage defenseEBlockImage;
    private AnimatedImage defenseLBlockImage;
    private AnimatedImage defenseBashBlockImage;
    private AnimatedImage powerYBlockImage;
    private AnimatedImage powerOBlockImage;
    private AnimatedImage powerKBlockImage;
    private AnimatedImage powerEBlockImage;

    private AnimatedImage powerLBlockImage;
    private AnimatedImage powerBashBlockImage;
    private AnimatedImage brokenYBlockImage;
    private AnimatedImage brokenOBlockImage;
    private AnimatedImage brokenKBlockImage;
    private AnimatedImage brokenEBlockImage;
    private AnimatedImage brokenLBlockImage;
    private AnimatedImage brokenBashBlockImage;
    private Image stoneBlockImage;
    private Image clearBlock;

    public YokelObjectFactory(){

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
