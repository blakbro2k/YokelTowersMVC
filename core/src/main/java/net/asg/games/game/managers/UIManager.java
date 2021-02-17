package net.asg.games.game.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.controller.UITestController;
import net.asg.games.game.objects.YokelBlockMove;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;
import net.asg.games.utils.YokelUtilities;

import java.util.Iterator;
import java.util.Vector;

public class UIManager implements Disposable {
    // Getting a utility logger:
    private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(UITestController.class);

    private static final int GAME_BOARDS_LENGTH = 8;
    private GameBoard[] gameBoards = new GameBoard[GAME_BOARDS_LENGTH];
    private GameBoard[] areas;
    private boolean useServer = true;
    private boolean isCellsDropping = false;
    private GameManager serverGameManger;
    private UIManagerUserConfiguration config;

    public UIManager(GameBoard[] areas, boolean useServer, UIManagerUserConfiguration config) {
        logger.enter("UIManager");
        if(areas == null || areas.length != GAME_BOARDS_LENGTH) throw new GdxRuntimeException("Cannot initialize UIManager, gameboards are of wrong size.");
        setUseServer(useServer);
        this.areas = areas;
        init(config);
        logger.exit("UIManager");
    }

    /*public UIManager(GameBoard[] areas){
        this(areas, false);
    }*/

    public void setDebug(boolean isDebug){
        for(GameBoard gameboard : gameBoards){
            if(gameboard != null){
                YokelUtilities.setDebug(isDebug, gameboard);
            }
        }
        for(GameBoard area : areas){
            if(area != null){
                YokelUtilities.setDebug(isDebug, area);
            }
        }
    }

    private void init(UIManagerUserConfiguration config){
        logger.debug("Enter init()");
        this.config = config;

        //Using Live Server to test
        if(useServer) {
            //Get GameManger from server
            //Get Table from GameManager
            //setUpGameArea(table);

        } else {
            if(config == null) {
                logger.error("Must set configuration to use non-server");
                throw new GdxRuntimeException("Must set configuration to use non-server");
            }

            YokelTable table = new YokelTable(config.getTableNumber());

            for(int i = 0; i < 8; i++) {
                YokelPlayer seatedPlayer = config.getSeat(i);
                if(seatedPlayer != null) {
                    YokelSeat seat = table.getSeat(i);
                    seat.sitDown(seatedPlayer);
                }
            }

            serverGameManger = new GameManager(table);
            setUpGameArea(table);
        }

        if(serverGameManger != null){
            //serverGameManger.startGame();
        } else {
            logger.error("Unable to start GameManger!");
        }

        logger.debug("Exit init()");
    }

    public GameManager getSimulatedGameManager(){
        return serverGameManger;
    }

    public void setUseServer(boolean useServer) {
        this.useServer = useServer;
    }

    public boolean isUsingServer() {
        return useServer;
    }

    public void startSimulatedGame() {
        logger.debug("Starting simulated Game()");
        //serverGameManger.showGameBoard(6);
        //serverGameManger.showGameBoard(1);
        //logger.error();
        serverGameManger.startGame();
    }

    public void stopSimulatedGame() {
        logger.debug("Stopping simulated Game()");
        serverGameManger.stopGame();
    }

    @Override
    public void dispose() {

    }

    public static class UIManagerUserConfiguration {
        private YokelPlayer seat1;
        private YokelPlayer seat2;
        private YokelPlayer seat3;
        private YokelPlayer seat4;
        private YokelPlayer seat5;
        private YokelPlayer seat6;
        private YokelPlayer seat7;
        private YokelPlayer seat8;
        private YokelPlayer currentPlayer;
        private int tableNumber = -1;
        private int currentSeat = -1;

        public UIManagerUserConfiguration() {
        }

        public void setSeat(int seatNumber, YokelPlayer player){
            switch (seatNumber) {
                case 0:
                    seat1 = player;
                    break;
                case 1:
                    seat2 = player;
                    break;
                case 2:
                    seat3 = player;
                    break;
                case 3:
                    seat4 = player;
                    break;
                case 4:
                    seat5 = player;
                    break;
                case 5:
                    seat6 = player;
                    break;
                case 6:
                    seat7 = player;
                    break;
                case 7:
                    seat8 = player;
                    break;
                default:
                    //throw new GdxRuntimeException("invalid seat number.");
            }
        }

        public YokelPlayer getSeat(int seatNumber) {
            switch (seatNumber) {
                case 0:
                    return seat1;
                case 1:
                    return seat2;
                case 2:
                    return seat3;
                case 3:
                    return seat4;
                case 4:
                    return seat5;
                case 5:
                    return seat6;
                case 6:
                    return seat7;
                case 7:
                    return seat8;
                default:
                    return null;
            }
        }

        public int getTableNumber() {
            return tableNumber;
        }

        public void setTableNumber(int i){
            this.tableNumber = i;
        }

        public void setCurrentPlayer(YokelPlayer player1) {
            currentPlayer = player1;
        }

        public YokelPlayer getCurrentPlayer() {
            return currentPlayer;
        }

        public void setCurrentSeat(int currentSeatNumber) {
            currentSeat = currentSeatNumber;
        }

        public int getCurrentSeat() {
            return currentSeat;
        }
    }

    //Needs to update the simulated game with the server state.
    public void updateGameBoards(GameManager game, float delta) {
        logger.debug("Enter updateGameBoards()");
        if(game != null){
            updateState(game);
            serverGameManger.update(delta);

            for(int board = 0; board < gameBoards.length; board++){
                if(game.isPlayerDead(board)){
                    //logger.debug("board={0}", gameBoards[board]);
                    logger.debug("is board[{}] dead: {}", board, game.isPlayerDead(board));
                    gameBoards[board].killPlayer();
                } else {
                    logger.debug("Updating board[{}]", board);
                    logger.debug("Current seat[{}]", config.getCurrentSeat());
                    gameBoards[board].update(game.getGameBoard(board));
                }
            }
        }
        logger.debug("Exit updateGameBoards()");
    }

    private void updateState(GameManager game) {
        //TODO: Need a collection of GameManagers and update current one based off
        if(game != null){
            serverGameManger = game;
        }
    }

    private void setUpGameArea(YokelTable table){
        logger.enter("setUpGameArea");

        //If table does not exist, keep it moving
        if(table == null) return;
        int playerSeat = config.getCurrentSeat();
        YokelPlayer currentPlayer = config.getCurrentPlayer();

        if(playerSeat < 0){
            logger.debug("No Player seated, setting player seat to 1");
            playerSeat = 0;
            //currentPlayer = null;
        }

        logger.debug("playerSeat={}", playerSeat);
        logger.debug("currentPlayer={}", currentPlayer);

        //Set up Player View with
        setUpPlayerArea(currentPlayer, playerSeat,true);

        //Set up Partner View
        int partnerSeat = getPlayerPartnerSeatNum(playerSeat);
        YokelPlayer partner = getSeatedPlayer(partnerSeat, table);

        logger.debug("partnerSeat={}", partnerSeat);
        logger.debug("partner={}", partner);
        setUpPlayerArea(partner, partnerSeat,false);

        //Set up rest of active players+
        Array<Integer> remaining = GdxArrays.newArray();
        for(int i = 2; i < gameBoards.length; i++) {
            remaining.add(i);
        }

        Iterator<Integer> iterator = remaining.iterator();

        for(int i = 0; i < gameBoards.length; i++){
            if(i != playerSeat && i != partnerSeat){
                YokelSeat seat = table.getSeat(i);
                if(seat != null && iterator.hasNext()){
                    activateArea(i, iterator.next(), seat.isOccupied(), seat.getSeatedPlayer(), false, true);
                }
            }
        }
        logger.exit("setUpGameArea");
        //throw new GdxRuntimeException("done");
    }

    private int getPlayerPartnerSeatNum(int playerSeat) {
        if (playerSeat % 2 == 0) {
            return playerSeat + 1;
        } else {
            return playerSeat - 1;
        }
    }

    private YokelPlayer getSeatedPlayer(int seatNum, YokelTable table){
        logger.debug("Enter getSeatedPlayer()");

        if(table != null){
            YokelSeat seat = table.getSeat(seatNum);
            if(seat != null){
                logger.debug("seat={0}", seat);
                logger.debug("Exit getSeatedPlayer()");
                return seat.getSeatedPlayer();
            }
        }
        logger.debug("Exit getSeatedPlayer()");
        return null;
    }

    private void setUpPlayerArea(YokelPlayer player, int playerSeat, boolean isPlayerView){
        logger.enter("setUpPlayerArea");
        //Even seats are on the left
        activateArea(playerSeat, playerSeat % 2, true, player, isPlayerView, false);
        logger.exit("setUpPlayerArea");
    }

    private void activateArea(int playerSeat, int areaNum, boolean isActive, YokelPlayer player, boolean isPlayerView, boolean isPreview){
        logger.debug("Enter activateArea()");

        gameBoards[playerSeat] = areas[areaNum];
        GameBoard area = gameBoards[playerSeat];

        logger.debug("areaNum={0}", areaNum);
        //logger.debug("areas[areaNum]=\n{0}", areas[areaNum]);
        //logger.debug("area=\n{0}", area);

        if(area != null){
            YokelGameBoard board = serverGameManger.getGameBoard(playerSeat);
            //logger.debug("board=\n{0}", board);

            if(board != null){
                area.setPreview(isPreview);

                if(player != null){
                    area.setPlayerLabel(player.getNameLabel().toString());
                    area.setPlayerView(isPlayerView);
                    area.setActive(isActive);
                }
                //logger.debug("area\n={0}", area);
                area.update(board);
            }
        }
        logger.debug("Exit activateArea()");
    }
}
