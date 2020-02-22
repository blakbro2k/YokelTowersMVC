package net.asg.games.game.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;

import net.asg.games.provider.actors.GameBlock;
import net.asg.games.service.UserInterfaceService;

public class YokelObjectFactory {
    private UserInterfaceService userInterfaceService;

    public YokelObjectFactory(UserInterfaceService userInterfaceService){
        if(userInterfaceService == null) throw new GdxRuntimeException("userInterfaceService was not initialized.");
        this.userInterfaceService = userInterfaceService;
        userInterfaceService.loadActors(createActors());
        userInterfaceService.loadDrawables();
    }

    private Iterable<? extends Actor> createActors() {
        Array<Actor> actors = new Array<>();

        String[] imageNames = new String[]{"Y_block","O_block","K_block","E_block","L_block","Bash_block","stone","clear_block"};
        for(String imageName : imageNames){
            addActor(actors, userInterfaceService.getImage(imageName));
        }

        String[] animatedImageNames = {"defense_Y_block","defense_O_block","defense_K_block","defense_E_block","defense_L_block",
                "defense_Bash_block","power_Y_block","power_O_block","power_K_block","power_E_block","power_L_block","power_bash_block",
                "Y_block_Broken","O_block_Broken","K_block_Broken","E_block_Broken","L_block_Broken","Bash_block_Broken"};
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
            return new GameBlock(userInterfaceService.getSkin(), getBlockImageName(0));
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
