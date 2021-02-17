package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewController;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewInitializer;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.controller.dialog.NextGameController;
import net.asg.games.game.managers.GameManager;
import net.asg.games.game.managers.UIManager;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GameOverText;
import net.asg.games.service.SessionService;
import net.asg.games.service.UserInterfaceService;
import net.asg.games.utils.GlobalConstants;
import net.asg.games.utils.Log4LibGDXLoggerService;
import net.asg.games.utils.Log4LibGDXLogger;

@View(id = GlobalConstants.UI_TEST_VIEW, value = GlobalConstants.UI_TEST_VIEW_PATH)
public class UITestController extends ApplicationAdapter implements ViewRenderer, ViewInitializer, ActionContainer {
    // Getting a utility logger:
    private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(UITestController.class);

    @Inject private UserInterfaceService uiService;
    @Inject private SessionService sessionService;
    @Inject private MusicService musicService;
    @Inject private LoggerService loggerService;
    @Inject private InterfaceService interfaceService;

    @LmlActor("gameClock") private GameClock gameClock;
    @LmlActor("1:area") private GameBoard area1;
    @LmlActor("2:area") private GameBoard area2;
    @LmlActor("3:area") private GameBoard area3;
    @LmlActor("4:area") private GameBoard area4;
    @LmlActor("5:area") private GameBoard area5;
    @LmlActor("6:area") private GameBoard area6;
    @LmlActor("7:area") private GameBoard area7;
    @LmlActor("8:area") private GameBoard area8;

    private boolean isGameOver, nextGameDialog, attemptGameStart = false;
    private long nextGame = 0;

    private UIManager uiManager;

    @Override
    public void initialize(Stage stage, ObjectMap<String, Actor> actorMappedByIds) {
        logger.debug("initializing viewID: {}", GlobalConstants.UI_TEST_VIEW);
        /*
        try {
            sessionService.asyncGameManagerFromServerRequest();
        } catch (InterruptedException e) {
            e.printStackTrace();
            sessionService.showError(e);
        }*/
        initialize();
    }

    @Override
    public void destroy(ViewController viewController) {
        logger.debug("destroying viewID: {}", GlobalConstants.UI_TEST_VIEW);
    }

    @Override
    public void render(Stage stage, float delta) {
        //logger.enter("render");

        try{
            //Fetch GameManager from Server
            GameManager game = fetchGameManagerFromServer();

            checkGameStart();

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
        //logger.exit("render");
    }

    private void checkGameStart() {
        if(gameClock == null) return;

        if(attemptGameStart){
            if(!nextGameDialog) {
                logger.debug("Showing next Game Dialog");
                nextGameDialog = true;
                interfaceService.showDialog(NextGameController.class);
                nextGame = TimeUtils.millis();
            }

            if(getElapsedSeconds() > NextGameController.NEXT_GAME_SECONDS){
                interfaceService.destroyDialog(NextGameController.class);
                attemptGameStart = false;
                startGame();
            }
        }
    }

    private int getElapsedSeconds(){
        return (int) ((TimeUtils.millis() - nextGame) / 1000);
    }

    private void startGame(){
        if(!gameClock.isRunning()) {
            logger.debug("Starting clock now");
            gameClock.start();
        }
    }

    private void stopGame(){
        if(gameClock.isRunning()) {
            logger.debug("Starting clock now");
            gameClock.stop();
        }
    }
    private void showGameOver(Stage stage, GameManager game){
        logger.enter("showGameOver");

        if(game != null && stage != null){
            if(game.showGameOver()){
                toggleGameStart();
                stage.addActor(getGameOverActor(game));
            }
        }
        logger.exit("showGameOver");
    }

    private Actor getGameOverActor(GameManager game) {
        logger.enter("getGameOverActor");
        if(game != null){
            Array<YokelPlayer> winners = game.getWinners();
            YokelPlayer player1 = getPlayer(winners, 0);
            YokelPlayer player2 = getPlayer(winners, 1);
            logger.debug("winners={}", winners);
            logger.debug("player1={}", player1);
            logger.debug("player2={}", player2);

            GameOverText gameOverText = new GameOverText(sessionService.getCurrentPlayer().equals(player1) || sessionService.getCurrentPlayer().equals(player2), player1, player2, uiService.getSkin());
            gameOverText.setPosition(uiService.getStage().getWidth() / 2, uiService.getStage().getHeight() / 2);
            return gameOverText;
        }
        logger.exit("getGameOverActor");
        return new GameOverText(false, sessionService.getCurrentPlayer(), sessionService.getCurrentPlayer(), uiService.getSkin());
    }

    private YokelPlayer getPlayer(Array<YokelPlayer> players, int index){
        if(logger.isDebugOn()){
            ObjectMap<String, Object> map = GdxMaps.newObjectMap();
            map.put("players", players);
            map.put("index", index);
            logger.enter("getPlayer", map);
        }

        if(players != null && players.size > 0 && index != players.size){
            logger.debug("player={}", players.get(index));
            if(index < players.size) return players.get(index);
        }

        logger.exit("getPlayer");
        return null;
    }

    private void initialize(){
        logger.enter("initialize");

        boolean isUsingServer = false;
        UIManager.UIManagerUserConfiguration config = new UIManager.UIManagerUserConfiguration();

        if(!isUsingServer){
            //UI Configuration manager needs to handle these.
            YokelPlayer player1 = new YokelPlayer("enboateng");
            YokelPlayer player2 = new YokelPlayer("lholtham", 1400, 5);
            YokelPlayer player3 = new YokelPlayer("rmeyers", 1700, 7);

            //setTable
            config.setTableNumber(1);
            int currentSeatNumber = 3;

            //this needs to be set outside of the UI manager
            sessionService.setCurrentPlayer(player1);
            sessionService.setCurrentLoungeName("Social");
            sessionService.setCurrentRoomName("Eiffel Tower");
            sessionService.setCurrentSeat(currentSeatNumber);

            config.setSeat(currentSeatNumber, player1);
            //config.setSeat(7, player3);
            config.setSeat(1, player2);
            config.setCurrentPlayer(sessionService.getCurrentPlayer());
            config.setCurrentSeat(sessionService.getCurrentSeat());
        } else {
            //TODO: Fetch table state from server
            isUsingServer = true;
        }
        uiManager = new UIManager(new GameBoard[]{area1, area2, area3, area4, area5, area6, area7, area8}, isUsingServer, config);
        toggleGameStart();

        logger.exit("initialize");
    }


    @LmlAction("toggleGameStart")
    private void toggleGameStart() {
        logger.enter("toggleGameStart");
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
        logger.exit("toggleGameStart");
    }

    private void handlePlayerInput() throws InterruptedException {
        logger.debug("handlePlayerInput");
        if(!isGameOver) {
            sessionService.handleLocalPlayerInput(uiManager.getSimulatedGameManager());
            if(uiManager.isUsingServer()){
                sessionService.handlePlayerInputToServer();
            }
        }
        logger.exit("handlePlayerInput");
    }

    private GameManager fetchGameManagerFromServer() throws InterruptedException {
        logger.enter("fetchGameManagerFromServer");

        if(uiManager.isUsingServer()) {
            sessionService.asyncGameManagerFromServerRequest();

            //TODO: Check if received new GameManager, return current simulation if null.
            logger.exit("fetchGameManagerFromServer");
            return sessionService.asyncGetGameManagerFromServerRequest();
        } else {
            logger.exit("fetchGameManagerFromServer");
            return uiManager.getSimulatedGameManager();
        }
    }
}