package net.asg.games.server;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

/** Launches the server application. */
public class ServerLauncher {
    private final AtomicInteger idCounter = new AtomicInteger();

    private final static String PORT_ATTR = "-port";
    private final static String PORT2_ATTR = "-p";
    private final static String TIMEOUT_ATTR = "-t";
    private final static String ROOM_ATTR = "-r";
    private final static String LOG_LEVEL_ATTR = "-log";
    private final static String TICK_RATE_ATTR = "-tickrate";
    private final static String DEBUG_ATTR = "-test";
    private final static String SERVER_BUILD = "0.0.1";

    private final static Array<String> SERVER_ARGS = ImmutableArray.of(PORT2_ATTR, PORT_ATTR, ROOM_ATTR, TIMEOUT_ATTR, TICK_RATE_ATTR, LOG_LEVEL_ATTR);
    private final static Array<String> PLAYER_NAMES = ImmutableArray.of("Hector","Lenny","Cullen","Kinsley","Tylor","Doug","Spring","Danica","Bekki",
            "Spirit","Harmony","Shelton","Philip","Liana","Joyce","Tucker","Jo","Cora","Philadelphia","Leyton");

    private final Vertx vertx = Vertx.vertx();
    private final ManualSerializer serializer;

    private int port = 8000;
    //<"room id", room object>
    private OrderedMap<String, YokelLounge> lounges;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> registeredPlayers;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> testPlayers;
    private int maxNumberOfRooms;
    private int logLevel;
    private int tickRate;
    private int timeOut;
    private HttpServer listen;
    private boolean isDebug = false;

    private ServerLauncher() {
        serializer = new ManualSerializer();
        Packets.register(serializer);
    }

    public static void main(final String... args) throws Exception {
        try{
            new ServerLauncher().launch(args);
        } catch (Exception e) {
            Logger.error(e,"Error in main: ");
            throw new Exception("Error in main: ", e);
        }
    }

    private void launch(String... args) throws Exception {
        try {
            Logger.info("Launching YokelTowers-server build: {}", SERVER_BUILD);
            initialize(args);

            final HttpServer server = vertx.createHttpServer();
            server.websocketHandler(webSocket -> {

                // Printing received packets to console, sending response:
                webSocket.frameHandler(frame -> {
                    try {
                        handleFrame(webSocket, frame);
                    } catch (Exception e) {
                        Logger.error("There was an error handling client request");
                        Logger.error(e);
                    }
                });
            }).listen(getPort());
        } catch (Exception e) {
            Logger.error(e,"Error Launching Server: ");
            throw new Exception("Error Launching Server: ", e);
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            Logger.trace("Enter initialize()");
            Logger.info("Initializing server arguments: ");
            initializeParams(args);
            initializeGameRooms();
            //System.out.println(lounges);

            if(registeredPlayers == null){
                registeredPlayers = new OrderedMap<>();
            }
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
            if(testPlayers == null){
                testPlayers = new OrderedMap<>();
            }
            int numPlayers = 8;
            while(numPlayers > 0){
                YokelPlayer player = new YokelPlayer(getRandomName());
                Logger.debug("Creating " + player.getName());
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
            if(lounges == null){
                lounges = new OrderedMap<>();
            }

            Logger.info("Creating Social: Eiffel Tower");
            YokelLounge socialLounge = new YokelLounge(YokelLounge.SOCIAL_GROUP);
            YokelRoom room1 = new YokelRoom("Eiffel Tower");
            socialLounge.addRoom(room1);
            Logger.info("Creating Social: Leaning Tower of Pisa");
            YokelRoom room3 = new YokelRoom("Leaning Tower of Pisa");
            socialLounge.addRoom(room3);

            Logger.info("Creating Beginning: Chang Tower");
            YokelLounge beginningLounge = new YokelLounge(YokelLounge.BEGINNER_GROUP);
            YokelRoom room2 = new YokelRoom("Chang Tower");
            beginningLounge.addRoom(room2);

            lounges.put(YokelLounge.SOCIAL_GROUP, socialLounge);
            lounges.put(YokelLounge.BEGINNER_GROUP, beginningLounge);
            Logger.trace("Exit initializeGameRooms()");
        } catch (Exception e) {
            Logger.error(e, "Error initializing game lounges: ");
            throw new Exception("Error initializing game lounges: ", e);
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
        Logger.debug("validating index " + i + " in parameter= {}", args[i]);
        Logger.trace("Exit validateArumentParameterValue()");
        return i != args.length - 1 && !SERVER_ARGS.contains(args[i + 1], false);
    }

    private void setPort(int port){
        Logger.info("setting port to: {}", port);
        this.port = port;
    }

    private void setDebug(boolean b){
        Logger.info("setting debug to: {}", b);
        this.isDebug = b;
    }

    private void setLogLevel(String logLevel){
        Logger.info("setting log level to: {}", logLevel);
        Configurator.defaultConfig().level(getTinyLogLevel(logLevel)).activate();
    }

    private Level getTinyLogLevel(String logLevel){
        if(StringUtils.equalsIgnoreCase("trace", logLevel)){
            return Level.TRACE;
        } else if(StringUtils.equalsIgnoreCase("debug", logLevel)){
            return Level.DEBUG;
        } else if(StringUtils.equalsIgnoreCase("warn", logLevel)){
            return Level.WARNING;
        } else if(StringUtils.equalsIgnoreCase("error", logLevel)){
            return Level.ERROR;
        } else {
            return Level.INFO;
        }
    }

    private int getPort(){
        Logger.info("setting port to: {}", port);
        return port;
    }

    private void setMaxRooms(int rooms){
        Logger.info("setting max lounges to: {}", rooms);
        this.maxNumberOfRooms = rooms;
    }

    private int getLogLevel(){
        Logger.info("getting log level");
        return logLevel;
    }

    private YokelLounge getLounge(String key){
        if(lounges != null){
            String loungeName = Util.getLoungeName(key);
            YokelLounge lounge = lounges.get(loungeName);
            Logger.info("Lounge Name={}", loungeName);
            Logger.info("Lounge Object={}",lounge);
            return lounge;
        }
        return null;
    }

    private Array<YokelLounge> getAllLounges(){
        return Util.getValuesArray(lounges.values());
    }

    private void shutDownServer(int errorCode){
        if(vertx != null){
            vertx.close();
        }

        if(listen != null){
            listen.close();
        }

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
        System.exit(errorCode);
    }

    private String getRandomName(){
        return PLAYER_NAMES.random();
    }

    private void setTickRate(int tickRate){
        Logger.info("setting tick rate to: {}", tickRate);
        this.tickRate = tickRate;
    }

    private int getTickRate(){
        return this.tickRate;
    }

    private int getServerId(){
        return idCounter.get();
    }

    private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) throws Exception {
        final byte[] packet = frame.binaryData().getBytes();
        final long start = System.nanoTime();
        final Object deserialized = serializer.deserialize(packet);
        final long time = System.nanoTime() - start;

        if(deserialized instanceof ClientRequest){
            Logger.debug("Deserializing packet recieved");
            Logger.debug("deserialized: {}", deserialized);
            ClientRequest request = (ClientRequest) deserialized;

            ServerResponse serverResponse = handleClientRequest(request);
            Logger.debug("serverResponse: {}", serverResponse);

            final byte[] serialized = serializer.serialize(serverResponse);
            webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
        }
    }

    private ServerResponse handleClientRequest(ClientRequest request) throws Exception {
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
        return new ServerResponse(requestSequence, sessionId, message, getServerId(), serverPayload);
    }

    private Array<String> testPlayersToJSON(){
        Array<String> jsonPlayers = new Array<>();
        for(String playerName : Util.toIterable(testPlayers.orderedKeys())){
            if(!StringUtils.isEmpty(playerName)){
                YokelPlayer player = testPlayers.get(playerName);
                if(player != null){
                    jsonPlayers.add(Util.getJsonString(player));
                }
            }
        }
        return jsonPlayers;
    }

    private Array<String> loungesToJSON(){
        Array<String> jsonLounge = new Array<>();
        for(YokelLounge lounge : Util.toIterable(getAllLounges())){
            if(lounge != null){
                jsonLounge.add(Util.getJsonString(lounge));
            }
        }
        return jsonLounge;
    }

    private void addNewTable(String[] clientPayload){
        if(clientPayload != null){
            Logger.info(Arrays.asList(clientPayload));

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
    }

    private void printLounges(){
        Logger.info("Start Printing lounges:");

        if(lounges != null){
            for(String key : lounges.keys()){
                Logger.info(lounges.get(key).toString());
            }
        }
        Logger.info("End Printing lounges:");
    }

    private String[] buildPayload(String message, String[] clientPayload) {
        Logger.debug("Enter ");
        if(clientPayload != null){
            Logger.info(Arrays.asList(clientPayload));
        }

        String[] load = null;
        try {
            if (!StringUtils.isEmpty(message)) {
                ServerRequest value = ServerRequest.valueOf(message);
                switch (value) {
                    case REQUEST_TEST_PLAYER_LIST:
                        load = Util.fromCollectionToStringArray(testPlayersToJSON());
                        break;
                    case REQUEST_GAME_LOUNGE:
                        load = Util.fromCollectionToStringArray(loungesToJSON());
                        break;
                    case REQUEST_GAME_CREATE:
                        addNewTable(clientPayload);
                        break;
                    case REQUEST_PRINT_LOUNGES:
                        printLounges();
                        break;
                    default:
                        throw new Exception("Unknown Client Request: " + value);
                }
            }
        } catch (Exception e){
            Logger.error(e);
            if(clientPayload != null){
                Logger.info(Arrays.asList(clientPayload));
            }
        }
        return load;
    }
}