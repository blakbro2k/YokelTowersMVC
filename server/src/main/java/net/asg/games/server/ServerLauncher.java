package net.asg.games.server;

import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;

import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;

import java.util.Map;
import java.util.Queue;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;


/** Launches the server application. */
public class ServerLauncher {
    private final static String PORT_ATTR = "-port";
    private final static String PORT2_ATTR = "-p";
    private final static String TIMEOUT_ATTR = "-t";
    private final static String ROOM_ATTR = "-r";
    private final static String LOG_LEVEL_ATTR = "-log";
    private final static String TICK_RATE_ATTR = "-tickrate";
    private final static String SERVER_BUILD = "0.0.1";

    private final static ImmutableList<String> SERVER_ARGS = ImmutableList.of(PORT2_ATTR, PORT_ATTR, ROOM_ATTR, TIMEOUT_ATTR, TICK_RATE_ATTR, LOG_LEVEL_ATTR);

    private final Vertx vertx = Vertx.vertx();
    private final ManualSerializer serializer;
    private final JsonSerializer jsonSerializer;

    private int port;
    private Queue<Room> rooms;
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
        new ServerLauncher().launch(args);
    }

    private void launch(String... args) throws Exception {
        initialize(args);
        System.out.println("Launching web socket server...");
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
        }).listen(8002);
    }

    private void initialize(String... args) throws Exception{
        //Logger.info("initializing input parameters");

        if(args == null){
            //Logger.error("Cannot Launch Server, no arguments found.");
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
       // initializeVariables();
        //setUpRooms();
    }

    //validate that the next value is not a parameter string
    //if it is something else, it will fail when we try to set it.
    private boolean validateArumentParameterValue(int i, String... args){
        //Logger.debug("validating index {} in parameter={}",i,args[i]);
        return i != args.length - 1 && !SERVER_ARGS.contains(args[i + 1]);
    }

    private void setPort(int port){
        //Logger.debug("calling setPort()");
        this.port = port;
    }

    private int getPort(){
        //Logger.debug("calling getPort()");
        return port;
    }
    private int getLogLevel(){
        return logLevel;
    }

    private void setTickRate(int tickRate){
        this.tickRate = tickRate;
    }

    private int getTickRate(){
        return logLevel;
    }

    private void setUpRooms(){ }

    private int getServerId(){
        return 1;
    }

    private static void handleStringFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        final String response = "Packet had " + frame.binaryData().length()
                + " bytes. Cannot deserialize packet class.";
        System.out.println(response);
        webSocket.writeFinalTextFrame(response);
    }

    private void handleJsonFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
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

    private void handleSerializationFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
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