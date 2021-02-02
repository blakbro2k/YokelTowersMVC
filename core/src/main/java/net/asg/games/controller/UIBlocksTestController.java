package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewController;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewInitializer;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GameJoinWidget;
import net.asg.games.service.SessionService;
import net.asg.games.service.UserInterfaceService;

@View(id = ControllerNames.UI_BLOCK_TEST_VIEW, value = "ui/templates/uiblocktest.lml")
public class UIBlocksTestController extends ApplicationAdapter implements ViewRenderer, ViewInitializer, ActionContainer {
    @Inject private UserInterfaceService uiService;
    @Inject private SessionService sessionService;

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
    @LmlActor("power_Y_block_preview") private Image poweryBlockImagePreview;
    @LmlActor("power_O_block_preview") private Image poweroBlockImagePreview;
    @LmlActor("power_K_block_preview") private Image powerkBlockImagePreview;
    @LmlActor("power_E_block_preview") private Image powereBlockImagePreview;
    @LmlActor("power_L_block_preview") private Image powerlBlockImagePreview;
    @LmlActor("power_Bash_block_preview") private Image powerbashBlockImagePreview;
    @LmlActor("defense_Y_block") private AnimatedImage defenseYBlockImage;
    @LmlActor("defense_O_block") private AnimatedImage defenseOBlockImage;
    @LmlActor("defense_K_block") private AnimatedImage defenseKBlockImage;
    @LmlActor("defense_E_block") private AnimatedImage defenseEBlockImage;
    @LmlActor("defense_L_block") private AnimatedImage defenseLBlockImage;
    @LmlActor("defense_Bash_block") private AnimatedImage defenseBashBlockImage;
    @LmlActor("defense_Y_block_preview") private Image defenseYBlockImagePreview;
    @LmlActor("defense_O_block_preview") private Image defenseOBlockImagePreview;
    @LmlActor("defense_K_block_preview") private Image defenseKBlockImagePreview;
    @LmlActor("defense_E_block_preview") private Image defenseEBlockImagePreview;
    @LmlActor("defense_L_block_preview") private Image defenseLBlockImagePreview;
    @LmlActor("defense_Bash_block_preview") private Image defensebashBlockImagePreview;
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
    @LmlActor("medusa") private AnimatedImage medusaImage;
    @LmlActor("top_midas") private AnimatedImage topMidasImage;
    @LmlActor("mid_midas") private AnimatedImage midMidasImage;
    @LmlActor("bottom_midas") private AnimatedImage bottomMidasImage;
    @LmlActor("stone") private Image stoneBlockImage;
    @LmlActor("gameClock") private GameClock gameClock;
    @LmlActor("clear_block") private Image clearBlock;
    @LmlActor("clear_block_preview") private Image clearBlockPreview;
    @LmlActor("1") private GameBoard area1;
    @LmlActor("2") private GameBoard area2;
    @LmlActor("joinReady") private GameJoinWidget joinReady;

    YokelGameBoard boardState;

    @Override
    public void render(Stage stage, float delta) {
        area1.update(boardState);
        checkInput();
        stage.act(delta);
        stage.draw();
    }

    private void initiate(){
        initiateActors();
        //boardState = getTestBoard();
        boardState =  new YokelGameBoard(1L);
        area1.setPlayerView(true);
        area1.setActive(true);
        area1.setPreview(false);
        area1.update(boardState);

        joinReady.setIsGameReady(true);
            /*area = new GameBoard(uiService.getSkin());
            YokelPlayer player = new YokelPlayer("Test Player One",2000, 5);
            area.setPlayerLabel(player.getNameLabel().toString());
            area.setBoardNumber(1);
            area.setPlayerView(true);
            area.setActive(true);
            area.setPreview(false);
            area.update(boardState);*/
            //area2.updateData(getTestBoard());
    }

    private void initiateActors() {
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
        uiService.loadDrawable(poweryBlockImagePreview);
        uiService.loadDrawable(powerkBlockImagePreview);
        uiService.loadDrawable(poweroBlockImagePreview);
        uiService.loadDrawable(powereBlockImagePreview);
        uiService.loadDrawable(powerlBlockImagePreview);
        uiService.loadDrawable(powerbashBlockImagePreview);
        uiService.loadDrawable(defenseYBlockImagePreview);
        uiService.loadDrawable(defenseOBlockImagePreview);
        uiService.loadDrawable(defenseKBlockImagePreview);
        uiService.loadDrawable(defenseEBlockImagePreview);
        uiService.loadDrawable(defenseLBlockImagePreview);
        uiService.loadDrawable(defensebashBlockImagePreview);
        uiService.loadDrawable(medusaImage);
        uiService.loadDrawable(topMidasImage);
        uiService.loadDrawable(midMidasImage);
        uiService.loadDrawable(bottomMidasImage);
    }

    @LmlAction("toggleGameStart")
    private void toggleGameStart() {
        if(gameClock == null) return;
        if(!gameClock.isRunning()){
            gameClock.start();
        } else {
            gameClock.stop();
        }
    }

    @LmlAction("getTestBoard")
    private YokelGameBoard getTestBoard(){
        YokelGameBoard board = new YokelGameBoard(1L);

        board.setCell(0,0, YokelBlock.Y_BLOCK);
        board.setCell(0,1, YokelBlock.A_BLOCK);
        board.setCell(0,2, YokelBlock.H_BLOCK);
        board.setCell(0,3, YokelBlock.Op_BLOCK);
        board.setCell(0,4, YokelBlock.Oy_BLOCK);
        board.setCell(0,5, YokelBlock.EX_BLOCK);

        board.setCell(1,0, YokelBlock.DEFENSIVE_Y_BLOCK_MINOR);
        board.setCell(1,1, YokelBlock.DEFENSIVE_O_BLOCK_REGULAR);
        board.setCell(1,2, YokelBlock.DEFENSIVE_K_BLOCK_MEGA);
        board.setCell(1,3, YokelBlock.DEFENSIVE_E_BLOCK_MINOR);
        board.setCell(1,4, YokelBlock.DEFENSIVE_L_BLOCK_MINOR);
        board.setCell(1,5, YokelBlock.DEFENSIVE_BASH_BLOCK_REGULAR);

        board.setCell(2,0, YokelBlock.OFFENSIVE_Y_BLOCK_MEGA);
        board.setCell(2,1, YokelBlock.OFFENSIVE_O_BLOCK_MEGA);
        board.setCell(2,2, YokelBlock.OFFENSIVE_K_BLOCK_MEGA);
        board.setCell(2,3, YokelBlock.OFFENSIVE_E_BLOCK_REGULAR);
        board.setCell(2,4, YokelBlock.OFFENSIVE_L_BLOCK_REGULAR);
        board.setCell(2,5, YokelBlock.OFFENSIVE_BASH_BLOCK_MINOR);

        board.setCell(3,0, YokelBlock.STONE);
        board.setCell(3,1, getRandomBlockId());
        board.setCell(3,2, getRandomBlockId());
        board.setCell(3,3, getRandomBlockId());
        board.setCell(3,4, getRandomBlockId());
        board.setCell(3,5, getRandomBlockId());

        board.setCell(4,0, getRandomBlockId());
        board.setCell(4,1, getRandomBlockId());
        board.setCell(4,2, getRandomBlockId());
        board.setCell(4,3, getRandomBlockId());
        board.setCell(4,4, getRandomBlockId());
        board.setCell(4,5, getRandomBlockId());

        board.setCell(5,0, getRandomBlockId());
        board.setCell(5,1, getRandomBlockId());
        board.setCell(5,2, getRandomBlockId());
        board.setCell(5,3, getRandomBlockId());
        board.setCell(5,4, getRandomBlockId());
        board.setCell(5,5, getRandomBlockId());

        board.setCell(6,0, getRandomBlockId());
        board.setCell(3,0, YokelBlock.STONE);
        board.setCell(6,2, getRandomBlockId());
        board.setCell(6,3, getRandomBlockId());
        board.setCell(6,4, getRandomBlockId());
        board.setCell(6,5, getRandomBlockId());

        board.setCell(7,0, getRandomBlockId());
        board.setCell(7,1, getRandomBlockId());
        board.setCell(7,2, getRandomBlockId());
        board.setCell(7,3, getRandomBlockId());
        board.setCell(7,4, getRandomBlockId());
        board.setCell(7,5, getRandomBlockId());

        board.setCell(8,0, getRandomBlockId());
        board.setCell(8,1, getRandomBlockId());
        board.setCell(8,2, getRandomBlockId());
        board.setCell(8,3, getRandomBlockId());
        board.setCell(8,4, getRandomBlockId());
        board.setCell(8,5, getRandomBlockId());

        board.setCell(9,0, getRandomBlockId());
        board.setCell(9,1, getRandomBlockId());
        board.setCell(9,2, getRandomBlockId());
        board.setCell(3,0, YokelBlock.STONE);
        board.setCell(9,4, getRandomBlockId());
        board.setCell(9,5, getRandomBlockId());

        board.setCell(10,0, getRandomBlockId());
        board.setCell(10,1, getRandomBlockId());
        board.setCell(10,2, getRandomBlockId());
        board.setCell(10,3, getRandomBlockId());
        board.setCell(10,4, getRandomBlockId());
        board.setCell(10,5, getRandomBlockId());

        board.setCell(11,0, getRandomBlockId());
        board.setCell(11,1, getRandomBlockId());
        board.setCell(11,2, getRandomBlockId());
        board.setCell(11,3, getRandomBlockId());
        board.setCell(11,4, getRandomBlockId());
        board.setCell(11,5, getRandomBlockId());

        board.setCell(12,0, getRandomBlockId());
        board.setCell(12,1, getRandomBlockId());
        board.setCell(12,2, getRandomBlockId());
        board.setCell(12,3, getRandomBlockId());
        board.setCell(3,0, YokelBlock.STONE);
        board.setCell(12,5, getRandomBlockId());

        board.setCell(13,0, getRandomBlockId());
        board.setCell(13,1, getRandomBlockId());
        board.setCell(13,2, getRandomBlockId());
        board.setCell(13,3, getRandomBlockId());
        board.setCell(13,4, getRandomBlockId());
        board.setCell(13,5, getRandomBlockId());

        board.setCell(14,0, getRandomBlockId());
        board.setCell(14,1, getRandomBlockId());
        board.setCell(14,2, getRandomBlockId());
        board.setCell(14,3, getRandomBlockId());
        board.setCell(14,4, getRandomBlockId());
        board.setCell(14,5, getRandomBlockId());

        board.setCell(15,0, getRandomBlockId());
        board.setCell(15,1, getRandomBlockId());
        board.setCell(15,2, getRandomBlockId());
        board.setCell(15,3, getRandomBlockId());
        board.setCell(15,4, getRandomBlockId());
        board.setCell(15,5, getRandomBlockId());

        return board;
    }

    private int getRandomBlockId(){
        return MathUtils.random(YokelBlock.EX_BLOCK);
    }

    public void checkInput(){
        if(area1 == null) return;
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, 3)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, 2)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.A_BLOCK, 3)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.A_BLOCK, 2)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.H_BLOCK, 5)));
            System.out.println(boardState);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.H_BLOCK, 2)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Op_BLOCK, 3)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.Op_BLOCK, 2)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, 3)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            boardState.handlePower(YokelBlockEval.addPowerBlockFlag(YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, 2)));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            System.out.println(12292);
            System.out.println(YokelBlockEval.getID(12292));
            System.out.println(YokelBlockEval.getIDFlag(YokelBlockEval.getID(12292), 12292));

            System.out.println(32820);
            System.out.println(YokelBlockEval.hasAddedByYahooFlag(32820));
            System.out.println(YokelBlockEval.getID(32820));
            System.out.println(YokelBlockEval.getIDFlag(YokelBlockEval.getID(32820), 32820));
        }
    }

    @Override
    public void initialize(Stage stage, ObjectMap<String, Actor> actorMappedByIds) {
        initiate();
    }

    @Override
    public void destroy(ViewController viewController) {
        boardState.dispose();
    }
}