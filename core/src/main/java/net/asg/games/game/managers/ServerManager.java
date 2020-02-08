package net.asg.games.game.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;
import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.server.serialization.AdminClientRequest;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.storage.StorageInterface;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

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

    //<"lounge name", room object>
    private OrderedMap<String, YokelLounge> lounges;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> registeredPlayers;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> testPlayers;
    //<"table name", gameManager>
    private OrderedMap<String, GameRunner> games;

    private ExecutorService threadPool;

    private int maxNumberOfRooms;
    private int timeOut;
    private int port = 8000;
    private float tickRate = 100;
    private boolean isDebug = true;
    private Level logLevel = Level.INFO;
    private StorageInterface storageInterface;

    public ServerManager(String... args){
        try {
            initialize(args);
        } catch (Exception e) {
            Logger.error(e,"Error during ServerManager initialization: ");
        }
    }

    private void validateLounges(){
        if(lounges == null){
            lounges = new OrderedMap<>();
        }
    }

    private void validateRegisteredPlayers(){
        if(registeredPlayers == null){
            registeredPlayers = new OrderedMap<>();
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            Logger.trace("Enter initialize()");
            Logger.info("Initializing server arguments: ");

            //storageInterface =

            games = new OrderedMap<>();
            validateLounges();
            validateRegisteredPlayers();

            initializeParams(args);
            initializeGameRooms();

            if(isDebug){
                generateTestPlayers();
            }

            //threadPool = new Executors.newCachedThreadPool();

            Logger.trace("Exit initialize()");
        } catch (Exception e) {
            Logger.error(e,"Error during initialization: ");
            throw new Exception("Error during initialization: ", e);
        }
    }

    private void generateTestPlayers() throws Exception{
        try {
            Logger.trace("Exit generateTestPlayers()");
            Logger.debug("Generating Debugable Players...");
            validateRegisteredPlayers();
            if(testPlayers == null){
                testPlayers = new OrderedMap<>();
            }
            int numPlayers = 8;
            while(numPlayers > 0){
                YokelPlayer player = new YokelPlayer(getRandomName());
                Logger.debug("Creating {}", player.getName());
                testPlayers.put(player.getName(), player);
                numPlayers--;
            }
            Logger.trace("Exit generateTestPlayers()");
        } catch (Exception e) {
            Logger.error(e,"Error generating test players: ");
            throw new Exception("Error generating test players: ", e);
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
            socialLounge.addRoom(room1);

            Logger.info("Creating Social: Leaning Tower of Pisa");
            YokelRoom room3 = new YokelRoom("Leaning Tower of Pisa");
            socialLounge.addRoom(room3);

            Logger.info("Creating Beginning: Chang Tower");
            YokelLounge beginningLounge = createLounge(YokelLounge.BEGINNER_GROUP);
            YokelRoom room2 = new YokelRoom("Chang Tower");
            beginningLounge.addRoom(room2);

            addLounge(socialLounge);
            addLounge(beginningLounge);
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
            if(!Util.isStaticArrayEmpty(args)){
                for(int i = 0; i < args.length; i++){
                    //System.out.println("Param: " + args[i]);
                    String param = args[i];
                    String paramValue = validateArumentParameterValue(i, args) ? args[i + 1] : null;

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
    private boolean validateArumentParameterValue(int i, String... args) throws Exception {
        Logger.trace("Enter validateArumentParameterValue()");
        if(Util.isStaticArrayEmpty(args)){
            Logger.error("Arguments cannot be null or empty.");
            throw new Exception("Arguments cannot be null or empty.");
        }
        Logger.debug("validating index {} in parameter= {}",i,args[i]);
        Logger.trace("Exit validateArumentParameterValue()");
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
        this.logLevel = Util.getTinyLogLevel(logLevel);
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
        if(lounges != null){
            lounges.clear();
            lounges = null;
        }

        if(registeredPlayers != null){
            registeredPlayers.clear();
            registeredPlayers = null;
        }

        if(testPlayers != null){
            testPlayers.clear();
            testPlayers = null;
        }

        if(threadPool != null){
            threadPool.shutdown();
        }
        Logger.trace("Exit shutDownServer()");
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

    private class GameRunner implements Runnable {
        ServerManager serverManager;
        GameManager gameManager;

        public GameRunner(ServerManager manager, YokelTable table){
            this.serverManager = manager;
            this.gameManager = new GameManager(table);
        }

        public void run() {
            /**
             * while(true)
             *     check for client commands
             *     sanity check client commands
             *     move all entities
             *     resolve collisions
             *     sanity check world data
             *     send updates about the game to the clients
             *     handle client disconnects
             * end while
             */
        }
    }

    public ServerResponse handleClientRequest(ClientRequest request){
        Logger.trace("Enter handleClientRequest()");

        String sessionId = null;
        String message = null;
        int requestSequence = -1;
        String[] serverPayload = null;

        if(request != null){
            Logger.debug("request: {}", request.getMessage());

            message = request.getMessage();
            sessionId = request.getSessionId();
            requestSequence = request.getRequestSequence();
            serverPayload = buildPayload(message, request.getPayload());
        }
        Logger.trace("Exit handleClientRequest()");
        return new ServerResponse(requestSequence, sessionId, message, getServerId(), serverPayload);
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
            response = new ServerResponse(requestSequence, sessionId, message, getServerId(), serverPayload);
        }
        Logger.trace("Exit handleAdminRequest()");
        return response;
    }

    private void sendServerResponse(Serializer serializer, ServerWebSocket webSocket, ServerResponse response) throws Exception{
        Logger.trace("Enter sendServerResponse()");
        try{
            if(response == null) throw new Exception("Server response was null");
            if(webSocket == null) throw new Exception("WebSocket is null, was it initialized?");
            final byte[] serialized = serializer.serialize(response);
            webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
            Logger.trace("Exit sendServerResponse()");
        } catch (Exception e){
            Logger.error(e,"Unable to send Server Response: ");
            throw new Exception("Unable to send Server Response: ", e);
        }
    }

    public void handleFrame(Serializer serializer, final ServerWebSocket webSocket, final WebSocketFrame frame) throws Exception {
        try {
            if(serializer == null) throw new Exception("No Serializer. Cannot deserialize websocket packet.");
            if(frame == null) throw new Exception("Incoming packet was null.");
            Logger.trace("Enter handleFrame()");
            final byte[] packet = frame.binaryData().getBytes();
            //final long start = System.nanoTime();
            Logger.info("Deserializing packet recieved");
            final Object deserialized = serializer.deserialize(packet);
            Logger.trace("deserialized: {}", deserialized);
            //final long time = System.nanoTime() - start;
            sendResponse(serializer, deserialized, webSocket);
            Logger.trace("Exit handleFrame()");
        } catch (Exception e){
            Logger.error(e,"Error handling websocket frame.");
            throw new Exception("Error handling websocket frame.", e);
        }
    }

    private void sendResponse(Serializer serializer, Object deserialized, ServerWebSocket webSocket) throws Exception{
        try{
            Logger.trace("Enter sendResponse()");
            if(deserialized instanceof ClientRequest){
                ClientRequest request = (ClientRequest) deserialized;
                clientRepsonse(serializer, webSocket, request);
            }

            if(deserialized instanceof AdminClientRequest){
                AdminClientRequest request = (AdminClientRequest) deserialized;
                adminResponse(serializer, webSocket, request);
            }
            Logger.trace("Exit sendResponse()");
        } catch (Exception e){
            Logger.error(e, "Error in sendResponse.");
            throw new Exception("Error handling websocket frame.", e);
        }
    }

    private void clientRepsonse(Serializer serializer, ServerWebSocket webSocket, ClientRequest request) throws Exception{
        Logger.trace("Enter clientRepsonse()");
        sendServerResponse(serializer, webSocket, handleClientRequest(request));
        Logger.trace("Exit clientRepsonse()");
    }

    private void adminResponse(Serializer serializer, ServerWebSocket webSocket, AdminClientRequest request) throws Exception{
        Logger.trace("Enter adminResponse()");
        sendServerResponse(serializer, webSocket, handleAdminRequest(request));
        Logger.trace("Exit adminResponse()");
    }

    public Array<String> testPlayersToJSON(){
        Array<String> jsonPlayers = new Array<>();

        for(String playerName : Util.toIterable(testPlayers.orderedKeys())){
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
        for(YokelLounge lounge : Util.toIterable(getAllLounges())){
            if(lounge != null){
                jsonLounge.add(lounge.toString());
            }
        }
        return jsonLounge;
    }

    private String[] buildPayload(String message, String[] clientPayload) {
        Logger.trace("Enter buildPayload()");

        String[] responsePayload = null;
        try {
            if (!StringUtils.isEmpty(message)) {
                ServerRequest value = ServerRequest.valueOf(message);
                switch (value) {
                    case REQUEST_LOGIN:
                        break;
                    case REQUEST_ALL_DEBUG_PLAYERS:
                        responsePayload = Util.toStringArray(testPlayersToJSON());
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
                        responsePayload = Util.toStringArray(loungesToJSON());
                        break;
                    default:
                        Logger.error("Unknown Client Request: " + value);
                        throw new Exception("Unknown Client Request: " + value);
                }
            }
        } catch (Exception e){
            Logger.error(e);
        }
        Logger.trace("Exit buildPayload()={}", Arrays.toString(responsePayload));
        return responsePayload;
    }

    private YokelLounge getLounge(String key) throws Exception {
        try{
            Logger.trace("Enter getLounge()");
            validateLounges();
            Logger.info("Attempting to get {} lounge.", key);
            YokelLounge lounge = null;

            if(key != null){
                lounge = lounges.get(key);
            }
            Logger.debug("GameLounge({})={}", key,lounge);
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
            YokelLounge yl = null;

            if(!StringUtils.isEmpty(loungeName)) {
                yl = new YokelLounge(loungeName);
                addLounge(yl);
            }
            Logger.trace("Exit createLounge()={}",yl);
            return yl;
        } catch (Exception e){
            Logger.error(e, "Error creating GameLounge: ");
            throw new Exception("Error creating lounges", e);
        }
    }

    private void addLounge(YokelLounge lounge){
        validateLounges();
        lounges.put(lounge.getName(), lounge);
    }

    private Array<YokelLounge> getAllLounges(){
        validateLounges();
        return Util.getValuesArray(lounges.values());
    }

    private Array<YokelRoom> getAllRooms(String loungeName) throws Exception {
        YokelLounge lounge = getLounge(loungeName);
        Array<YokelRoom> rooms = null;
        if(lounge != null){
            OrderedMap<String, YokelRoom> roomsMap = lounge.getAllRooms();

            if(roomsMap != null){
                rooms = Util.getValuesArray(roomsMap.values());
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
                OrderedMap<Integer, YokelTable> tableMap = room.getAllTables();

                tables = Util.getValuesArray(tableMap.values());
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
        validateLounges();
        YokelRoom room = null;

        YokelLounge lounge = getLounge(loungeName);

        if(lounge != null && roomName != null){
            room = lounge.getRoom(roomName);
        }
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
        Logger.trace("Exit getTable()");
        return table;
    }

    private YokelSeat getSeat(String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
        YokelSeat seat = null;

        YokelTable table = getTable(loungeName, roomName, tableNumber);

        if(table != null){
            seat = table.getSeat(seatNumber);
        }
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
                Logger.debug("seat=" + seat);
            }
        }
        Logger.trace("Exit sitAtTable()=" + success);
        return success;
    }

    private boolean standAtTable(String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
        YokelSeat seat = getSeat(loungeName, roomName, tableNumber, seatNumber);

        if(seat != null){
            seat.standUp();
            return true;
        }
        return false;
    }

    private boolean startGameAtTable(String loungeName, String roomName, int tableNumber) throws Exception {
        Logger.trace("Enter startGameAtTable()");
        YokelTable table = getTable(loungeName, roomName, tableNumber);

        if(table != null){
            Logger.info("attempting to start game at " + roomName + ":table:" + table.getTableNumber());
            GameRunner game = getGameFromTable(loungeName, roomName, table);
            //store thead id for runner
            //if(){

            //}
        }
        Logger.trace("Exit startGameAtTable()=" + false);
        return false;
    }

    private GameRunner getGameFromTable(String loungeName, String roomName, YokelTable table) throws Exception{
        if(loungeName == null) throw new Exception("Error getting game from table: Lounge is null.");
        if(roomName == null) throw new Exception("Error getting game from table: Room is null.");
        int tableNumber = table.getTableNumber();
        GameRunner game = games.get(loungeName+roomName+tableNumber);
        if(game == null){
            game = new GameRunner(this, table);
            games.put(loungeName + ":" + roomName + ":" + tableNumber, game);
        }
        return game;
    }

    private boolean registerPlayer(YokelPlayer player){
        if(player != null){
            validateRegisteredPlayers();
            String playerId = player.getPlayerId();
            if(!registeredPlayers.containsKey(playerId)){
                registeredPlayers.put(playerId, player);
                Logger.info("Player={} registered successfully.", player.toString());
                return true;
            } else {
                YokelPlayer regPlayer = registeredPlayers.get(playerId);
                boolean doesExist = StringUtils.equalsIgnoreCase(player.getName(), regPlayer.getName());

                if(doesExist){
                    Logger.warn("Player={} already registered.", player.toString());
                } else {
                    Logger.warn("Player={} already registered with a different ID!", player.toString());
                }
            }
        }
        return false;
    }

    private YokelPlayer getRegisteredPlayer(String playerId){
        if(playerId != null){
            return registeredPlayers.get(playerId);
        }
        return null;
    }

    //0 = Player Name
    private String[] registerPlayerRequest(String[] clientPayload){
        Logger.trace("Enter registerPlayerRequest()");

        String[] ret = new String[1];
        ret[0] = "false";

        YokelPlayer player = PayloadUtil.getRegisterPlayerFromPayload(clientPayload);
        if(player != null){
            Logger.info("Attempting to register player={}", player.toString());
            ret[0] = Util.otos(registerPlayer(player));
        }
        Logger.trace("Exit registerPlayerRequest()");
        return ret;
    }

    //0 = GameLounge Name
    private String[] getLoungesRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter getLoungesRequest()");
        String[] ret = new String[1];
        if(Util.isValidPayload(clientPayload, 1)){
            String loungeName = clientPayload[0];
            YokelLounge lounge = getLounge(loungeName);
            ret[0] = lounge == null ? null : lounge.toString();
        }
        Logger.trace("Exit getLoungesRequest()");
        return ret;
    }

    //0 = loungeName
    //1 = roomName
    private String[] getRoomRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter getRoomRequest()");
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));

        String[] ret = new String[1];
        if(Util.isValidPayload(clientPayload, 2)){
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
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        try{
            String[] ret = new String[1];
            ret[0] = "false";
            if(Util.isValidPayload(clientPayload, 4)){
                String loungeName = Util.getStringValue(clientPayload, 0);
                String roomName = Util.getStringValue(clientPayload, 1);
                String type = Util.getStringValue(clientPayload, 2);
                boolean isRated = Util.getBooleanValue(clientPayload, 3);

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

        if(Util.isValidPayload(clientPayload, 3)){
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

        if(Util.isValidPayload(clientPayload, 3)){
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

        if(Util.isValidPayload(clientPayload, 2)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];

            return Util.toStringArray(getAllTables(loungeName, roomName));
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
        Logger.trace("Received payloatd=" + Arrays.toString(clientPayload));
        String[] ret = new String[1];

        if(Util.isValidPayload(clientPayload, 5)){
            String playerId = clientPayload[0];
            String loungeName = clientPayload[1];
            String roomName = clientPayload[2];
            int tableNumber = Util.otoi(clientPayload[3]);
            int seatNumber = Util.otoi(clientPayload[4]);

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

        if(Util.isValidPayload(clientPayload, 4)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            int tableNumber = Util.otoi(clientPayload[2]);
            int seatNumber = Util.otoi(clientPayload[3]);

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

        if(Util.isValidPayload(clientPayload, 3)){
            String loungeName = clientPayload[0];
            String roomName = clientPayload[1];
            int tableNumber = Util.otoi(clientPayload[2]);

            ret[0] = "" + startGameAtTable(loungeName, roomName, tableNumber);
        }
        Logger.trace("Exit startGameRequest()");
        return ret;
    }

    //No payload from lient
    //private void playGameRequest();
}
