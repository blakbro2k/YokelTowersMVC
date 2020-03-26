package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GamePiece;
import net.asg.games.service.UserInterfaceService;

@View(id = "uitest", value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
     @Inject private UserInterfaceService uiService;

    @LmlActor("Y_block") private Image yBlockImage;
    @LmlActor("O_block") private Image oBlockImage;
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
    @LmlActor("gameClock") public GameClock gameClock;
    @LmlActor("clear_block") public Image clearBlock;
    @LmlActor("area1") public GameBlockArea area1;
    @LmlActor("area2") public GameBlockArea area2;

    private void loadGameData() {}
    private boolean isInitiated;

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
        GamePiece gp = new GamePiece(uiService.getSkin(), new String[]{"12","16","3"});
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
        board.setCell(2,5, getRandomBlockId());
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
