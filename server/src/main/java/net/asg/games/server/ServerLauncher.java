package net.asg.games.server;

import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import com.badlogic.gdx.utils.Array;

import com.github.czyzby.kiwi.util.common.Comparables;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;

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
import jdk.nashorn.internal.runtime.ECMAException;


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
    private final static String SERVER_BUILD = "0.0.1";

    private final static Array<String> SERVER_ARGS = ImmutableArray.of(PORT2_ATTR, PORT_ATTR, ROOM_ATTR, TIMEOUT_ATTR, TICK_RATE_ATTR, LOG_LEVEL_ATTR);

    private final Vertx vertx = Vertx.vertx();
    private final ManualSerializer serializer;
    private final JsonSerializer jsonSerializer;

    private int port;
    private Map<String, Collection<YokelRoom>> rooms;
    private Map<String, Boolean> players;
    private int maxNumberOfRooms;
    private int logLevel;
    private int tickRate;
    private int timeOut;
    private HttpServer listen;

    private ServerLauncher() {
        serializer = new ManualSerializer();
        Packets.register(serializer);
        jsonSerializer = new JsonSerializer();
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
                webSocket.frameHandler(frame -> handleStringFrame(webSocket, frame));
                // Closing the socket in 5 seconds:
                System.out.println("Closing the socket in 5 seconds:");
                vertx.setTimer(5000L, id -> webSocket.close());
                //System.exit(-1);
            }).listen(getPort());

        /*
        HttpServer server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // String test:
            webSocket.frameHandler(frame -> handleStringFrame(webSocket, frame));
        }).listen(8000);
        server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // JSON test:
            webSocket.frameHandler(frame -> handleJsonFrame(webSocket, frame));
        }).listen(8001);
        server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // Serialization test:
            webSocket.frameHandler(frame -> handleSerializationFrame(webSocket, frame));
        }).listen(8002);*/
        } catch (Exception e) {
            throw new Exception("Error Launching Server: ", e);
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            initializeParams(args);
            initializeGameRooms();
            generateTestPlayers();
        } catch (Exception e) {
            throw new Exception("Error during initialization: ", e);
        }
    }

    private void generateTestPlayers() throws Exception{
        try {
            System.out.println("Generating Debugable Players...");
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
            if(!StringUtils.isEmpty(group)){
                throw new Exception("Group cannot be null.");
            }
            if(room == null){
                throw new Exception("Room cannot be null.");
            }

            Collection<YokelRoom> roomList = rooms.get(group);

            if(roomList == null){
               roomList = new ArrayList<YokelRoom>();
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
            if(args == null){
                //logger.error("Cannot Launch Server, no arguments found.");
                throw new Exception("Cannot Launch Server, no arguments found.");
            }

            for(int i = 0; i < args.length; i++){
                if(i < args.length - 1){
                    if(PORT_ATTR.equalsIgnoreCase(args[i])){
                        if(validateArumentParameterValue(i, args)){
                            setPort(Integer.parseInt(args[i + 1]));
                        }
                    } else if(PORT2_ATTR.equalsIgnoreCase(args[i])){
                        if(validateArumentParameterValue(i, args)){
                            setPort(Integer.parseInt(args[i + 1]));
                        }
                    } else if(ROOM_ATTR.equalsIgnoreCase(args[i])){
                        if(validateArumentParameterValue(i, args)){
                            maxNumberOfRooms = Integer.parseInt(args[i + 1]);
                        }
                    } else if(TIMEOUT_ATTR.equalsIgnoreCase(args[i])){
                        if(validateArumentParameterValue(i, args)){
                            setTickRate(Integer.parseInt(args[i + 1]));
                        }
                    } else if(TICK_RATE_ATTR.equalsIgnoreCase(args[i])){
                        if(validateArumentParameterValue(i, args)){
                            setTickRate(Integer.parseInt(args[i + 1]));
                        }
                    } else if(LOG_LEVEL_ATTR.equalsIgnoreCase(args[i])){
                        if(validateArumentParameterValue(i, args)){
                            //setLogLevel(args[i + 1]);
                        }
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
        if(args == null){
            throw new Exception("Arguments cannot be null.");
        }
        //logger.debug("validating index {} in parameter={}",i,args[i]);
        System.out.println("validating index {} in parameter={}");
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

    private void shutDownServer(){

    }
    private void setTickRate(int tickRate){
        this.tickRate = tickRate;
    }

    private int getTickRate(){
        return logLevel;
    }

    private int getServerId(){
        return idCounter.get();
    }

    private static void handleStringFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        final String response = "Packet had " + frame.binaryData().length()
                + " bytes. Cannot deserialize packet class.";
        System.out.println(response);
        webSocket.writeFinalTextFrame(response);
    }

    private void handleJsonFrame(final ServerWebSocket webSocket, final WebSocketFrame frame){
        try {

        } catch (Exception e) {

        }
        final byte[] packet = frame.binaryData().getBytes();
        final long start = System.nanoTime();
        final Object deserialized = jsonSerializer.deserialize(packet);
        final long time = System.nanoTime() - start;

        final net.asg.games.server.json.ServerResponse response = new net.asg.games.server.json.ServerResponse();
        response.message = "Packet had " + packet.length + " bytes. Class: " + deserialized.getClass().getSimpleName()
                + ", took " + time + " nanos to deserialize.";
        System.out.println(response.message);
        final byte[] serialized = jsonSerializer.serialize(response);
        webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
    }

    private void handleSerializationFrame(final ServerWebSocket webSocket, final WebSocketFrame frame){
        try {

        } catch (Exception e) {

        }
        final byte[] packet = frame.binaryData().getBytes();
        final long start = System.nanoTime();
        final Object deserialized = serializer.deserialize(packet);
        final long time = System.nanoTime() - start;

        final net.asg.games.server.serialization.ServerResponse response = new ServerResponse("Packet had " + packet.length + " bytes. Class: "
                + deserialized.getClass().getSimpleName() + ", took " + time + " nanos to deserialize.");
        System.out.println(response.getMessage());
        final byte[] serialized = serializer.serialize(response);
        webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
    }
}