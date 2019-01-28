package net.asg.games.server;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.ServerManager;
import net.asg.games.server.serialization.AdminClientRequest;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;

import org.pmw.tinylog.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

/** Launches the server application. */
public class ServerLauncher {
    private final static String SERVER_BUILD = "0.0.1";

    private ServerManager serverDaemon;
    private final Vertx vertx = Vertx.vertx();
    private final ManualSerializer serializer = new ManualSerializer();

    private ServerLauncher() {
        registerSerializer();
    }

    public static void main(final String... args) throws Exception {
        try{
            new ServerLauncher().launch(args);
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
            Logger.info("Launching YokelTowers-server build: {}", SERVER_BUILD);
            initialize(args);
            initializeNetwork();
            Logger.trace("Exit launch()");
        } catch (Exception e) {
            Logger.error(e,"Error Launching Server: ");
            serverDaemon.shutDownServer(1);
            throw new Exception("Error Launching Server: ", e);
        }
    }

    private void initializeNetwork() throws Exception{
        try{
            Logger.trace("Enter initializeNetwork()");
            Logger.info("Initializing Network Listener.");
            if(serverDaemon == null) throw new Exception("Server daemon was not started!");

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
            }).listen(serverDaemon.getPort());
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
            serverDaemon = new ServerManager(args);
            Logger.info("Server daemon started..");
            Logger.trace("Exit initialize()");
        } catch (Exception e) {
            Logger.error(e,"Error during initialization: ");
            throw new Exception("Error during initialization: ", e);
        }
    }

    private void sendServerResponse(ServerWebSocket webSocket, ServerResponse response){
        Logger.trace("Enter sendServerResponse()");
        if(response != null && webSocket != null){
            final byte[] serialized = serializer.serialize(response);
            webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
        }
        Logger.trace("Exit sendServerResponse()");
    }

    public void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) throws Exception {
        try {
            Logger.trace("Enter handleFrame()");
            final byte[] packet = frame.binaryData().getBytes();
            final long start = System.nanoTime();
            final Object deserialized = serializer.deserialize(packet);
            Logger.trace("Deserializing packet recieved");
            Logger.trace("deserialized: {}", deserialized);
            final long time = System.nanoTime() - start;

            if(deserialized instanceof ClientRequest){
                ClientRequest request = (ClientRequest) deserialized;

                if(serverDaemon != null){
                    sendServerResponse(webSocket, serverDaemon.handleClientRequest(request));
                }
            }

            if(deserialized instanceof AdminClientRequest){
                AdminClientRequest request = (AdminClientRequest) deserialized;

                sendServerResponse(webSocket, handleAdminRequest(request));
            }
            Logger.trace("Exit handleFrame()");
        } catch (Exception e){
            Logger.error("Error handling websocket frame.");
            throw new Exception("Error handling websocket frame.");
        }
    }

    private ServerResponse handleAdminRequest(AdminClientRequest request) throws Exception {
        try{
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
                response = new ServerResponse(requestSequence, sessionId, message, serverDaemon.getServerId(), serverPayload);
            }
            Logger.trace("Exit handleAdminRequest()");
            return response;
        } catch (Exception e){
            Logger.error("Error handling AdminRequest frame.");
            throw new Exception("Error handling AdminRequest frame.");
        }
    }
}