package net.asg.games.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.storage.StorageInterface;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

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

    //<"room id", room object>
    private OrderedMap<String, YokelLounge> lounges;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> registeredPlayers;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> testPlayers;
    private int maxNumberOfRooms;
    private int timeOut;
    private int port = 8000;
    private float tickRate = 1000;
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

    public void update(){}

    public void readClientInputs(){}

    public void processServerResponses(){}

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

            validateLounges();
            validateRegisteredPlayers();

            initializeParams(args);
            initializeGameRooms();

            if(isDebug){
                generateTestPlayers();
            }
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

    private YokelLounge createLounge(String loungeName) throws Exception{
        try{
            Logger.trace("Enter createLounge()");
            Logger.debug("loungeName={}",loungeName);
            YokelLounge yl = null;
            if(!StringUtils.isEmpty(loungeName)) {
                yl = new YokelLounge(loungeName);
            }
            Logger.trace("Exit createLounge()={}",yl);
            return yl;
        } catch (Exception e){
            Logger.error(e, "Error creating Lounge: ");
            throw new Exception("Error creating lounges", e);
        }
    }

    private YokelLounge getDefaultLounge() throws Exception {
        try{
            Logger.trace("Enter createLounge()");
            YokelLounge lounge = getLounge(YokelLounge.DEFAULT_LOUNGE);
            if(lounge == null){
                lounge = new YokelLounge(YokelLounge.DEFAULT_LOUNGE);
                addLounge(lounge);
            }
            Logger.trace("Exit createLounge()");
            return lounge;
        } catch(Exception e){
            Logger.error("Error creating default lounge.", e);
            throw new Exception("Error creating default lounge.", e);
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

    public int getPort(){
        Logger.info("setting port to: {}", port);
        return port;
    }

    private void setMaxRooms(int rooms){
        Logger.info("setting max lounges to: {}", rooms);
        this.maxNumberOfRooms = rooms;
    }

    public Level getLogLevel(){
        Logger.info("getting log level");
        return logLevel;
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

    private class GameRunner implements Runnable{
        ServerManager manager;

        public GameRunner(ServerManager manager){
            this.manager = manager;
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

    public ServerResponse handleClientRequest(ClientRequest request) throws Exception {
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

    private Array<String> testPlayersToJSON(){
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

    private void addNewTable(String[] clientPayload) throws Exception {
        try{
            Logger.trace("Enter addNewTable()");

            if(clientPayload != null){
                String loungeName = Util.getStringValue(clientPayload, 0);
                String roomName = Util.getStringValue(clientPayload, 1);
                String type = Util.getStringValue(clientPayload, 2);
                boolean rated = Util.getBooleanValue(clientPayload, 3);

                OrderedMap<String, Object> arguments = new OrderedMap<>();
                arguments.put("type", type);
                arguments.put("rated", rated);

                YokelLounge lounge = getLounge(loungeName);

                Logger.debug("loungeName={}", loungeName);
                Logger.debug("roomName={}", roomName);
                Logger.debug("type={}", type);
                Logger.debug("rated={}", rated);
                Logger.debug("arguments={}", arguments);
                Logger.debug("lounge={}", lounge);

                if(lounge != null){
                    YokelRoom room = lounge.getRoom(roomName);
                    Logger.debug("room={}", room);

                    if(room != null){
                        room.addTable(1, arguments);
                    }
                }
            }
            Logger.trace("Exit addNewTable()");
        } catch (Exception e) {
            Logger.error("Error adding new table.", e);
            throw new Exception("Error adding new table.", e);
        }
    }

    private String[] buildPayload(String message, String[] clientPayload) throws Exception {
        Logger.trace("Enter buildPayload()");

        String[] responsePayload = null;
        try {
            if (!StringUtils.isEmpty(message)) {
                ServerRequest value = ServerRequest.valueOf(message);
                switch (value) {
                    case REQUEST_LOGIN:
                        break;
                    case REQUEST_PLAYER_REGISTER:
                        responsePayload = registerPlayerRequest(getRegisterPlayerFromPayload(clientPayload));
                        break;
                    case REQUEST_CREATE_GAME:
                        addNewTable(clientPayload);
                        break;
                    case REQUEST_PLAY_GAME:
                        break;
                    case REQUEST_TABLE_STAND:
                        break;
                    case REQUEST_TABLE_JOIN:
                        break;
                    case REQUEST_TABLE_SIT:
                        break;
                    case REQUEST_ROOM:
                        break;
                    case REQUEST_ROOM_JOIN:
                        break;
                    case REQUEST_ROOM_LEAVE:
                        break;
                    case REQUEST_ALL_TABLES:
                        break;
                    case REQUEST_LOUNGE:
                        responsePayload = getLoungesRequest(clientPayload);
                        break;
                    case REQUEST_ALL_LOUNGES:
                        responsePayload = Util.fromCollectionToStringArray(loungesToJSON());
                        break;
                    default:
                        Logger.error("Unknown Client Request: " + value);
                        throw new Exception("Unknown Client Request: " + value);
                }
            }
        } catch (Exception e){
            Logger.error(e);
        }
        Logger.trace("Exit buildPayload()=" + responsePayload);
        return responsePayload;
    }

    private YokelLounge getLounge(String key) throws Exception {
        try{
            Logger.trace("Enter getLounge()");
            validateLounges();

            String loungeName = Util.getLoungeName(key);
            YokelLounge lounge = lounges.get(loungeName);
            Logger.debug("Lounge({})={}",loungeName,lounge);
            Logger.trace("Exit getLounge()");
            return lounge;
        } catch (Exception e){
            Logger.error("Error getting Lounge.", e);
            throw new Exception(e);
        }
    }

    private void addLounge(YokelLounge lounge){
        validateLounges();
        lounges.put(lounge.getName(), lounge);
    }

    private Array<YokelLounge> getAllLounges(){
        return Util.getValuesArray(lounges.values());
    }

    private String[] registerPlayerRequest(YokelPlayer player){
        Logger.trace("Enter registerPlayerRequest()");

        String[] ret = new String[1];
        ret[0] = "false";
        if(player != null){
            Logger.info("Attempting to register player={}", player.toString());
            validateRegisteredPlayers();
            String playerId = player.getPlayerId();
            if(!registeredPlayers.containsKey(playerId)){
                registeredPlayers.put(playerId, player);
                ret[0] = "true";
                Logger.info("Player={} registered successfully.", player.toString());
            } else {
                YokelPlayer regPlayer = registeredPlayers.get(playerId);
                ret[0] = StringUtils.equalsIgnoreCase(player.getName(), regPlayer.getName()) + "";

                if(Boolean.parseBoolean(ret[0])){
                    Logger.info("Player={} already registered.", player.toString());
                } else {
                    Logger.warn("Player={} already registered with a different ID!", player.toString());
                }
            }
        }
        Logger.trace("Exit registerPlayerRequest()");
        return ret;
    }

    //Assumes only 1 array with player JSON
    private YokelPlayer getRegisterPlayerFromPayload(String[] clientPayload){
        if(clientPayload != null && clientPayload.length == 1){
            return Util.getObjectFromJsonString(YokelPlayer.class, clientPayload[0]);
        }
        return null;
    }

    private String[] getLoungesRequest(String[] clientPayload) throws Exception {
        Logger.trace("Enter getLoungesRequest()");
        String[] ret = new String[1];
        if(clientPayload != null && clientPayload.length == 1){
            String loungeName = clientPayload[0];
            YokelLounge lounge = getLounge(loungeName);
            ret[0] = lounge == null ? null : lounge.toString();
        }
        Logger.trace("Exit getLoungesRequest()");
        return ret;
    }
    //private void getDebugPlayersRequest();
    //private void joinRoomRequest();
    //private void leaveRoomRequest();
    //private void getRoomRequest();
    //private void getTablesRequest();
    //private void joinTableRequest();
    //private void sitAtTableRequest();
    //private void standFromTableRequest();
    //private void playGameRequest();
    //private void createGameRequest();
}
