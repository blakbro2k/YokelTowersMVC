package net.asg.games.admin;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.managers.ServerManager;
import net.asg.games.server.serialization.AdminClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.YokelUtilities;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

/** Launches the server application. */
public class AdminLauncher {
    private final static int LOCAL_PORT = 8000;
    private final static String LOCAL_HOST = "localhost";

    private final Vertx vertx = Vertx.vertx();
    private final ManualSerializer serializer = new ManualSerializer();

    private int port = LOCAL_PORT;
    private String host = LOCAL_HOST;

    private AdminLauncher() {
        registerSerializer();
    }

    public static void main(final String... args) throws Exception {
        try{
            new AdminLauncher().launch(args);
        } catch (Exception e) {
            Logger.error(e,"Error in main: ");
            throw new Exception("Error in main: ", e);
        }
    }

    private void registerSerializer(){
        Packets.register(serializer);
    }

    private void launch(String... args) throws Exception {
        try {
            Logger.trace("Enter launch()");
            Logger.info("Launching Admin client");
            initialize(args);
            initializeNetwork();

            Logger.trace("Exit launch()");
        } catch (Exception e) {
            Logger.error(e,"Error Launching Server: ");
            throw new Exception("Error Launching Server: ", e);
        }
    }

    private void initializeNetwork()throws Exception{
        try{
            Logger.trace("Enter initializeNetwork()");
            Logger.info("Initializing Network Listener.");

            final HttpServer server = vertx.createHttpServer();
            server.websocketHandler(webSocket -> {

                // Printing received packets to console, sending response:
                webSocket.frameHandler(frame -> {
                    try {
                        handleFrame(webSocket, frame);
                    } catch (Exception e) {
                        Logger.error(e, "There was an error handling client request");
                        Logger.error(e);
                    }
                });
            }).listen(getPort());
            Logger.trace("Exit initializeNetwork()");
        } catch(Exception e) {
            Logger.error(e,"Error Setting up Network Listener: ");
            throw new Exception("Error Setting up Network Listener: ", e);
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            Logger.trace("Enter initialize()");
            Logger.info("Initializing server arguments: ");
            initializeParams(args);

            Logger.info("Server daemon started..");
            Logger.trace("Exit initialize()");
        } catch (Exception e) {
            Logger.error(e,"Error during initialization: ");
            throw new Exception("Error during initialization: ", e);
        }
    }

    private void initializeParams(String... args) throws Exception {
        try {
            Logger.trace("Enter initializeParams()");
            Logger.info("Evaluating input parameters...");
            if(!YokelUtilities.isStaticArrayEmpty(args)){
                for(int i = 0; i < args.length; i++){
                    //System.out.println("Param: " + args[i]);
                    String param = args[i];
                    String paramValue = validateArumentParameterValue(i, args) ? args[i + 1] : null;

                    if(!StringUtils.isEmpty(paramValue)){
                        if (StringUtils.equalsIgnoreCase(ServerManager.PORT_ATTR, param)) {
                            setPort(Integer.parseInt(paramValue));
                        } else if (StringUtils.equalsIgnoreCase(ServerManager.PORT2_ATTR, param)) {
                            setPort(Integer.parseInt(paramValue));
                        } else if (StringUtils.equalsIgnoreCase(ServerManager.HOST_ATTR, param)) {
                            setHost(paramValue);
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
        if(YokelUtilities.isStaticArrayEmpty(args)){
            Logger.error("Arguments cannot be null or empty.");
            throw new Exception("Arguments cannot be null or empty.");
        }
        Logger.debug("validating index " + i + " in parameter= {}", args[i]);
        Logger.trace("Exit validateArumentParameterValue()");
        return i != args.length - 1 && !ServerManager.SERVER_ARGS.contains(args[i + 1], false);
    }

    private void setPort(int port){
        this.port = port;
    }

    private int getPort(){
        return this.port;
    }

    private void setHost(String host){
        this.host = host;
    }

    private String getHost(){
        return this.host;
    }

    public void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) throws Exception {
        try {
            Logger.trace("Enter handleFrame()");
            final byte[] packet = frame.binaryData().getBytes();
            final long start = System.nanoTime();
            final Object deserialized = serializer.deserialize(packet);
            final long time = System.nanoTime() - start;

            if(deserialized instanceof AdminClientRequest){
                Logger.trace("Deserializing packet recieved");
                Logger.trace("deserialized: {}", deserialized);

                AdminClientRequest request = (AdminClientRequest) deserialized;

                String sessionId = null;
                String message = null;
                int requestSequence = -1;
                String[] serverPayload = null;

                Logger.debug("request: {}", request.getMessage());

                message = request.getMessage();
                sessionId = request.getSessionId();
                requestSequence = request.getRequestSequence();
                //serverPayload = null; //buildPayload(message, request.getPayload());

                ServerResponse serverResponse = new ServerResponse(requestSequence, sessionId, message, serverPayload, -1);
                Logger.debug("serverResponse: {}", serverResponse);

                final byte[] serialized = serializer.serialize(serverResponse);
                webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
                Logger.trace("Exit handleFrame()");
            }
        } catch (Exception e){
            Logger.error("Error handling websocket frame.");
            throw new Exception("Error handling websocket frame.");
        }
    }
}