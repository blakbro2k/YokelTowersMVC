package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewController;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewInitializer;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.managers.UIManager;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GameOverText;
import net.asg.games.service.SessionService;
import net.asg.games.service.UserInterfaceService;

@View(id = ControllerNames.UI_TEST_VIEW, value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ViewInitializer, ActionContainer {
    // Getting a utility logger:
    private Logger logger = LoggerService.forClass(UITestController.class);

    @Inject private UserInterfaceService uiService;
    @Inject private SessionService sessionService;
    @Inject private MusicService musicService;
    @Inject private LoggerService loggerService;

    @LmlActor("gameClock") private GameClock gameClock;
    @LmlActor("1:area") private GameBoard area1;
    @LmlActor("2:area") private GameBoard area2;
    @LmlActor("3:area") private GameBoard area3;
    @LmlActor("4:area") private GameBoard area4;
    @LmlActor("5:area") private GameBoard area5;
    @LmlActor("6:area") private GameBoard area6;
    @LmlActor("7:area") private GameBoard area7;
    @LmlActor("8:area") private GameBoard area8;

    private boolean isGameOver = false;

    private UIManager uiManager;

    @Override
    public void initialize(Stage stage, ObjectMap<String, Actor> actorMappedByIds) {
        logger.debug("initializing viewID: " + ControllerNames.UI_TEST_VIEW);
        initialize();
    }

    @Override
    public void destroy(ViewController viewController) {
        logger.debug("destroying viewID: " + ControllerNames.UI_TEST_VIEW);
    }

    @Override
    public void render(Stage stage, float delta) {
        try{
            //Fetch GameManager from Server
            GameManager game = fetchGameManagerFromServer();

            //Handle Player input
            handlePlayerInput();

            //Update UI base on Game State
            uiManager.updateGameBoards(game, delta);

            //If Game Over, show it
            showGameOver(stage, game);

            stage.act(delta);
            stage.draw();
        } catch (Exception e){
            e.printStackTrace();
            sessionService.showError(e);
        }
    }


    private void showGameOver(Stage stage, GameManager game){
        if(game != null && stage != null){
            if(game.showGameOver()){
                toggleGameStart();
                stage.addActor(getGameOverActor(game));
            }
        }
    }

    private Actor getGameOverActor(GameManager game) {
        logger.debug("Enter getGameOverActor()");

        if(game != null){
            Array<YokelPlayer> winners = game.getWinners();
            YokelPlayer player1 = getPlayer(winners, 0);
            YokelPlayer player2 = getPlayer(winners, 1);
            logger.debug("winners={0}", winners);
            logger.debug("player1={0}", player1);
            logger.debug("player2={0}", player2);

            GameOverText gameOverText = new GameOverText(sessionService.getCurrentPlayer().equals(player1) || sessionService.getCurrentPlayer().equals(player2), player1, player2, uiService.getSkin());
            gameOverText.setPosition(uiService.getStage().getWidth() / 2, uiService.getStage().getHeight() / 2);
            return gameOverText;
        }
        logger.debug("Exit getGameOverActor()");
        return new GameOverText(false, sessionService.getCurrentPlayer(), sessionService.getCurrentPlayer(), uiService.getSkin());
    }

    private YokelPlayer getPlayer(Array<YokelPlayer> players, int index){
        logger.debug("Enter getPlayer(players={0}, index={1})", players, index);

        if(players != null && players.size > 0 && index != players.size){
            logger.debug("player={0}", players.get(index));
            if(index < players.size) return players.get(index);
        }

        logger.debug("Exit getPlayer()");
        return null;
    }

    private void initialize(){
        logger.debug("Enter initiate()");

        boolean isUsingServer = false;
        UIManager.UIManagerUserConfiguration config = new UIManager.UIManagerUserConfiguration();

        if(!isUsingServer){
            //UI Configuration manager needs to handle these.
            YokelPlayer player1 = new YokelPlayer("enboateng");
            YokelPlayer player2 = new YokelPlayer("lholtham", 1400, 5);
            YokelPlayer player3 = new YokelPlayer("rmeyers", 1700, 7);

            //setTable
            config.setTableNumber(1);

            //this needs to be set outside of the UI manager
            sessionService.setCurrentPlayer(player1);
            sessionService.setCurrentLoungeName("Social");
            sessionService.setCurrentRoomName("Eiffel Tower");
            int currentSeatNumber = 6;
            sessionService.setCurrentSeat(currentSeatNumber);

            config.setSeat(6, player1);
            config.setSeat(1, player3);
            config.setCurrentPlayer(player1);
            config.setCurrentSeat(currentSeatNumber);
        } else {
            //TODO: Fetch table state from server
            isUsingServer = true;
        }
        uiManager = new UIManager(new GameBoard[]{area1, area2, area3, area4, area5, area6, area7, area8}, isUsingServer, config);
        toggleGameStart();

        logger.debug("Exit initiate()");
    }


    @LmlAction("toggleGameStart")
    private void toggleGameStart() {
        logger.debug("Entering toggleGameStart()");
        if(gameClock == null) return;

        //fetch
        if(!gameClock.isRunning()){
            isGameOver = false;
            uiManager.startSimulatedGame();
            gameClock.start();
        } else {
            isGameOver = true;
            uiManager.stopSimulatedGame();
            gameClock.stop();
        }
        logger.debug("Exiting toggleGameStart()");
    }

    private void handlePlayerInput() throws InterruptedException {
        logger.debug("Enter handlePlayerInput()");
        if(!isGameOver) {
            sessionService.handleLocalPlayerInput(uiManager.getSimulatedGameManager());
            if(uiManager.isUsingServer()){
                sessionService.handlePlayerInputToServer();
            }
        }
        logger.debug("Exit handlePlayerInput()");
    }

    private GameManager fetchGameManagerFromServer() {
        if(uiManager.isUsingServer()) {
            //TODO: Check if received new GameManager, return current simulation if null.
            return sessionService.asyncGetGameManagerFromServerRequest();
        } else {
            return uiManager.getSimulatedGameManager();
        }
    }
}