package net.asg.games.game.managers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.service.SessionService;
import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;
import net.asg.games.utils.YokelUtilities;

import java.util.Iterator;

public class UIManager implements Disposable {
    // Getting a utility logger:
    private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(UIManager.class);

    private static final int GAME_BOARDS_LENGTH = 8;
    private GameBoard[] gameBoards = new GameBoard[GAME_BOARDS_LENGTH];
    private GameBoard[] uiAreas;
    private boolean useServer = true;
    private boolean isCellsDropping = false;
    private GameManager serverGameManger;
    //Is a representation of the session
    private UIManagerUserConfiguration config;

    public UIManager(GameBoard[] uiGameAreas, boolean useServer, UIManagerUserConfiguration config) {
        logger.enter("UIManager");
        if(uiGameAreas == null || uiGameAreas.length != GAME_BOARDS_LENGTH) throw new GdxRuntimeException("Cannot initialize UIManager, gameboards are of wrong size.");
        setUseServer(useServer);
        this.uiAreas = uiGameAreas;
        init(config);
        logger.exit("UIManager");
    }

    /*public UIManager(GameBoard[] areas){
        this(areas, false);
    }*/

    public void setDebug(boolean isDebug){
        for(GameBoard area : uiAreas){
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
            serverGameManger = new GameManager(getGameTable());
        }

        if(serverGameManger != null){
            //serverGameManger.startGame();
        } else {
            logger.error("Unable to start GameManger!");
        }
        //initializeJoinButtons();
        //updateGameAreas();
        //updateUI();
        logger.debug("Exit init()");
    }

    private YokelTable getGameTable(){
        return config.getCurrentTable();
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
        //serverGameManger.showGameBoard(6);
        //serverGameManger.showGameBoard(1);
        logger.enter("startSimulatedGame");
        serverGameManger.startGame();
        logger.exit("startSimulatedGame");
    }

    public void stopSimulatedGame() {
        logger.enter("stopSimulatedGame");
        serverGameManger.stopGame();
        logger.exit("stopSimulatedGame");
    }

    @Override
    public void dispose() {}

    public void hideAllJoinButtons() {
        for(GameBoard gameboard : gameBoards){
            if(gameboard != null){
                gameboard.hideJoinButton();
            }
        }
        for(GameBoard area : uiAreas){
            if(area != null){
                area.hideJoinButton();
            }
        }
    }

    public void showAllJoinButtons() {
        for(GameBoard gameboard : gameBoards){
            if(gameboard != null){
                gameboard.showJoinButton();
            }
        }
        for(GameBoard area : uiAreas){
            if(area != null){
                area.showJoinButton();
            }
        }
    }

    public static class UIManagerUserConfiguration {
        private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(UIManagerUserConfiguration.class);

        OrderedMap<Integer, YokelPlayer> seats = GdxMaps.newOrderedMap();
        private YokelPlayer currentPlayer;
        private int tableNumber = -1;
        private int currentSeat = -1;
        private YokelTable table;

        public String toString(){
            return seats + "\ntableNumber: " + tableNumber + "\ncurrentSeat: " + currentSeat;
        }

        public UIManagerUserConfiguration() {
            for(int i= 0; i < 8; i++){
                seats.put(i, null);
            }
        }

        public void setSeat(int seatNumber, YokelPlayer player){
            logger.error("setting player {} @ seat[{}]", player, seatNumber);
            if(seatNumber < 0 || seatNumber > seats.size) return;
            seats.put(seatNumber, player);
            YokelSeat seat = table.getSeat(seatNumber);
            if(seat != null) {
                if(player == null) {
                    seat.standUp();
                } else {
                    seat.sitDown(player);
                }
            }
        }

        public YokelPlayer getSeat(int seatNumber) {
            return seats.get(seatNumber);
        }

        boolean isGameReady(){
            //TODO: needs to be if more than one group is ready.
            boolean gameReady = false;
            int count = 0;

            for(int seatNumber : seats.keys()){
                YokelPlayer player = seats.get(seatNumber);
                if(player != null){
                    ++count;
                }

                if(count > 1) {
                    gameReady = true;
                    break;
                }
            }
            return gameReady;
        }

        public int getTableNumber() {
            return tableNumber;
        }

        public void setTableNumber(int i){
            this.tableNumber = i;
        }

        void setCurrentPlayer(YokelPlayer player) {
            currentPlayer = player;
        }

        YokelPlayer getCurrentPlayer() {
            return currentPlayer;
        }

        void setCurrentSeat(int currentSeatNumber) {
            currentSeat = currentSeatNumber;
        }

        int getCurrentSeat() {
            return currentSeat;
        }

        void setCurrentTable(YokelTable currentTable) {
            this.table = currentTable;
        }

        YokelTable getCurrentTable() {
            return this.table;
        }

        public void updateConfig(SessionService sessionService) {
            logger.enter("updateConfig");
            logger.debug("seats={}", seats);

            if(sessionService != null){
                YokelTable table = sessionService.getCurrentTable();
                setTableNumber(sessionService.getCurrentTableNumber());
                //updateSeats(table);
                setCurrentPlayer(sessionService.getCurrentPlayer());
                setCurrentTable(sessionService.getCurrentTable());

                logger.debug("currentSeat={}", sessionService.getCurrentSeat());
            }
            logger.debug("seats on exit={}", seats);
            logger.exit("updateConfig");
        }

        private void updateSeats(YokelTable table) {
            if(table != null){
                for(int i= 0; i < 8; i++){
                    YokelSeat seat = table.getSeat(i);
                    if(seat != null){
                        setSeat(i, seat.getSeatedPlayer());
                    }
                }
            }
        }
    }

    //Needs to update the simulated game with the server state.
    public void update(GameManager game, float delta, SessionService sessionService) {
        if(logger.isDebugOn()){
            ObjectMap<String, Object> map = GdxMaps.newObjectMap();
            map.put("game", game);
            map.put("delta", delta);
            map.put("sessionService", sessionService);
            logger.enter("update", map);
        }
        //logger.enter("update");
        updateConfig(sessionService);

        //Update UI rooms
        updateGameAreas();

        logger.exit("update");
    }


    private void updateConfig(SessionService sessionService) {
        config.updateConfig(sessionService);
        serverGameManger.setTable(sessionService.getCurrentTable());
    }

    private void updateGameAreas(){
        logger.enter("setUpGameArea");
        logger.error("config={}", config);
        YokelTable table = config.getCurrentTable();
        logger.error("table={}", table);

        //If table does not exist, keep it moving
        if(table == null) return;
        int playerSeat = config.getCurrentSeat();
        if(playerSeat < 0){
            logger.debug("No Player seated, setting player seat to 1");
            playerSeat = 0;
        }

        YokelPlayer readyPlayerOne = config.getSeat(playerSeat);
        logger.debug("player={}", playerSeat);
        logger.debug("currentPlayer={}", readyPlayerOne);

        //Set up Player View with
        setUpPlayerArea(readyPlayerOne, playerSeat,true);

        //Set up Partner View
        int partnerSeat = getPlayerPartnerSeatNum(playerSeat);
        YokelPlayer readyPartnerOne = getSeatedPlayer(partnerSeat, table);

        logger.debug("partnerSeat={}", partnerSeat);
        logger.debug("partner={}", readyPartnerOne);
        setUpPlayerArea(readyPartnerOne, partnerSeat,false);

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
    }

    private String printBoards(GameBoard[] gameBoards) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if(gameBoards != null){
            for(GameBoard gameboard : gameBoards){
                sb.append(gameboard.getName()).append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private int getPlayerPartnerSeatNum(int playerSeat) {
        if (playerSeat % 2 == 0) {
            return playerSeat + 1;
        } else {
            return playerSeat - 1;
        }
    }

    private YokelPlayer getSeatedPlayer(int seatNum, YokelTable table){
        if(logger.isDebugOn()){
            ObjectMap<String, Object> map = GdxMaps.newObjectMap();
            map.put("seatNum", seatNum);
            map.put("table", table);
            logger.debug("Enter getSeatedPlayer()", map);
        }

        if(table != null){
            YokelSeat seat = table.getSeat(seatNum);
            if(seat != null){
                logger.debug("seat={}", seat);
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
        gameBoards[playerSeat] = uiAreas[areaNum];
        GameBoard area = gameBoards[playerSeat];

        logger.debug("areaNum={}", areaNum);

        if(area != null){
            YokelGameBoard board = serverGameManger.getGameBoard(playerSeat);
            //area.addButtonListener(getListener(playerSeat));

            if(board != null){
                area.setPreview(isPreview);

                if(player != null){
                    logger.error("player={}", player);
                    logger.error("isGameReady={}", config.isGameReady());

                    area.setPlayerView(isPlayerView);
                    area.setActive(isActive);
                    area.setGameReady(config.isGameReady());
                    area.setIsPlayerReady(!player.equals(config.getCurrentPlayer()));
                    area.sitPlayerDown(player);
                } else {
                    area.standPlayerUp();
                }
               // area.update(board);
            }
            //[areaNum] = area;
        }
        logger.debug("Exit activateArea()");
    }


}
