package net.asg.games.game.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.game.objects.YokelBlockMove;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.service.SessionService;
import net.asg.games.utils.YokelUtilities;

import java.util.Iterator;
import java.util.Vector;

public class UIManager implements Disposable {
    // Getting a utility logger:
    private Logger logger = LoggerService.forClass(UIManager.class);

    private static final int GAME_BOARDS_LENGTH = 8;
    private GameBoard[] gameBoards = new GameBoard[GAME_BOARDS_LENGTH];
    private GameBoard[] areas;
    private boolean useServer = true;
    private boolean isCellsDropping = false;
    private GameManager serverGameManger;
    private UIManagerUserConfiguration config;

    public UIManager(GameBoard[] areas, boolean useServer, UIManagerUserConfiguration config) {
        logger.debug("Enter UIManager()");
        if(areas == null || areas.length != GAME_BOARDS_LENGTH) throw new GdxRuntimeException("Cannot initialize UIManager, gameboards are of wrong size.");
        setUseServer(useServer);
        this.areas = areas;
        init(config);
        logger.debug("Exit UIManager()");
    }

    /*public UIManager(GameBoard[] areas){
        this(areas, false);
    }*/

    public void setDebug(boolean isDebug){
        //YokelUtilities.setDebug(isDebug, area1, area2, area3, area4, area5, area6, area7, area8);
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

            for(int i = 1; i < 9; i++) {
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

    public boolean isCellsDropping() {
        return isCellsDropping;
    }

    public void handleCellDrops() {
        logger.debug("handling drops for seat: " + config.getCurrentSeat());
        handleBrokenCells(config.getCurrentSeat(), serverGameManger);
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
        private int tableNumber;
        private int currentSeat;

        public UIManagerUserConfiguration() {
        }

        public void setSeat(int seatNumber, YokelPlayer player){
            switch (seatNumber) {
                case 1:
                    seat1 = player;
                    break;
                case 2:
                    seat2 = player;
                    break;
                case 3:
                    seat3 = player;
                    break;
                case 4:
                    seat4 = player;
                    break;
                case 5:
                    seat5 = player;
                    break;
                case 6:
                    seat6 = player;
                    break;
                case 7:
                    seat7 = player;
                    break;
                case 8:
                    seat8 = player;
                    break;
                default:
                    throw new GdxRuntimeException("invalid seat number.");
            }
        }

        public YokelPlayer getSeat(int seatNumber) {
            switch (seatNumber) {
                case 1:
                    return seat1;
                case 2:
                    return seat2;
                case 3:
                    return seat3;
                case 4:
                    return seat4;
                case 5:
                    return seat5;
                case 6:
                    return seat6;
                case 7:
                    return seat7;
                case 8:
                    return seat8;
                default:
                    throw new GdxRuntimeException("invalid seat number.");
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
                    logger.debug("is board[{0}] dead: {1}", board, game.isPlayerDead(board));
                    gameBoards[board].killPlayer();
                } else {
                    logger.debug("Updating board[{0}]", board);
                    logger.debug("Current seat[{0}]", config.getCurrentSeat());
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

    private void handleBrokenCells(int boardIndex, GameManager game) {
        logger.debug("Enter handleBrokenCells()");
        if(game != null){
            game.handleBrokenCellDrops(boardIndex);
        }
        logger.debug("Exit handleBrokenCells()");
    }

    private void animateBrokenCells(GameBoard gameBoard, int board, GameManager game) {
        logger.debug("Enter animateBrokenCells(boardNum="+ board+")");

        if(!isCellsDropping){
            Vector<YokelBlockMove> cellsToDrop = game.getCellsToDrop(board);
            if(!YokelUtilities.isEmpty(cellsToDrop)){
                logger.debug("game drops=" + cellsToDrop);
                isCellsDropping = true;
                logger.debug("Starting to animate drop cells.");
                logger.debug("isCellsDropping={0}", isCellsDropping);
                gameBoard.addBlocksToDrop(cellsToDrop);
            }
        }

        if(gameBoard.isActionFinished()){
            isCellsDropping = false;
            logger.debug("ending animate drop cells.");
            logger.debug("isCellsDropping={0}", isCellsDropping);
        }
        logger.debug("Exit animateBrokenCells()");
    }

    private void setUpGameArea(YokelTable table){
        logger.debug("Enter setUpGameArea()");

        //If table does not exist, keep it moving
        if(table == null) return;
        int playerSeat = config.getCurrentSeat();
        logger.debug("playerSeat={0}", playerSeat);

        //Set up Player View
        setUpPlayerArea(config.getCurrentPlayer(), playerSeat,true);

        //Set up Partner View
        int partnerSeat = getPlayerPartnerSeatNum(playerSeat);
        YokelPlayer partner = getSeatedPlayer(partnerSeat, table);

        logger.debug("partnerSeat={0}", partnerSeat);
        logger.debug("partner={0}", partner);
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
        logger.debug("Exit setUpGameArea()");
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
        logger.debug("Enter setUpPlayerArea()");

        //Even seats are on the left
        activateArea(playerSeat, playerSeat % 2, true, player, isPlayerView, false);
        logger.debug("Exit setUpPlayerArea()");
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
