package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelBlockMove;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GameOverText;
import net.asg.games.service.SessionService;
import net.asg.games.service.UserInterfaceService;

import java.util.Iterator;
import java.util.Vector;

@View(id = ControllerNames.UI_TEST_VIEW, value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    @Inject private UserInterfaceService uiService;
    @Inject private SessionService sessionService;
    @Inject private MusicService musicService;

    @LmlActor("gameClock") private GameClock gameClock;
    @LmlActor("1:area") private GameBoard area1;
    @LmlActor("2:area") private GameBoard area2;
    @LmlActor("3:area") private GameBoard area3;
    @LmlActor("4:area") private GameBoard area4;
    @LmlActor("5:area") private GameBoard area5;
    @LmlActor("6:area") private GameBoard area6;
    @LmlActor("7:area") private GameBoard area7;
    @LmlActor("8:area") private GameBoard area8;

    private boolean isInitiated;
    private boolean isGameOver = false;
    private boolean isCellsDropping = false;

    private GameManager game;
    private GameBoard[] gameBoards = new GameBoard[8];
    private GameBoard[] areas;

    @Override
    public void render(Stage stage, float delta) {
        initiate();
        checkForInput();

        //Simulation: fetch game from server.
        //if live, an updated gameManager will be fetched
        game.update(delta);
        //End Simulation:

        updateGameBoards();
        /*
        if(game.showGameOver()){
            toggleGameStart();
            stage.addActor(getGameOverActor());
        }*/
        stage.act(delta);
        stage.draw();
    }

    private void animateBrokenCells(GameBoard gameBoard, Vector<YokelBlockMove> cellsToDrop, int board) {
        if(cellsToDrop != null && cellsToDrop.size() > 0){
            isCellsDropping = true;
            gameBoard.addBlocksToDrop(cellsToDrop);
        }

        if(gameBoard.isActionFinished()){
            isCellsDropping = false;
        }

        if(!isCellsDropping){
            handleBrokenCells(board);
        }
    }

    private Actor getGameOverActor() {
        Array<YokelPlayer> winners = game.getWinners();
        YokelPlayer player1 = getPlayer(winners, 0);
        YokelPlayer player2 = getPlayer(winners, 1);

        GameOverText gameOverText = new GameOverText(sessionService.getCurrentPlayer().equals(player1) || sessionService.getCurrentPlayer().equals(player2), player1, player2, uiService.getSkin());
        gameOverText.setPosition(uiService.getStage().getWidth() / 2, uiService.getStage().getHeight() / 2);
        return gameOverText;
    }

    private YokelPlayer getPlayer(Array<YokelPlayer> players, int index){
        if(players != null && players.size > 0){
            if(index < players.size) return players.get(index);
        }
        return null;
    }

    private void updateGameBoards() {
        for(int board = 0; board < gameBoards.length; board++){
            gameBoards[board].update(game.getGameBoard(board));

            if(game.isPlayerDead(board)){
                gameBoards[board].killPlayer();
            } else {
                //Animate Cell drops if needed and is this
                if(sessionService.getCurrentSeat() == board){
                    animateBrokenCells(gameBoards[board], game.getCellsToDrop(board), board);
                } else {
                    handleBrokenCells(board);
                }
            }
        }
    }

    private void handleBrokenCells(int boardIndex) {
        game.handleBrokenCellDrops(boardIndex);
    }

    private void initiate(){
        if(!isInitiated){
            isInitiated = true;
            areas = new GameBoard[]{area1, area2, area3, area4, area5, area6, area7, area8};

            //YokelUtilities.setDebug(true, area1, area2, area3, area4, area5, area6, area7, area8);

            YokelPiece piece1 = new YokelPiece(1,32,84,112);
            YokelPiece piece2 = new YokelPiece(2,68,53,51);
            YokelPlayer player1 = new YokelPlayer("enboateng");
            YokelPlayer player2 = new YokelPlayer("lholtham", 1400,5);
            YokelPlayer player3 = new YokelPlayer("rmeyers", 1700,7);

            //System.out.println(sessionService.getView(ControllerNames.UI_TEST_VIEW));

            //{0,1}{2,3}{4,5}{6,7}
            int cSeat = 6;
            int pSeat = 5;
            int tSeat = 1;

            sessionService.setCurrentPlayer(player1);
            sessionService.setCurrentLoungeName("Social");
            sessionService.setCurrentRoomName("Eiffel Tower");
            sessionService.setCurrentSeat(cSeat);

            YokelTable table = new YokelTable(1);
            YokelSeat seat1 = table.getSeat(cSeat);
            YokelSeat seat2 = table.getSeat(pSeat);
            YokelSeat seat3 = table.getSeat(tSeat);

            seat1.sitDown(player1);
            //seat2.sitDown(player2);
            seat3.sitDown(player3);

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
            board.setName("BoardNumber: " + boardIndex);
            System.out.println("Setting up board: " + boardIndex);
            area.setPreview(isPreview);

            if(player != null){
                area.setPlayerLabel(player.getNameLabel().toString());
                area.setPlayerView(isPlayerView);
                area.setActive(isActive);
            }
            area.update(board);
        }
    }

    @LmlAction("toggleGameStart")
    private void toggleGameStart() {
        if(gameClock == null) return;
        if(!gameClock.isRunning()){
            isGameOver = false;
            gameClock.start();
        } else {
            isGameOver = true;
            gameClock.stop();
        }
    }

    private void checkForInput(){
        if(!isGameOver) {
            sessionService.checkPlayerInputMap(game);
        }
    }
}