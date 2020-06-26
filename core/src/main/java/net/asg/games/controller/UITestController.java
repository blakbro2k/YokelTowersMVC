package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.PlayerKeyMap;
import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GamePowersQueue;
import net.asg.games.service.SessionService;
import net.asg.games.service.UserInterfaceService;
import net.asg.games.utils.YokelUtilities;

import java.util.Iterator;

@View(id = ControllerNames.UI_TEST_VIEW, value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
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
    @LmlActor("1:area") private GameBoard area1;
    @LmlActor("2:area") private GameBoard area2;
    @LmlActor("3:area") private GameBoard area3;
    @LmlActor("4:area") private GameBoard area4;
    @LmlActor("5:area") private GameBoard area5;
    @LmlActor("6:area") private GameBoard area6;
    @LmlActor("7:area") private GameBoard area7;
    @LmlActor("8:area") private GameBoard area8;
    @LmlActor("9:area") private GameBlockArea area9;
    @LmlActor("1:next") private GameBoard next1;
    @LmlActor("2:next") private GameBoard next2;
    @LmlActor("1:powers") private GamePowersQueue powersQueue1;
    @LmlActor("2:powers") private GamePowersQueue powersQueue2;
    @LmlActor("gameblock_1") private GameBlock gameblock_1;

    private boolean isInitiated;
    private PlayerKeyMap keyMap = new PlayerKeyMap();
    private GameManager game;
    private GameBoard[] gameBoards = new GameBoard[8];
    private GameBoard[] areas;

    @Override
    public void render(Stage stage, float delta) {
        initiate(stage);
        checkForInput();
        game.update(delta);
        updateGameBoards();

        stage.act(delta);
        stage.draw();
    }

    private void updateGameBoards() {
        for(int board = 0; board < gameBoards.length; board++){
            gameBoards[board].update(game.getGameBoard(board));
            if(game.isPlayerDead(board)){
                gameBoards[board].killPlayer();
            }
        }
    }

    private void initiate(Stage stage){
        if(!isInitiated){
            isInitiated = true;
            //initiateActors();
            areas = new GameBoard[]{area1, area2, area3, area4, area5, area6, area7, area8};

            YokelUtilities.setDebug(true, area1, area2, area3, area4, area5, area6, area7, area8);

            YokelPiece piece1 = new YokelPiece(1,32,84,112);
            YokelPiece piece2 = new YokelPiece(2,68,53,51);
            YokelPlayer player1 = new YokelPlayer("enboateng");
            YokelPlayer player2 = new YokelPlayer("lholtham", 1400,5);
            YokelPlayer player3 = new YokelPlayer("rmeyers", 1700,7);

            System.out.println(sessionService.getView(ControllerNames.UI_TEST_VIEW));

            //{0,1}{2,3}{4,5}{6,7}
            int cSeat = 6;
            int pSeat = 7;
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
            seat2.sitDown(player2);
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
            if(player != null){
                area.setPlayerLabel(player.getNameLabel().toString());
                area.setPlayerView(isPlayerView);
                area.setActive(isActive);
            }
            area.setPreview(isPreview);
            area.update(board);
        }
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

        board.setCell(3,0, 96);
        board.setCell(3,1, getRandomBlockId());
        board.setCell(3,2, getRandomBlockId());
        board.setCell(3,3, getRandomBlockId());
        board.setCell(3,4, getRandomBlockId());
        board.setCell(3,5, 112);

        board.setCell(4,0, 112);
        board.setCell(4,1, getRandomBlockId());
        board.setCell(4,2, getRandomBlockId());
        board.setCell(4,3, getRandomBlockId());
        board.setCell(4,4, getRandomBlockId());
        board.setCell(4,5, 83);

        board.setCell(5,0, getRandomBlockId());
        board.setCell(5,1, getRandomBlockId());
        board.setCell(5,2, getRandomBlockId());
        board.setCell(5,3, getRandomBlockId());
        board.setCell(5,4, getRandomBlockId());
        board.setCell(5,5, getRandomBlockId());

        board.setCell(6,0, getRandomBlockId());
        board.setCell(6,1, getRandomBlockId());
        board.setCell(6,2, getRandomBlockId());
        board.setCell(6,3, getRandomBlockId());
        board.setCell(6,4, getRandomBlockId());
        board.setCell(6,5, getRandomBlockId());

        board.setCell(7,0, getRandomBlockId());
        board.setCell(7,1, getRandomBlockId());
        board.setCell(7,2, getRandomBlockId());
        board.setCell(7,3, getRandomBlockId());
        board.setCell(7,4, getRandomBlockId());
        board.setCell(7,5, 112);

        board.setCell(8,0, getRandomBlockId());
        board.setCell(8,1, getRandomBlockId());
        board.setCell(8,2, getRandomBlockId());
        board.setCell(8,3, getRandomBlockId());
        board.setCell(8,4, getRandomBlockId());
        board.setCell(8,5, getRandomBlockId());

        board.setCell(9,0, getRandomBlockId());
        board.setCell(9,1, getRandomBlockId());
        board.setCell(9,2, getRandomBlockId());
        board.setCell(9,3, getRandomBlockId());
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
        board.setCell(12,4, getRandomBlockId());
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

    private void checkForInput(){
        int currentSeat = sessionService.getCurrentSeat();
        if (Gdx.input.isKeyJustPressed(keyMap.getRightKey())) {
            game.handleMoveRight(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getLeftKey())) {
            game.handleMoveLeft(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getCycleDownKey())) {
            game.handleCycleDown(currentSeat);
        }
        if (Gdx.input.isKeyPressed(keyMap.getDownKey())) {
            game.handleStartMoveDown(currentSeat);
        }
        if (!Gdx.input.isKeyPressed(keyMap.getDownKey())) {
            game.handleStopMoveDown(currentSeat);
        }
        //System.out.println("key pressed:" + Gdx.input.isKeyPressed(Input.Keys.LEFT));
    }
}