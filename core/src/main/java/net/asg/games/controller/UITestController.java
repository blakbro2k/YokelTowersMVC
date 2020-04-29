package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GamePiece;
import net.asg.games.provider.actors.GamePowersQueue;
import net.asg.games.service.UserInterfaceService;

@View(id = "uitest", value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    @Inject private UserInterfaceService uiService;

    @LmlActor("Y_block") private Image yBlockImage;
    @LmlActor("O_block") private Image oBlockImage;
    @LmlActor("K_block") private Image kBlockImage;
    @LmlActor("E_block") private Image eBlockImage;
    @LmlActor("L_block") private Image lBlockImage;
    @LmlActor("Bash_block") private Image bashBlockImage;
    @LmlActor("Y_block_preview") private Image yBlockImagePreview;
    @LmlActor("O_block_preview") private Image oBlockImagePreview;
    @LmlActor("K_block_preview") private Image kBlockImagePreview;
    @LmlActor("E_block_preview") private Image eBlockImagePreview;
    @LmlActor("L_block_preview") private Image lBlockImagePreview;
    @LmlActor("Bash_block_preview") private Image bashBlockImagePreview;
    @LmlActor("defense_Y_block") private AnimatedImage defenseYBlockImage;
    @LmlActor("defense_O_block") private AnimatedImage defenseOBlockImage;
    @LmlActor("defense_K_block") private AnimatedImage defenseKBlockImage;
    @LmlActor("defense_E_block") private AnimatedImage defenseEBlockImage;
    @LmlActor("defense_L_block") private AnimatedImage defenseLBlockImage;
    @LmlActor("defense_Bash_block") private AnimatedImage defenseBashBlockImage;
    @LmlActor("power_Y_block") private AnimatedImage powerYBlockImage;
    @LmlActor("power_O_block") private AnimatedImage powerOBlockImage;
    @LmlActor("power_K_block") private AnimatedImage powerKBlockImage;
    @LmlActor("power_E_block") private AnimatedImage powerEBlockImage;
    @LmlActor("power_L_block") private AnimatedImage powerLBlockImage;
    @LmlActor("power_bash_block") private AnimatedImage powerBashBlockImage;
    @LmlActor("Y_block_Broken") private AnimatedImage brokenYBlockImage;
    @LmlActor("O_block_Broken") private AnimatedImage brokenOBlockImage;
    @LmlActor("K_block_Broken") private AnimatedImage brokenKBlockImage;
    @LmlActor("E_block_Broken") private AnimatedImage brokenEBlockImage;
    @LmlActor("L_block_Broken") private AnimatedImage brokenLBlockImage;
    @LmlActor("Bash_block_Broken") private AnimatedImage brokenBashBlockImage;
    @LmlActor("stone") private Image stoneBlockImage;
    @LmlActor("gameClock") private GameClock gameClock;
    @LmlActor("clear_block") private Image clearBlock;
    @LmlActor("clear_block_preview") private Image clearBlockPreview;
    @LmlActor("1:area") private GameBlockArea area1;
    @LmlActor("2:area") private GameBlockArea area2;
    @LmlActor("3:area") private GameBlockArea area3;
    @LmlActor("4:area") private GameBlockArea area4;
    @LmlActor("5:area") private GameBlockArea area5;
    @LmlActor("6:area") private GameBlockArea area6;
    @LmlActor("7:area") private GameBlockArea area7;
    @LmlActor("8:area") private GameBlockArea area8;
    @LmlActor("1:next") private GamePiece next1;
    @LmlActor("2:next") private GamePiece next2;
    @LmlActor("1:powers") private GamePowersQueue powersQueue1;
    @LmlActor("2:powers") private GamePowersQueue powersQueue2;

    private boolean isInitiated;
    private Queue<GameBlock> powerUps = new Queue<>();

    @Override
    public void render(Stage stage, float delta) {
        initiate();
        stage.act(delta);
        stage.draw();
    }

    private void initiate(){
        if(!isInitiated){
            isInitiated = true;
            initiateActors();
            YokelPiece piece1 = new YokelPiece(1,32,84,112);
            YokelPiece piece2 = new YokelPiece(2,68,53,51);

            next1.setData(piece1.toString());
            next2.setData(piece2.toString());

            //area1.updateData(getTestBoard());
            //area2.updateData(getTestBoard());
            //area5.updateData(getTestBoard());

            //Queue<YokelBlock>
            //powersQueue1.update();
            //powersQueue1.update();
        }
    }

    public void initiateActors() {
        uiService.loadDrawable(yBlockImage);
        uiService.loadDrawable(oBlockImage);
        uiService.loadDrawable(kBlockImage);
        uiService.loadDrawable(eBlockImage);
        uiService.loadDrawable(lBlockImage);
        uiService.loadDrawable(bashBlockImage);
        uiService.loadDrawable(defenseYBlockImage);
        uiService.loadDrawable(defenseOBlockImage);
        uiService.loadDrawable(defenseKBlockImage);
        uiService.loadDrawable(defenseEBlockImage);
        uiService.loadDrawable(defenseLBlockImage);
        uiService.loadDrawable(defenseBashBlockImage);
        uiService.loadDrawable(powerYBlockImage);
        uiService.loadDrawable(powerOBlockImage);
        uiService.loadDrawable(powerKBlockImage);
        uiService.loadDrawable(powerEBlockImage);
        uiService.loadDrawable(powerLBlockImage);
        uiService.loadDrawable(powerBashBlockImage);
        uiService.loadDrawable(brokenYBlockImage);
        uiService.loadDrawable(brokenOBlockImage);
        uiService.loadDrawable(brokenKBlockImage);
        uiService.loadDrawable(brokenEBlockImage);
        uiService.loadDrawable(brokenLBlockImage);
        uiService.loadDrawable(brokenBashBlockImage);
        uiService.loadDrawable(stoneBlockImage);
        uiService.loadDrawable(clearBlock);
        uiService.loadDrawable(clearBlockPreview);
        uiService.loadDrawable(yBlockImagePreview);
        uiService.loadDrawable(oBlockImagePreview);
        uiService.loadDrawable(kBlockImagePreview);
        uiService.loadDrawable(eBlockImagePreview);
        uiService.loadDrawable(lBlockImagePreview);
        uiService.loadDrawable(bashBlockImagePreview);
    }

    @LmlAction("toggleGameStart")
    public void toggleGameStart(Actor actor) {
        if(gameClock == null) return;// gameClock = uiService.getImage("Y_block")
        if(!gameClock.isRunning()){
            gameClock.start();
        } else {
            gameClock.stop();
        }
    }

    @LmlAction("setGamePiece")
    private void setGamePiece(){
        //GamePiece gp = new GamePiece(uiService.getSkin(), new String[]{"12","16","3"});
        //gp.setData(new String[]{"12","16","3"});
        //System.out.println("lksjfd\n" + gp);
        //uiService.area1.setGamePiece(gp);
    }

    @LmlAction("getTestBoard")
    private YokelGameBoard getTestBoard(){
        YokelGameBoard board = new YokelGameBoard(1L);

        board.setCell(0,0, getRandomBlockId());
        board.setCell(0,1, getRandomBlockId());
        board.setCell(0,2, getRandomBlockId());
        board.setCell(0,3, getRandomBlockId());
        board.setCell(0,4, getRandomBlockId());
        board.setCell(0,5, getRandomBlockId());

        board.setCell(1,0, getRandomBlockId());
        board.setCell(1,1, getRandomBlockId());
        board.setCell(1,2, getRandomBlockId());
        board.setCell(1,3, getRandomBlockId());
        board.setCell(1,4, getRandomBlockId());
        board.setCell(1,5, getRandomBlockId());

        board.setCell(2,0, getRandomBlockId());
        board.setCell(2,1, getRandomBlockId());
        board.setCell(2,2, getRandomBlockId());
        board.setCell(2,3, getRandomBlockId());
        board.setCell(2,4, getRandomBlockId());
        board.setCell(2,5, 112);
        return board;
    }

    private int getRandomBlockId(){
        return MathUtils.random(YokelBlock.EX_BLOCK);
    }

    @LmlAction("getTimerSeconds")
    public int getTimerSeconds() {
        return gameClock.isRunning() ? gameClock.getElapsedSeconds() : 0;
    }
}