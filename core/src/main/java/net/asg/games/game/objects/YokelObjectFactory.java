package net.asg.games.game.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
            addActor(actors, userInterfaceService.addImageName(imageName));
        }

        String[] animatedImageNames = {"defense_Y_block","defense_O_block","defense_K_block","defense_E_block","defense_L_block",
                "defense_Bash_block","power_Y_block","power_O_block","power_K_block","power_E_block","power_L_block","power_bash_block",
                "Y_block_Broken","O_block_Broken","K_block_Broken","E_block_Broken","L_block_Broken","Bash_block_Broken"};
        for(String aniImageName : animatedImageNames){
            addActor(actors, userInterfaceService.addAnimatedImageName(aniImageName));
        }
        return actors;
    }

    private void addActor(Array<Actor> actors, Actor actor){
        if(actor != null && actors != null){
            actors.add(actor);
        }
    }

    // YokelBlock pool.
    private final Pool<GameBlock> yokelGameBlockPool = new Pool<GameBlock>() {
        @Override
        protected GameBlock newObject() {
            return new GameBlock();
        }
    };

    public GameBlock getGameBlock(int blockType){
        Image image = (Image) userInterfaceService.getActor(getBlockImageName(blockType));
        GameBlock block = yokelGameBlockPool.obtain();
        block.setImage(image);
        if(blockType != YokelBlock.CLEAR){
            block.setActive(true);
        }
        return block;
    }

    public void freeObject(GameBlock block){
        if(block != null){
            yokelGameBlockPool.free(block);
        }
    }

    private String getBlockImageName(int blockValue){
        switch (blockValue){
            case YokelBlock.CLEAR:
                return "clear_block";
            case YokelBlock.NORMAL_Y:
                return "Y_block";
            case YokelBlock.NORMAL_O:
                return "O_block";
            case YokelBlock.NORMAL_K:
                return "K_block";
            case YokelBlock.NORMAL_E:
                return "E_block";
            case YokelBlock.NORMAL_L:
                return "L_block";
            case YokelBlock.NORMAL_EX:
                return "Bash_block";
            case YokelBlock.ATTACK_Y:
                return "power_Y_block";
            case YokelBlock.ATTACK_O:
                return "power_O_block";
            case YokelBlock.ATTACK_K:
                return "power_K_block";
            case YokelBlock.ATTACK_E:
                return "power_E_block";
            case YokelBlock.ATTACK_L:
                return "power_L_block";
            case YokelBlock.ATTACK_EX:
                return "power_bash_block";
            case YokelBlock.DEFENSE_Y:
                return "defense_Y_block";
            case YokelBlock.DEFENSE_O:
                return "defense_O_block";
            case YokelBlock.DEFENSE_K:
                return "defense_K_block";
            case YokelBlock.DEFENSE_E:
                return "defense_E_block";
            case YokelBlock.DEFENSE_L:
                return "defense_L_block";
            case YokelBlock.DEFENSE_EX:
                return "defense_Bash_block";
            case YokelBlock.MIDAS:
                return "stone";
            case YokelBlock.MEDUSA:
                return "stone";
            case YokelBlock.BROKEN_Y:
                return "Y_block_Broken";
            case YokelBlock.BROKEN_O:
                return "O_block_Broken";
            case YokelBlock.BROKEN_K:
                return "K_block_Broken";
            case YokelBlock.BROKEN_E:
                return "E_block_Broken";
            case YokelBlock.BROKEN_L:
                return "L_block_Broken";
            case YokelBlock.BROKEN_EX:
                return "Bash_block_Broken";
            case YokelBlock.STONE:
                return "stone";
            default:
                return "";
        }
    }
    //public YokelBlock
}
