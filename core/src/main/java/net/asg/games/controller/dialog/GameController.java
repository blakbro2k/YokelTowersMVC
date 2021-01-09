package net.asg.games.controller.dialog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogShower;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.controller.ControllerNames;
import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.service.SessionService;
import net.asg.games.utils.YokelUtilities;

import java.util.Iterator;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = ControllerNames.GAME_DIALOG, value = "ui/templates/dialogs/game.lml")
public class GameController implements ViewRenderer, ActionContainer, ViewDialogShower {
    private Logger logger = LoggerService.forClass(GameController.class);

    @Inject private InterfaceService interfaceService;
    @Inject private SessionService sessionService;

    @LmlActor("1:area") private GameBoard area1;
    @LmlActor("2:area") private GameBoard area2;
    @LmlActor("3:area") private GameBoard area3;
    @LmlActor("4:area") private GameBoard area4;
    @LmlActor("5:area") private GameBoard area5;
    @LmlActor("6:area") private GameBoard area6;
    @LmlActor("7:area") private GameBoard area7;
    @LmlActor("8:area") private GameBoard area8;
    @LmlActor("gameClock") private GameClock gameClock;

    private float refresh = 500;
    private boolean isInitiated;
    private boolean isGameOver = false;
    private GameManager game;
    private GameBoard[] gameBoards = new GameBoard[8];
    private GameBoard[] areas;

    @Override
    public void doBeforeShow(Window dialog) {
        logger.debug("doBeforeShow() called");
    }

    @Override
    public void render(Stage stage, float delta) {
        initiate();

        if(++refresh > 300){
            refresh = 0;
            try {
                sessionService.asyncPlayerAllRequest();
                sessionService.asyncTableAllRequest();
            } catch (Exception e) {
                e.printStackTrace();
                sessionService.showError(e);
            }
        }

        /*
        //Fetch GameManager from Server
        GameManager game = fetchGameManagerFromServer();

        //Handle Player input
        handlePlayerInput(game);

        //Update UI base on Game State
        updateGameBoards(game);

        //If Game Over, show it
        if(game.showGameOver()){
            toggleGameStart();
            //stage.addActor(getGameOverActor());
        }*/

        stage.act(delta);
        stage.draw();
    }

    private GameManager fetchGameManagerFromServer() {
        return null;
    }

    private void updateGameBoards(GameManager game) {
        if(game != null){
            for(int board = 0; board < gameBoards.length; board++){
                gameBoards[board].update(game.getGameBoard(board));
            }
        }
    }

    private void initiate(){
        if(!isInitiated){
            isInitiated = true;
            areas = new GameBoard[]{area1, area2, area3, area4, area5, area6, area7, area8};

            YokelUtilities.setDebug(true, area1, area2, area3, area4, area5, area6, area7, area8);

            YokelTable table = sessionService.getCurrentTable();
            game = new GameManager(table);
            setUpGameArea(table);
            toggleGameStart();
            game.startGame();
        }
    }


    private void setUpGameArea(YokelTable table){
        //If table does not exist, keep it moving
        if(table == null) return;
        int playerSeat = sessionService.getCurrentSeat();

        //Set up Player View
        setUpPlayerArea(sessionService.getCurrentPlayer(), playerSeat,true);

        //Set up Partner View
        int partnerSeat = getPlayerPartnerSeatNum(playerSeat);
        YokelPlayer partner = getSeatedPlayer(partnerSeat, table);
        setUpPlayerArea(partner, partnerSeat,false);

        //Set up rest of active players
        Array<Integer> remaining = GdxArrays.newArray();
        for(int i = 2; i < gameBoards.length; i++) {
            remaining.add(i);
        }

        Iterator<Integer> iterator = remaining.iterator();

        for(int i = 0; i < gameBoards.length; i++){
            if(i != playerSeat && i != partnerSeat){
                YokelSeat seat = table.getSeat(i);
                if(seat != null && iterator.hasNext()){
                    activateArea(i, iterator.next(), seat.isOccupied(), seat.getSeatedPlayer(), playerSeat, false, true);
                }
            }
        }
    }

    private int getPlayerPartnerSeatNum(int playerSeat) {
        if (playerSeat % 2 == 0) {
            return playerSeat + 1;
        } else {
            return playerSeat - 1;
        }
    }

    private YokelPlayer getSeatedPlayer(int seatNum, YokelTable table){
        if(table != null){
            YokelSeat seat = table.getSeat(seatNum);
            if(seat != null){
                return seat.getSeatedPlayer();
            }
        }
        return null;
    }

    private void setUpPlayerArea(YokelPlayer player, int playerSeat, boolean isPlayerView){
        //Even seats are on the left
        activateArea(playerSeat, playerSeat % 2, true, player, playerSeat, isPlayerView, false);
    }

    private void activateArea(int playerSeat, int areaNum, boolean isActive, YokelPlayer player, int boardIndex, boolean isPlayerView, boolean isPreview){
        gameBoards[playerSeat] = areas[areaNum];
        GameBoard area = gameBoards[playerSeat];

        if(area != null){
            YokelGameBoard board = game.getGameBoard(boardIndex);
            if(player != null){
                area.setPlayerLabel(player.getNameLabel().toString());
                area.setPlayerView(isPlayerView);
                area.setActive(isActive);
            }
            area.setPreview(isPreview);
            area.update(board);
        }
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

    private void handlePlayerInput(GameManager game){
        logger.debug("Enter handlePlayerInput()");
        if(!isGameOver) {
            sessionService.handleLocalPlayerInput(game);
        }
        logger.debug("Exit handlePlayerInput()");
    }
}