package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Pool;

public class YokelObjectFactory {
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
