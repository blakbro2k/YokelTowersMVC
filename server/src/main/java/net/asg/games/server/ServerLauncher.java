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

    private int port = 8000;
    //<"room id", room object>
    private Array<YokelLounge> rooms;
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
            }).listen(getPort());
        } catch (Exception e) {
            throw new Exception("Error Launching Server: ", e);
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            initializeParams(args);
            initializeGameRooms();
            //System.out.println(rooms);

            if(registeredPlayers == null){
                registeredPlayers = new OrderedMap<>();
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
                testPlayers = new OrderedMap<>();
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
            if(rooms == null){
                rooms = new Array<>();
            }

            System.out.println("Creating Social: Eiffel Tower");
            YokelLounge socialLounge = new YokelLounge(YokelRoom.SOCIAL_GROUP);
            YokelRoom room1 = new YokelRoom("Eiffel Tower");
            socialLounge.addRoom(room1);
            System.out.println("Creating Social: Leaning Tower of Pisa");
            YokelRoom room3 = new YokelRoom("Leaning Tower of Pisa");
            socialLounge.addRoom(room3);

            System.out.println("Creating Beginning: Chang Tower");
            YokelLounge beginningLounge = new YokelLounge(YokelRoom.BEGINNER_GROUP);
            YokelRoom room2 = new YokelRoom("Chang Tower");
            beginningLounge.addRoom(room2);

            rooms.add(socialLounge);
            rooms.add(beginningLounge);
        } catch (Exception e) {
            throw new Exception("Error initializing game rooms: ", e);
        }
    }

    private void initializeParams(String... args) throws Exception {
        try {
            //logger.info("initializing input parameters");
            System.out.println("Evaluating input parameters...");
            if(Util.isStaticArrayEmpty(args)){
                //logger.error("Cannot Launch Server, no arguments found.");
                throw new Exception("Cannot Launch Server, no arguments found.");
            }

            //System.out.println("argument size=" + args.length);
            for(int i = 0; i < args.length; i++){
                //System.out.println("Param: " + args[i]);
                String param = args[i];
                String paramValue = validateArumentParameterValue(i, args) ? args[i + 1] : null;

                if(StringUtils.equalsIgnoreCase(DEBUG_ATTR,args[i])){
                    setDebug(true);
                    break;
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
        if(Util.isStaticArrayEmpty(args)){
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

    private void setDebug(boolean b){
        System.out.println("setting debug to: " + b);
        this.isDebug = b;
    }

    private int getPort(){
        //Logger.debug("calling getPort()");
        return port;
    }

    private void setMaxRooms(int rooms){
        System.out.println("setting max rooms to: " + rooms);
        this.maxNumberOfRooms = rooms;
    }

    private int getLogLevel(){
        return logLevel;
    }

    private Array<YokelLounge> getRooms(){
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
        Array<String> jsonRooms = new Array<>();
        for(YokelLounge room : Util.toIterable(rooms)){
            if(room != null){
                jsonRooms.add(Util.getJsonString(room));
            }
        }
        return jsonRooms;
    }

    private String[] buildPayload(String message) {
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
                    default:
                        throw new Exception("Unknown Server Request: " + value);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return load;
    }
}