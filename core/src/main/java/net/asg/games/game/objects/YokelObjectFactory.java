package net.asg.games.game.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;

import net.asg.games.provider.actors.GameBlock;
import net.asg.games.service.UserInterfaceService;

public class YokelObjectFactory {
    private UserInterfaceService userInterfaceService;

    public YokelObjectFactory(UserInterfaceService userInterfaceService, Array<String> images, Array<String> animatedImages){
        if(userInterfaceService == null) throw new GdxRuntimeException("userInterfaceService was not initialized.");
        if(images == null) throw new GdxRuntimeException("Images to load cannot be null.");
        if(animatedImages == null) throw new GdxRuntimeException("Animated Images to load cannot be null.");
        this.userInterfaceService = userInterfaceService;
        userInterfaceService.loadActors(createActors(images, animatedImages));
        userInterfaceService.loadDrawables();
    }

    private Iterable<? extends Actor> createActors(Array<String> imageNames, Array<String> animatedImageNames) {
        Array<Actor> actors = new Array<>();

        for(String imageName : imageNames){
            addActor(actors, userInterfaceService.getImage(imageName));
        }

        for(String aniImageName : animatedImageNames){
            addActor(actors, userInterfaceService.getAnimatedImage(aniImageName));
        }
        return actors;
    }

    private void addActor(Array<Actor> actors, Actor actor){
        if(actor != null && actors != null){
            actors.add(actor);
        }
    }

    public UserInterfaceService getUserInterfaceService() {
        return userInterfaceService;
    }

    // YokelBlock pool.
    private final Pool<GameBlock> yokelGameBlockPool = new Pool<GameBlock>() {
        @Override
        protected GameBlock newObject() {
            return new GameBlock(userInterfaceService.getSkin(), getBlockImageName(YokelBlock.CLEAR_BLOCK));
        }
    };

    public GameBlock getGameBlock(int blockType){
        GameBlock block = yokelGameBlockPool.obtain();
        block.setImage(blockType);
        if(blockType != YokelBlock.CLEAR_BLOCK){
            block.setActive(true);
        }
        return block;
    }

    public void freeObject(GameBlock block){
        if(block != null){
            yokelGameBlockPool.free(block);
        }
    }

    public String getBlockImageName(int blockValue){
        switch (blockValue){
            case YokelBlock.CLEAR_BLOCK:
                return "clear_block";
            case YokelBlock.Y_BLOCK:
                return "Y_block";
            case YokelBlock.O_BLOCK:
                return "O_block";
            case YokelBlock.K_BLOCK:
                return "K_block";
            case YokelBlock.E_BLOCK:
                return "E_block";
            case YokelBlock.L_BLOCK:
                return "L_block";
            case YokelBlock.EX_BLOCK:
                return "Bash_block";
            case YokelBlock.MIDAS:
                return "Midas";
            case YokelBlock.MEDUSA:
                return "Medusa";
            case YokelBlock.STONE:
                return "Stone";
            default:
                return "";
        }
    }
}
