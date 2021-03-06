package net.asg.games.game.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.server.serialization.AdminClientRequest;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.storage.YokelStorageAdapter;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.YokelUtilities;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerManager {
    private final AtomicInteger idCounter = new AtomicInteger();

    public final static String PORT_ATTR = "-port";
    public final static String PORT2_ATTR = "-p";
    private final static String TIMEOUT_ATTR = "-t";
    public final static String HOST_ATTR = "-host";
    private final static String ROOM_ATTR = "-r";
    public final static String LOG_LEVEL_ATTR = "-log";
    private final static String TICK_RATE_ATTR = "-tickrate";
    public final static String DEBUG_ATTR = "-test";

    public final static Array<String> SERVER_ARGS = ImmutableArray.of(HOST_ATTR, PORT2_ATTR, PORT_ATTR, ROOM_ATTR, TIMEOUT_ATTR, TICK_RATE_ATTR, LOG_LEVEL_ATTR);
    private final static Array<String> PLAYER_NAMES = ImmutableArray.of("Hector","Lenny","Cullen","Kinsley","Tylor","Doug","Spring","Danica","Bekki",
            "Spirit","Harmony","Shelton","Philip","Liana","Joyce","Tucker","Jo","Cora","Philadelphia","Leyton");


    private int maxNumberOfRooms;
    private int timeOut;
    private int port = 8000;
    private float tickRate = 100;
    private boolean isDebug = true;
    private Level logLevel = Level.INFO;
    private YokelStorageAdapter storage;
    private OrderedMap<String, YokelPlayer> testPlayers;

    public ServerManager(YokelStorageAdapter storage, String... args){
        try {
            this.storage = storage;
            initialize(args);
        } catch (Exception e) {
            Logger.error(e,"Error during ServerManager initialization: ");
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            Logger.trace("Enter initialize()");
            Logger.info("Initializing server arguments: ");
            initializeParams(args);
            initializeGameRooms();
            Logger.trace("Exit initialize()");
        } catch (Exception e) {
            Logger.error(e,"Error during initialization: ");
            throw new Exception("Error during initialization: ", e);
        }
    }

    ObjectMap.Values<GameManager> getAllGames() throws Exception {
        if(storage == null) throw new Exception("Cannot access storage object!");
        return storage.getAllGames();
    }

    void putAllGames(ObjectMap.Values<GameManager> games) throws Exception{
        if(storage == null) throw new Exception("Cannot access storage object!");
        if(games != null){
            storage.putAllGames(games);
        }
    }

    private void initializeGameRooms() throws Exception {
        try {
            Logger.trace("Enter initializeGameRooms()");
            Logger.info("Initializing Game Rooms...");
            generateDefaultLounges();
            Logger.trace("Exit initializeGameRooms()");
        } catch (Exception e) {
            Logger.error(e, "Error initializing game lounges: ");
            throw new Exception("Error initializing game lounges: ", e);
        }
    }

    private void generateDefaultLounges() throws Exception {
        try{
            Logger.trace("Enter generateDefaultLounges()");

            Logger.info("Creating Social: Eiffel Tower");
            YokelLounge socialLounge = createLounge(YokelLounge.SOCIAL_GROUP);
            YokelRoom room1 = new YokelRoom("Eiffel Tower");
            Logger.debug("room1=" + room1);
            socialLounge.addRoom(room1);

            Logger.info("Creating Social: Leaning Tower of Pisa");
            YokelRoom room3 = new YokelRoom("Leaning Tower of Pisa");
            Logger.debug("room3=" + room3);
            socialLounge.addRoom(room3);

            Logger.info("Creating Beginning: Chang Tower");
            YokelLounge beginningLounge = createLounge(YokelLounge.BEGINNER_GROUP);
            YokelRoom room2 = new YokelRoom("Chang Tower");
            Logger.debug("room2=" + room2);

            beginningLounge.addRoom(room2);

            addLounge(socialLounge);
            Logger.debug("socialLounge=" + socialLounge);

            addLounge(beginningLounge);
            Logger.debug("beginningLounge=" + beginningLounge);

            Logger.trace("Exit generateDefaultLounges()");
        } catch (Exception e){
            Logger.error(e, "Error generating default game lounges: ");
            throw new Exception("Error generating default game lounges: ", e);
        }
    }

    private void initializeParams(String... args) throws Exception {
        try {
            Logger.trace("Enter initializeParams()");
            Logger.info("Evaluating input parameters...");
            if(!YokelUtilities.isStaticArrayEmpty(args)){
                for(int i = 0; i < args.length; i++){
                    String param = args[i];
                    String paramValue = validateArgumentParameterValue(i, args) ? args[i + 1] : null;

                    if(StringUtils.equalsIgnoreCase(DEBUG_ATTR,args[i])){
                        setDebug(true);
                    }

                    if(!StringUtils.isEmpty(paramValue)){
                        if (StringUtils.equalsIgnoreCase(PORT_ATTR, param)) {
                            setPort(Integer.parseInt(paramValue));
                        } else if (StringUtils.equalsIgnoreCase(PORT2_ATTR, param)) {
                            setPort(Integer.parseInt(paramValue));
                        } else if (StringUtils.equalsIgnoreCase(ROOM_ATTR, param)) {
                            setMaxRooms(Integer.parseInt(paramValue));
                        } else if (StringUtils.equalsIgnoreCase(TIMEOUT_ATTR, param)) {
                            setTickRate(Integer.parseInt(paramValue));
                        } else if (StringUtils.equalsIgnoreCase(TICK_RATE_ATTR, param)) {
                            setTickRate(Integer.parseInt(paramValue));
                        } else if (StringUtils.equalsIgnoreCase(LOG_LEVEL_ATTR, param)) {
                            setLogLevel(paramValue);
                        }
                    }
                }
            }
            Logger.trace("Exit initializeParams()");
        } catch (Exception e) {
            Logger.error(e, "Error initializing input parameters: ");
            throw new Exception("Error initializing input parameters: ", e);
        }
    }

    //validate that the next value is not a parameter string
    //if it is something else, it will fail when we try to set it.
    private boolean validateArgumentParameterValue(int i, String... args) throws Exception {
        Logger.trace("Enter validateArgumentParameterValue()");
        if(YokelUtilities.isStaticArrayEmpty(args)){
            Logger.error("Arguments cannot be null or empty.");
            throw new Exception("Arguments cannot be null or empty.");
        }
        Logger.debug("validating index {} in parameter= {}",i,args[i]);
        Logger.trace("Exit validateArgumentParameterValue()");
        return i != args.length - 1 && !SERVER_ARGS.contains(args[i + 1], false);
    }

    public void setPort(int port){
        Logger.info("setting port to: {}", port);
        this.port = port;
    }

    public int getPort(){
        Logger.info("setting port to: {}", port);
        return port;
    }

    public void setDebug(boolean b){
        Logger.info("setting debug to: {}", b);
        this.isDebug = b;
    }

    public boolean getDebug(){
        Logger.info("getting debug to: {}", isDebug);
        return isDebug;
    }

    public void setLogLevel(String logLevel){
        Logger.info("setting log level to: {}", logLevel);
        this.logLevel = YokelUtilities.getTinyLogLevel(logLevel);
        Configurator.defaultConfig().level(this.logLevel).activate();
    }

    public Level getLogLevel(){
        Logger.info("getting log level");
        return logLevel;
    }

    private void setMaxRooms(int rooms){
        Logger.info("setting max lounges to: {}", rooms);
        this.maxNumberOfRooms = rooms;
    }

    public void shutDownServer(int errorCode){
        Logger.trace("Enter shutDownServer()");
        Logger.info("Shutting server down...");

        if(testPlayers != null){
            testPlayers.clear();
            testPlayers = null;
        }

       /* if(threadPool != null){
            threadPool.shutdown();
        }*/
        Logger.trace("Exit shutDownServer("+ errorCode + ")");
        //System.exit(errorCode);
    }

    private String getRandomName(){
        return PLAYER_NAMES.random();
    }

    public void setTickRate(float tickRate){
        Logger.info("setting tick rate to: {}", tickRate);
        this.tickRate = tickRate;
    }

    public float getTickRate(){
        return this.tickRate;
    }

    public int getServerId(){
        return idCounter.get();
    }

    public ServerResponse handleClientRequest(ClientRequest request){
        Logger.trace("Enter handleClientRequest()");

        String sessionId = null;
        String message = null;
        int requestSequence = -1;
        String[] serverPayload = null;
        String clientId = null;

        if(request != null){
            Logger.debug("request: {}", request.getMessage());
            clientId = request.getClientId();
            message = request.getMessage();
            sessionId = request.getSessionId();
            requestSequence = request.getRequestSequence();
            serverPayload = buildPayload(message, request.getPayload());
        }
        Logger.trace("Exit handleClientRequest()");
        return new ServerResponse(requestSequence, sessionId, message, serverPayload, getServerId());
    }

    private ServerResponse handleAdminRequest(AdminClientRequest request) {
        Logger.trace("Enter handleAdminRequest()");
        ServerResponse response = null;

        if(request != null){
            String sessionId = null;
            String message = null;
            int requestSequence = -1;
            String[] serverPayload = null;

            Logger.debug("request: {}", request.getMessage());

            message = request.getMessage();
            sessionId = request.getSessionId();
            requestSequence = request.getRequestSequence();
            //serverPayload = null; //buildPayload(message, request.getPayload());
            response = new ServerResponse(requestSequence, sessionId, message, serverPayload, getServerId());
        }
        Logger.trace("Exit handleAdminRequest()");
        return response;
    }

    private Array<String> testPlayersToJSON(){
        Array<String> jsonPlayers = new Array<>();

        for(String playerName : YokelUtilities.getMapKeys(testPlayers)){
            if(!StringUtils.isEmpty(playerName)){
                YokelPlayer player = testPlayers.get(playerName);
                if(player != null){
                    jsonPlayers.add(player.toString());
                }
            }
        }
        return jsonPlayers;
    }

    private Array<String> loungesToJSON(){
        Array<String> jsonLounge = new Array<>();
        for(YokelLounge lounge : getAllLounges()){
            if(lounge != null){
                jsonLounge.add(lounge.toString());
            }
        }
        return jsonLounge;
    }

    private Array<String> playersToJSON(){
        Array<String> jsonPlayer = new Array<>();
        for(YokelPlayer player : getAllRegisteredPlayer()){
            if(player != null){
                jsonPlayer.add(player.toString());
            }
        }
        return jsonPlayer;
    }

    private String[] buildPayload(String message, String[] clientPayload) {
        Logger.trace("Enter buildPayload()");

        String[] responsePayload = null;
        try {
            if (!StringUtils.isEmpty(message)) {
                ServerRequest value = ServerRequest.valueOf(message);
                switch (value) {
                    case REQUEST_CLIENT_DISCONNECT:
                        responsePayload = disconnectPlayer(clientPayload);
                        break;
                    case REQUEST_ALL_REGISTERED_PLAYERS:
                        responsePayload = YokelUtilities.toStringArray(playersToJSON());
                        break;
                    case REQUEST_LOGOFF:
                        responsePayload = YokelUtilities.toStringArray(testPlayersToJSON());
                        break;
                    case REQUEST_PLAYER_REGISTER:
                        responsePayload = registerPlayerRequest(clientPayload);
                        break;
                    case REQUEST_CREATE_GAME:
                        responsePayload = createGameRequest(clientPayload);
                        break;
                    case REQUEST_PLAY_GAME:
                        responsePayload = startGameRequest(clientPayload);
                        break;
                    case REQUEST_TABLE_STAND:
                        responsePayload = tableStandRequest(clientPayload);
                        break;
                    case REQUEST_TABLE_JOIN:
                    case REQUEST_TABLE_SIT:
                        responsePayload = tableSitRequest(clientPayload);
                        break;
                    case REQUEST_ROOM:
                        responsePayload = getRoomRequest(clientPayload);
                        break;
                    case REQUEST_ROOM_JOIN:
                        responsePayload = joinRoomRequest(clientPayload);
                        break;
                    case REQUEST_ROOM_LEAVE:
                        responsePayload = leaveRoomRequest(clientPayload);
                        break;
                    case REQUEST_TABLE_INFO:
                        responsePayload = getTablesRequest(clientPayload);
                        break;
                    case REQUEST_LOUNGE:
                        responsePayload = getLoungesRequest(clientPayload);
                        break;
                    case REQUEST_LOUNGE_ALL:
                        responsePayload = YokelUtilities.toStringArray(loungesToJSON());
                        break;
                    case REQUEST_TABLE_MOVE_RIGHT:
                    case REQUEST_TABLE_MOVE_LEFT:
                    case REQUEST_TABLE_CYCLE_UP:
                    case REQUEST_TABLE_CYCLE_DOWN:
                    case REQUEST_TABLE_MOVE_START_DOWN:
                    case REQUEST_TABLE_MOVE_STOP_DOWN:
                        responsePayload = handleGamePieceMove(clientPayload);
                        break;
                    case REQUEST_TABLE_ATTACK_TARGET:
                    case REQUEST_TABLE_ATTACK_RANDOM :
                        responsePayload = handleGameAttack(clientPayload);
                        break;
                    case REQUEST_TABLE_GAME_MANAGER:
                        responsePayload = getGameManager(clientPayload);
                        break;
                    default:
                        Logger.error("Unknown Client Request: " + value);
                        throw new Exception("Unknown Client Request: " + value);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        Logger.trace("Exit buildPayload()={}", Arrays.toString(responsePayload));
        return responsePayload;
    }

    private YokelLounge getLounge(String loungName) throws Exception {
        try{
            Logger.trace("Enter getLounge()");
            //validateLounges();
            Logger.info("Attempting to get {} lounge.", loungName);
            YokelLounge lounge = null;

            if(loungName != null){
                lounge = storage.getLounge(loungName);
            }
            Logger.debug("GameLounge({})={}", loungName, lounge);
            Logger.trace("Exit getLounge()");
            return lounge;
        } catch (Exception e){
            Logger.error("Error getting GameLounge.", e);
            throw new Exception(e);
        }
    }

    private YokelLounge createLounge(String loungeName) throws Exception{
        Logger.trace("Enter createLounge()");
        try{
            Logger.debug("loungeName={}",loungeName);
            YokelLounge lounge = null;

            if(!StringUtils.isEmpty(loungeName)) {
                lounge = new YokelLounge(loungeName);
                addLounge(lounge);
            }
            Logger.trace("Exit createLounge()={}",lounge);
            return lounge;
        } catch (Exception e){
            Logger.error(e, "Error creating GameLounge: ");
            throw new Exception("Error creating lounges", e);
        }
    }

    private void addLounge(YokelLounge lounge) throws Exception {
        storage.putLounge(lounge);
    }

    private OrderedMap.Values<YokelLounge> getAllLounges(){
        return storage.getAllLounges();
    }


    private ObjectMap.Values<YokelPlayer> getAllRegisteredPlayer(){
        return storage.getAllRegisteredPlayers();
    }

    private OrderedMap.Values<YokelRoom> getAllRooms(String loungeName) throws Exception {
        YokelLounge lounge = getLounge(loungeName);
        OrderedMap.Values<YokelRoom> rooms = null;
        if(lounge != null){
            OrderedMap<String, YokelRoom> roomsMap = lounge.getAllRooms();

            if(roomsMap != null){
                rooms = YokelUtilities.getMapValues(roomsMap);
            }
        }
        return rooms;
    }

    private Array<YokelTable> getAllTables(String loungeName, String roomName) throws Exception {
        YokelLounge lounge = getLounge(loungeName);
        Array<YokelTable> tables = null;
        if(lounge != null){
            YokelRoom room = getRoom(lounge.getName(), roomName);

            if(room != null){
                tables = room.getAllTables();
            }
        }
        return tables;
    }

    private Array<YokelPlayer> getAllRoomPlayers(String loungeName, String roomName) throws Exception {
        YokelLounge lounge = getLounge(loungeName);
        Array<YokelPlayer> players = null;
        if(lounge != null){
            YokelRoom room = getRoom(lounge.getName(), roomName);

            if(room != null){
                players = room.getAllPlayers();
            }
        }
        return players;
    }

    private YokelRoom getRoom(String loungeName, String roomName) throws Exception {
        Logger.trace("Enter getRoom()");
        Logger.debug("Attempting to get Room : " + roomName + " in " + loungeName);
        YokelRoom room = null;
        YokelLounge lounge = getLounge(loungeName);

        if(lounge != null && roomName != null){
            room = lounge.getRoom(roomName);
        }
        Logger.trace("Exit getRoom()=" + room);
        return room;
    }

    private YokelTable getTable(String loungeName, String roomName, int tableNumber) throws Exception {
        Logger.trace("Enter getTable()");
        Logger.debug("Attempting to get Table(" + tableNumber + ")@" + roomName + " in " + loungeName);

        YokelTable table = null;
        YokelRoom room = getRoom(loungeName, roomName);

        if(room != null){
            table = room.getTable(tableNumber);
        }
        Logger.trace("Exit getTable()=" + table);
        return table;
    }

    private YokelSeat getSeat(String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
        Logger.trace("Enter getSeat()");
        Logger.debug("Attempting to get Seat=" + seatNumber +  " (" + tableNumber + ")@" + roomName + " in " + loungeName);

        YokelSeat seat = null;
        YokelTable table = getTable(loungeName, roomName, tableNumber);

        if(table != null){
            seat = table.getSeat(seatNumber);
        }
        Logger.trace("Exit getSeat()=" + seat);
        return seat;
    }

    private boolean joinLeaveRoom(YokelPlayer player, String loungeName, String roomName, boolean isJoin) throws Exception {
        if(player != null){
            YokelRoom room = getRoom(loungeName, roomName);

            if(room != null){
                if(isJoin){
                    room.joinRoom(player);
                } else {
                    room.leaveRoom(player);
                }
                return true;
            }
        }
        return false;
    }

    private boolean sitAtTable(YokelPlayer player, String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
        Logger.trace("Enter sitAtTable()");
        boolean success = false;
        if(player != null){
            YokelSeat seat = getSeat(loungeName, roomName, tableNumber, seatNumber);

            if(seat != null){
                success = seat.sitDown(player);
                storage.putSeat(seat);
                Logger.debug("seat=" + seat);
            }
        }
        Logger.trace("Exit sitAtTable()=" + success);
        return success;
    }

    private boolean standAtTable(String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
        YokelSeat seat = getSeat(loungeName, roomName, tableNumber, seatNumber);

        if(seat != null){
            YokelPlayer standingPlayer = seat.standUp();
            storage.putSeat(seat);
            //storage.standUpPlayer()
            return true;
        }
        return false;
    }

    private boolean startGameAtTable(String loungeName, String roomName, int tableNumber) throws Exception {
        Logger.trace("Enter startGameAtTable()");
        YokelTable table = getTable(loungeName, roomName, tableNumber);

        if(table != null){
            Logger.info("attempting to start game at " + roomName + ":table:" + table.getTableNumber());
            GameManager game = getGameFromTable(loungeName, roomName, table);

            //store thread id for runner
            if(game != null){
                game.resetGameBoards();
            } else {
                GameManager gameManager = new GameManager(table);
                storage.putGame(loungeName+roomName+tableNumber, gameManager);
            }
        }
        Logger.trace("Exit startGameAtTable()=" + false);
        return false;
    }

    private GameManager getGameFromTable(String loungeName, String roomName, YokelTable table) throws Exception{
        if(loungeName == null) throw new Exception("Error getting game from table: Lounge is null.");
        if(roomName == null) throw new Exception("Error getting game from table: Room is null.");
        int tableNumber = table.getTableNumber();

        return getGameFromTableNumber(loungeName,roomName, tableNumber);
    }

    private GameManager getGameFromTableNumber(String loungeName, String roomName, int tableNumber) throws Exception{
        if(loungeName == null) throw new Exception("Error getting game from table: Lounge is null.");
        if(roomName == null) throw new Exception("Error getting game from table: Room is null.");
        return storage.getGame(loungeName+roomName+tableNumber);
    }

    private boolean registerPlayer(String clientId, YokelPlayer player) throws Exception {
        if(player != null){
            //validateRegisteredPlayers();
            storage.registerPlayer(clientId, player);
        }
        return true;
    }

    private boolean unRegisterPlayer(String clientId) throws Exception {
        //validateRegisteredPlayers();
        storage.unRegisterPlayer(clientId);
        return true;
    }

    private YokelPlayer getRegisteredPlayer(String playerId){
        return storage.getRegisteredPlayerGivenId(playerId);
    }


    private boolean handleGameMove(String loungeName, String roomName, int tableNumber, int seatNumber, String action) throws Exception {
        Logger.trace("Enter handleGameMove()");
        GameManager game = getGameFromTableNumber(loungeName, roomName, tableNumber);

        if(game != null){
            switch (ServerRequest.valueOf(action)) {
                case REQUEST_TABLE_MOVE_RIGHT :
                    game.handleMoveRight(seatNumber);
                    break;
                case REQUEST_TABLE_MOVE_LEFT :
                    game.handleMoveLeft(seatNumber);
                    break;
                case REQUEST_TABLE_CYCLE_DOWN :
                    game.handleCycleDown(seatNumber);
                    break;
                case REQUEST_TABLE_CYCLE_UP :
                    game.handleCycleUp(seatNumber);
                    break;
                case REQUEST_TABLE_MOVE_START_DOWN :
                    game.handleStartMoveDown(seatNumber);
                    break;
                case REQUEST_TABLE_MOVE_STOP_DOWN :
                    game.handleStopMoveDown(seatNumber);
                    break;
                default:
                    Logger.error("Invalid Action give, ignoring.");
            }
        }
        Logger.trace("Exit handleGameMove()");
        return true;
    }

    private boolean handleGameBoardAttack(String loungeName, String roomName, int tableNumber, int seatNumber, int target) throws Exception {
        Logger.trace("Enter handleGameBoardAttack()");
        GameManager game = getGameFromTableNumber(loungeName, roomName, tableNumber);

        if(game != null){
            if(target > 0 && target <= 8){
                game.handleTargetAttack(seatNumber, target);
            } else {
                game.handleRandomAttack(seatNumber);
            }
        }
        Logger.trace("Exit handleGameBoardAttack()");
        return true;
    }

    //0 = Client ID
    //1 = Player Name
    private String[] registerPlayerRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter registerPlayerRequest()");

        String[] ret = new String[1];
        ret[0] = "false";

        YokelPlayer player = PayloadUtil.getRegisterPlayerFromPayload(clientPayload);
        String clientId = PayloadUtil.getClientIDFromPayload(clientPayload);
        Logger.trace("clientId=" + clientId);
        Logger.trace("player=" + player);

        if(player != null){
            Logger.info("Attempting to register player={}", player.toString());
            ret[0] = YokelUtilities.otos(registerPlayer(clientId, player));
        }
        Logger.trace("Exit registerPlayerRequest()");
        return ret;
    }

    //0 = Client ID
    private String[] disconnectPlayer(String[] clientPayload) throws Exception {
        Logger.trace("Enter registerPlayerRequest()");

        String[] ret = new String[1];
        ret[0] = "false";

        String clientId = PayloadUtil.getClientIDFromPayload(clientPayload);
        Logger.info("Removing client [{}] from all storage session.", clientId);

        //Remove user from registered list
        ret[0] = unRegisterPlayer(clientId) + "";
        Logger.trace("Exit registerPlayerRequest()");
        return ret;
    }

    //0 = GameLounge Name
    private String[] getLoungesRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter getLoungesRequest()");
        String[] ret = new String[1];
        if(YokelUtilities.isValidPayload(clientPayload, 1)){
            String loungeName = clientPayload[0];
            YokelLounge lounge = getLounge(loungeName);
            ret[0] = lounge == null ? null : lounge.toString();
        }
        Logger.trace("Exit getLoungesRequest()");
        return ret;
    }

    //0 = GameLounge Name
    //1 = GameRoom Name
    //2 = GameTable number
    private String[] getGameManager(String[] clientPayload) throws Exception {
        Logger.trace("Enter getGameManager()");
        String[] ret = new String[1];
        if(YokelUtilities.isValidPayload(clientPayload, 3)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            int tableNumber = YokelUtilities.otoi(clientPayload[2]);
            GameManager game = getGameFromTableNumber(loungeName, roomName, tableNumber);

            ret[0] = game == null ? null : game.toString();
        }
        Logger.trace("Exit getGameManager()");
        return ret;
    }

    //0 = loungeName
    //1 = roomName
    private String[] getRoomRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter getRoomRequest()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));

        String[] ret = new String[1];
        if(YokelUtilities.isValidPayload(clientPayload, 2)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            YokelRoom room = getRoom(loungeName, roomName);
            ret[0] = room == null ? null : room.toString();
        }
        Logger.trace("Exit getRoomRequest()");
        return ret;
    }

    //0 = loungeName
    //1 = roomName
    //2 = type
    //3 = isRated
    private String[] createGameRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter createGameRequest()");
        Logger.trace("Received payload=" + Arrays.toString(clientPayload));
        try{
            String[] ret = new String[1];
            ret[0] = "false";
            //TODO: Cannot create a table in a lounge you are not apart of
            if(YokelUtilities.isValidPayload(clientPayload, 4)){
                String loungeName = YokelUtilities.getStringValue(clientPayload, 0);
                String roomName = YokelUtilities.getStringValue(clientPayload, 1);
                String type = YokelUtilities.getStringValue(clientPayload, 2);
                boolean isRated = YokelUtilities.getBooleanValue(clientPayload, 3);

                OrderedMap<String, Object> arguments = new OrderedMap<>();
                arguments.put("type", type);
                arguments.put("rated", isRated);

                YokelLounge lounge = getLounge(loungeName);

                Logger.debug("loungeName={}", loungeName);
                Logger.debug("roomName={}", roomName);
                Logger.debug("type={}", type);
                Logger.debug("isRated={}", isRated);
                Logger.debug("arguments={}", arguments);
                Logger.debug("lounge={}", lounge);

                if(lounge != null){
                    YokelRoom room = lounge.getRoom(roomName);
                    Logger.debug("room={}", room);

                    if(room != null){
                        room.addTable(arguments);
                        Logger.debug("room={}", room);
                        ret[0] = "true";
                    }
                }
            }
            Logger.trace("Exit createGameRequest()");
            return ret;
        } catch (Exception e) {
            Logger.error("Error adding new table.", e);
            throw new Exception("Error adding new table.", e);
        }
    }

    //0 = Player id
    //1 = GameLounge Name
    //2 = Room Name
    private String[] joinRoomRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter joinRoomRequest()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];
        ret[0] = "false";

        if(YokelUtilities.isValidPayload(clientPayload, 3)){
            String playerId = clientPayload[0];
            String loungeName = clientPayload[1];
            String roomName = clientPayload[2];

            YokelPlayer player = getRegisteredPlayer(playerId);

            ret[0] = "" + joinLeaveRoom(player, loungeName, roomName, true);
        }
        Logger.trace("Exit joinRoomRequest()");
        return ret;
    }

    //0 = Player id
    //1 = GameLounge Name
    //2 = Room Name
    private String[] leaveRoomRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter leaveRoomRequest()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];
        ret[0] = "false";

        if(YokelUtilities.isValidPayload(clientPayload, 3)){
            String playerId = clientPayload[0];
            String loungeName = clientPayload[1];
            String roomName = clientPayload[2];

            YokelPlayer player = getRegisteredPlayer(playerId);

            ret[0] = "" + joinLeaveRoom(player, loungeName, roomName, false);
        }
        Logger.trace("Exit leaveRoomRequest()");
        return ret;
    }

    //0 = GameLounge Name
    //1 = Room Name
    private String[] getTablesRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter getTablesRequest()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));

        if(YokelUtilities.isValidPayload(clientPayload, 2)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];

            return YokelUtilities.toStringArray(getAllTables(loungeName, roomName));
        }
        Logger.trace("Exit getTablesRequest()");
        return new String[]{"false"};
    }

    //0 = Player id
    //1 = GameLounge Name
    //2 = Room Name
    //3 = Table Number
    //4 = Seat Number
    private String[] tableSitRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter tableSitRequest()");
        Logger.trace("Received payload=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];

        if(YokelUtilities.isValidPayload(clientPayload, 5)){
            String playerId = clientPayload[0];
            String loungeName = clientPayload[1];
            String roomName = clientPayload[2];
            int tableNumber = YokelUtilities.otoi(clientPayload[3]);
            int seatNumber = YokelUtilities.otoi(clientPayload[4]);

            YokelPlayer player = getRegisteredPlayer(playerId);
            Logger.debug("player=" + player);

            ret[0] = "" + sitAtTable(player, loungeName, roomName, tableNumber, seatNumber);
        }
        Logger.trace("Exit tableSitRequest()");
        return ret;
    }

    //0 = GameLounge Name
    //1 = Room Name
    //2 = Table Number
    //3 = Seat Number
    private String[] tableStandRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter tableStandRequest()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];

        if(YokelUtilities.isValidPayload(clientPayload, 4)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            int tableNumber = YokelUtilities.otoi(clientPayload[2]);
            int seatNumber = YokelUtilities.otoi(clientPayload[3]);

            ret[0] = "" + standAtTable(loungeName, roomName, tableNumber, seatNumber);
        }
        Logger.trace("Exit tableStandRequest()");
        return ret;
    }

    //0 = GameLounge Name
    //1 = Room Name
    //2 = Table Number
    private String[] startGameRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter startGameRequest()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];

        if(YokelUtilities.isValidPayload(clientPayload, 3)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            int tableNumber = YokelUtilities.otoi(clientPayload[2]);

            ret[0] = "" + startGameAtTable(loungeName, roomName, tableNumber);
        }
        Logger.trace("Exit startGameRequest()");
        return ret;
    }

    //0 = GameLounge Name
    //1 = Room Name
    //2 = Table Number
    //3 = Seat Number
    //4 = Action
    private String[] handleGamePieceMove(String[] clientPayload) throws Exception {
        Logger.trace("Enter moveGamePieceRight()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];

        if(YokelUtilities.isValidPayload(clientPayload, 5)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            int tableNumber = YokelUtilities.otoi(clientPayload[2]);
            int seatNumber = YokelUtilities.otoi(clientPayload[3]);
            String action = clientPayload[4];

            ret[0] = "" + handleGameMove(loungeName, roomName, tableNumber, seatNumber, action);
        }
        Logger.trace("Exit moveGamePieceRight()");
        return ret;
    }

    //0 = GameLounge Name
    //1 = Room Name
    //2 = Table Number
    //3 = Seat Number
    //4 = target seat
    private String[] handleGameAttack(String[] clientPayload) throws Exception {
        Logger.trace("Enter handleGameAttack()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];

        if(YokelUtilities.isValidPayload(clientPayload, 4) || YokelUtilities.isValidPayload(clientPayload, 5)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            int tableNumber = YokelUtilities.otoi(clientPayload[2]);
            int seatNumber = YokelUtilities.otoi(clientPayload[3]);
            int targetSeat = -1;

            if(clientPayload.length == 5){
                targetSeat = YokelUtilities.otoi(clientPayload[4]);
            }

            ret[0] = "" + handleGameBoardAttack(loungeName, roomName, tableNumber, seatNumber, targetSeat);
        }
        Logger.trace("Exit handleGameAttack()");
        return ret;
    }

    //No payload from lient
    //private void playGameRequest();
}
