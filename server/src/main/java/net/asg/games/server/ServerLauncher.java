package net.asg.games.server;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;


/** Launches the server application. */
public class ServerLauncher {
    //protected final Logger logger = LoggerService.forClass(ServerLauncher.class);
    private final AtomicInteger idCounter = new AtomicInteger();

    private final static String PORT_ATTR = "-port";
    private final static String PORT2_ATTR = "-p";
    private final static String TIMEOUT_ATTR = "-t";
    private final static String ROOM_ATTR = "-r";
    private final static String LOG_LEVEL_ATTR = "-log";
    private final static String TICK_RATE_ATTR = "-tickrate";
    private final static String DEBUG_ATTR = "-debug";
    private final static String SERVER_BUILD = "0.0.1";

    private final static Array<String> SERVER_ARGS = ImmutableArray.of(PORT2_ATTR, PORT_ATTR, ROOM_ATTR, TIMEOUT_ATTR, TICK_RATE_ATTR, LOG_LEVEL_ATTR);
    private final static Array<String> PLAYER_NAMES = ImmutableArray.of("Hector","Lenny","Cullen","Kinsley","Tylor","Doug","Spring","Danica","Bekki",
            "Spirit","Harmony","Shelton","Philip","Liana","Joyce","Tucker","Jo","Cora","Philadelphia","Leyton");

    private final Vertx vertx = Vertx.vertx();
    private final ManualSerializer serializer;
    private final JsonSerializer jsonSerializer;

    private int port = 8000;
    private Map<String, Collection<YokelRoom>> rooms;
    private Map<String, YokelPlayer> registeredPlayers;
    private Map<String, YokelPlayer> testPlayers;
    private int maxNumberOfRooms;
    private int logLevel;
    private int tickRate;
    private int timeOut;
    private HttpServer listen;
    private boolean isDebug = false;

    private ServerLauncher() {
        serializer = new ManualSerializer();
        jsonSerializer = new JsonSerializer();
        Packets.register(serializer);
    }

    public static void main(final String... args) throws Exception {
        try{
            new ServerLauncher().launch(args);
        } catch (Exception e) {
            throw new Exception("Error in main: ", e);
        }
    }

    private void launch(String... args) throws Exception {
        try {
            //logger.info("Launching YokelTowers-server build" + SERVER_BUILD);
            System.out.println("Launching YokelTowers-server build: " + SERVER_BUILD);
            initialize(args);

            final HttpServer server = vertx.createHttpServer();
            server.websocketHandler(webSocket -> {
                // Printing received packets to console, sending response:
                webSocket.frameHandler(frame -> {
                    try {
                        handleFrame(webSocket, frame);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                //vertx.setTimer(5000L, id -> webSocket.close());
                //System.exit(-1);
            }).listen(getPort());
        } catch (Exception e) {
            throw new Exception("Error Launching Server: ", e);
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            initializeParams(args);
            initializeGameRooms();
            if(registeredPlayers == null){
                registeredPlayers = new HashMap<>();
            }
            if(isDebug){
                generateTestPlayers();
            }
        } catch (Exception e) {
            throw new Exception("Error during initialization: ", e);
        }
    }

    private void generateTestPlayers() throws Exception{
        try {
            System.out.println("Generating Debugable Players...");
            if(testPlayers == null){
                testPlayers = new HashMap<>();
            }
            int numPlayers = 8;
            while(numPlayers > 0){
                YokelPlayer player = new YokelPlayer(getRandomName());
                System.out.println("Creating " + player.getName());
                testPlayers.put(player.getName(), player);
                numPlayers--;
            }
        } catch (Exception e) {
            throw new Exception("Error generating test players: ", e);
        }
    }

    private void initializeGameRooms() throws Exception {
        try {
            System.out.println("Initializing Game Rooms...");
            rooms = new HashMap<>();

            System.out.println("Creating Social: Eiffel Tower");
            YokelRoom room1 = new YokelRoom("Eiffel Tower");
            addRoom(YokelRoom.SOCIAL_GROUP, room1);
            YokelRoom room2 = new YokelRoom("Chang Tower");
            addRoom(YokelRoom.BEGINNER_GROUP, room2);
        } catch (Exception e) {
            throw new Exception("Error initializing game rooms: ", e);
        }
    }

    private void addRoom(String group, YokelRoom room) throws Exception {
        try {
            if(StringUtils.isEmpty(group)){
                throw new Exception("Group cannot be null.");
            }
            if(room == null){
                throw new Exception("Room cannot be null.");
            }

            Collection<YokelRoom> roomList = rooms.get(group);

            if(roomList == null){
               roomList = new ArrayList<>();
            }

            System.out.println("adding '" + room.getName() + "' to Group:" + group);
            roomList.add(room);
            rooms.put(group, roomList);
        } catch (Exception e) {
            throw new Exception("Error adding game room: ", e);
        }
    }

    private void initializeParams(String... args) throws Exception {
        try {
            //logger.info("initializing input parameters");
            System.out.println("Evaluating input parameters...");
            if(Util.isArrayEmpty(args)){
                //logger.error("Cannot Launch Server, no arguments found.");
                throw new Exception("Cannot Launch Server, no arguments found.");
            }

            //System.out.println("argument size=" + args.length);
            for(int i = 0; i < args.length; i++){
                //System.out.println("Param: " + args[i]);
                String param = args[i];
                String paramValue = validateArumentParameterValue(i, args) ? args[i + 1] : null;

                if(StringUtils.equalsIgnoreCase(DEBUG_ATTR,args[i])){
                    this.isDebug = true;
                    break;
                }

                if(!StringUtils.isEmpty(paramValue)){
                    if (StringUtils.equalsIgnoreCase(PORT_ATTR, param)) {
                        setPort(Integer.parseInt(paramValue));
                    } else if (StringUtils.equalsIgnoreCase(PORT2_ATTR, param)) {
                        setPort(Integer.parseInt(paramValue));
                    } else if (StringUtils.equalsIgnoreCase(ROOM_ATTR, param)) {
                        maxNumberOfRooms = Integer.parseInt(paramValue);
                    } else if (StringUtils.equalsIgnoreCase(TIMEOUT_ATTR, param)) {
                        setTickRate(Integer.parseInt(paramValue));
                    } else if (StringUtils.equalsIgnoreCase(TICK_RATE_ATTR, param)) {
                        setTickRate(Integer.parseInt(paramValue));
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error initializing input parameters: ", e);
        }
    }
    //validate that the next value is not a parameter string
    //if it is something else, it will fail when we try to set it.
    private boolean validateArumentParameterValue(int i, String... args) throws Exception {
        if(Util.isArrayEmpty(args)){
            throw new Exception("Arguments cannot be null or empty.");
        }
        //logger.debug("validating index {} in parameter={}",i,args[i]);
        System.out.println("validating index " + i + " in parameter= " + args[i]);
        return i != args.length - 1 && !SERVER_ARGS.contains(args[i + 1], false);
    }

    private void setPort(int port){
        //Logger.debug("calling setPort()");
        System.out.println("setting port to: " + port);
        this.port = port;
    }

    private int getPort(){
        //Logger.debug("calling getPort()");
        return port;
    }
    private int getLogLevel(){
        return logLevel;
    }

    private Map getRooms(){
        return rooms;
    }

    private void shutDownServer(int errorCode){
        if(vertx != null){
            vertx.close();
        }

        if(listen != null){
            listen.close();
        }

        if(rooms != null){
            rooms.clear();
            rooms = null;
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
            System.out.println("deserialized: " + deserialized);
            ClientRequest request = (ClientRequest) deserialized;

            ServerResponse serverResponse = handleClientRequest(request);
            System.out.println("serverResponse: " + serverResponse);

            final byte[] serialized = serializer.serialize(serverResponse);
            webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
        }
    }

    private ServerResponse handleClientRequest(ClientRequest request) throws Exception {
        String sessionId = null;
        String message = null;
        int requestSequence = -1;
        String[] payload = null;

        if(request != null){
            System.out.println("request: " + request.getMessage());
            message = request.getMessage();
            sessionId = request.getSessionId();
            requestSequence = request.getRequestSequence();
            payload = buildPayload(message);
        }
        return new ServerResponse(requestSequence, sessionId, message, getServerId(), payload);
    }

    private Array<String> testPlayersToJSON(){
        Array<String> jsonPlayers = new Array<>();
        Json json  = new Json();;
        for(String playerName : testPlayers.keySet()){
            if(!StringUtils.isEmpty(playerName)){
                YokelPlayer player = testPlayers.get(playerName);
                if(player != null){
                    jsonPlayers.add(json.toJson(player));
                }
            }
        }
        return jsonPlayers;
    }

    private String[] buildPayload(String message) throws Exception {
        String[] load = null;
        if(!StringUtils.isEmpty(message)){
            ServerRequest value = ServerRequest.valueOf(message);
            switch (value) {
                case GET_PLAYER_LIST :
                    load = Util.fromCollectionToArray(testPlayersToJSON());
                    break;
                default:
                    throw new Exception("Unknown Server Request: " + value);
            }
        }
        return load;
    }
}