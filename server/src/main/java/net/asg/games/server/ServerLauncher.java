package net.asg.games.server;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.managers.ServerGameRunner;
import net.asg.games.game.managers.ServerManager;
import net.asg.games.server.serialization.AdminClientRequest;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.storage.YokelMemoryStorage;
import net.asg.games.storage.YokelStorageAdapter;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Logger;

import io.netty.buffer.Unpooled;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.http.impl.FrameType;
import io.vertx.core.http.impl.ws.WebSocketFrameImpl;

/** Launches the server application. */
public class ServerLauncher {
    private final static String SERVER_BUILD = "0.2.1";

    private ServerManager serverDaemon;
    private final static Vertx vertx = Vertx.vertx();
    private final static ManualSerializer serializer = new ManualSerializer();
    private YokelStorageAdapter storage;
    private static ServerGameRunner serverGameRunner;
    //private ExecutorService threadPool;

    private ServerLauncher() {
        registerSerializer();
    }

    public static void main(final String... args) throws Exception {
        try{
            Logger.info("Entering Main()");
            new ServerLauncher().launch(args);
        } catch (Exception e) {
            Logger.error(e,"Failed to launch server: ");
            throw new Exception("Failed to launch server: ", e);
        }
    }

    private void registerSerializer(){
        Packets.register(serializer);
    }

    private void launch(String... args) throws Exception {
        try {
            Logger.trace("Enter launch()");
            Logger.info("Launching YokelTowers-server build: {}", SERVER_BUILD);
            initializeStorage();
            initialize(args);
            initializeNetwork();

            //Start ServerGameRunner
            serverGameRunner = new ServerGameRunner(serverDaemon);
            Logger.trace("Exit launch()");
        } catch (Exception e) {
            Logger.error(e,"Error Launching Server: ");
            serverDaemon.shutDownServer(1);
            throw new Exception("Error Launching Server: ", e);
        }
        System.err.println("###Finally block Called###");
    }

    private void initializeStorage() throws Exception {
        try {
            Logger.trace("Enter launch()");
            Logger.info("Launching YokelTowers-server build: {}", SERVER_BUILD);

            //Start ServerGameRunner
            storage = new YokelMemoryStorage();
            Logger.trace("Exit launch()");
        } catch (Exception e) {
            Logger.error(e,"Error Launching Server: ");
            serverDaemon.shutDownServer(1);
            throw new Exception("Error Initializing Storage: ", e);
        }
    }

    private void initializeNetwork() throws Exception{
        try{
            Logger.trace("Enter initializeNetwork()");
            Logger.info("Initializing Network Listener.");
            if(serverDaemon == null) throw new Exception("Server daemon was not started!");

            final HttpServer server = vertx.createHttpServer();
            int port = serverDaemon.getPort();
            server.websocketHandler(webSocket -> {

                // Printing received packets to console, sending response:
                webSocket.frameHandler(frame -> {
                    try {
                        if(frame.isBinary()){
                            Logger.trace("is Binary");
                            handleFrame(webSocket, frame);
                        } else if(frame.isText()){
                            Logger.trace("is Text");
                            handleTextFrame(webSocket, frame);
                        } else if(frame.isClose()){
                            String handlerID = webSocket.binaryHandlerID();
                            Logger.info("Client [" + handlerID + "] closing connection");
                            handleFrame(webSocket, createDisconnectFrame(handlerID));
                        } else {
                            Logger.error("Received Unhandled WebSocket Frame type.");
                            throw new Exception("Received Unhandled WebSocket Frame type.");
                        }
                    } catch (Exception e) {
                        Logger.error(e, "There was an error handling client request");
                    }
                });
            }).listen(port);
            Logger.trace("Exit initializeNetwork()");
        } catch(Exception e) {
            Logger.error(e,"Error Setting up Network Listener: ");
            throw new Exception("Error Setting up Network Listener: ", e);
        }
    }

    private WebSocketFrame createDisconnectFrame(String id) throws Exception {
        try{
            Logger.trace("Enter createDisconnectFrame()");
            WebSocketFrame frame = new WebSocketFrameImpl(FrameType.BINARY, Unpooled.wrappedBuffer(serializedRequest(createDisconnectRequest(id))), true);
            Logger.trace("Exit createDisconnectFrame()=" + frame);
            return frame;
        } catch(Exception e) {
            Logger.error(e,"Error creating disconnect frame: ");
            throw new Exception("Error creating disconnect frame: ", e);
        }
    }

    private byte[] serializedRequest(ClientRequest request){
        Logger.trace("Enter serializedRequest()");
        return serializer.serialize(request);
    }

    private ClientRequest createDisconnectRequest(String handlerId) throws Exception {
        try{
            Logger.trace("Enter createDisconnectRequest()");
            ClientRequest request = new ClientRequest(-1,
                    "",
                    ServerRequest.REQUEST_CLIENT_DISCONNECT.toString(),
                    PayloadUtil.createPlayerDisconnectRequest(handlerId),
                    handlerId);
            Logger.trace("Exit createDisconnectRequest()=" + request);
            return request;
        } catch (Exception e) {
            Logger.error(e,"Error creating disconnect request: ");
            throw new Exception("Error creating disconnect request: ", e);
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            Logger.trace("Enter initialize()");
            Logger.info("Initializing server arguments: ");
            serverDaemon = new ServerManager(storage, args);
            Logger.info("Server daemon started..");
            Logger.trace("Exit initialize()");
        } catch (Exception e) {
            Logger.error(e,"Error during initialization: ");
            throw new Exception("Error during initialization: ", e);
        }
    }

    private void sendClientId(ServerWebSocket webSocket, String handlerID) throws Exception {
        try{
            Logger.trace("Enter sendClentId()");
            ServerResponse response = new ServerResponse(-1,
                    handlerID,
                    ServerRequest.REQUEST_CLIENT_ID.toString(),
                    new String[]{handlerID},
                    getServerId());
            sendServerResponse(webSocket, response);
            Logger.trace("Exit sendClentId()");
        } catch (Exception e) {
            Logger.error(e,"Unable to send Client ID: ");
            throw new Exception("Unable to send Client ID: ", e);
        }
    }

    private void sendServerResponse(ServerWebSocket webSocket, ServerResponse response) throws Exception{
        Logger.trace("Enter sendServerResponse()");
        if(response == null) throw new Exception("Unable to send Server Response: Server response was null");
        if(webSocket == null) throw new Exception("Unable to send Server Response: WebSocket is null, was it initialized?");

        try{
            //sendClientId(storage, webSocket);
            final byte[] serialized = serializer.serialize(response);
            webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
            Logger.trace("Exit sendServerResponse()");
        } catch (Exception e){
            Logger.error(e,"Unable to send Server Response: ");
            throw new Exception("Unable to send Server Response: ", e);
        }
    }

    private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) throws Exception {
        try {
            Logger.trace("Enter handleFrame()");
            Logger.trace("Websocket frame: {}", frame);
            final byte[] packet = frame.binaryData().getBytes();
            final long start = System.nanoTime();
            Logger.info("Deserializing packet recieved");
            Logger.trace("packet: {}", packet);
            final Object deserialized = serializer.deserialize(packet);
            Logger.trace("deserialized: {}", deserialized);
            final long time = System.nanoTime() - start;
            Logger.trace("packet took {} seconds to deserialze.", time);
            sendResponse(deserialized, webSocket);
            Logger.trace("Exit handleFrame()");
        } catch (Exception e){
            Logger.error(e,"Error handling websocket frame.");
            throw new Exception("Error handling websocket frame.", e);
        }
    }

    private void handleTextFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) throws Exception {
        try {
            Logger.trace("Enter handleTextFrame()");

            final String frameText = frame.textData();
            Logger.trace("Websocket text frame: {}", frameText);

            String handlerID = webSocket.binaryHandlerID();
            if(StringUtils.equalsIgnoreCase(frameText, ServerRequest.REQUEST_CLIENT_ID.toString())){
                Logger.info("Sending Client [" + handlerID + "]");
                sendClientId(webSocket, handlerID);
            }

            Logger.trace("Exit handleTextFrame()");
        } catch (Exception e){
            Logger.error(e,"Error handling websocket text frame.");
            throw new Exception("Error handling websocket text frame.", e);
        }
    }

    private void sendResponse(Object deserialized, ServerWebSocket webSocket) throws Exception{
        try{
            Logger.trace("Enter sendResponse()");
            if(deserialized instanceof ClientRequest){
                ClientRequest request = (ClientRequest) deserialized;
                clientRepsonse(webSocket, request);
            }

            if(deserialized instanceof AdminClientRequest){
                AdminClientRequest request = (AdminClientRequest) deserialized;
                adminResponse(webSocket, request);
            }
            Logger.trace("Exit sendResponse()");
        } catch (Exception e){
            Logger.error(e, "Error in sendResponse.");
            throw new Exception("Error handling websocket frame.", e);
        }
    }

    private void clientRepsonse(ServerWebSocket webSocket, ClientRequest request) throws Exception{
        Logger.trace("Enter clientRepsonse()");
        if(serverDaemon == null) throw new Exception("Server Deamon is not running.");
        sendServerResponse(webSocket, serverDaemon.handleClientRequest(request));
        Logger.trace("Exit clientRepsonse()");
    }

    private void adminResponse(ServerWebSocket webSocket, AdminClientRequest request) throws Exception{
        Logger.trace("Enter adminResponse()");
        sendServerResponse(webSocket, handleAdminRequest(request));
        Logger.trace("Exit adminResponse()");
    }

    private ServerResponse handleAdminRequest(AdminClientRequest request) {
            Logger.trace("Enter handleAdminRequest()");
            ServerResponse response = null;

            if(request != null){
                String[] serverPayload = null;

                Logger.debug("request: {}", request.getMessage());

                String message = request.getMessage();
                String sessionId = request.getSessionId();
                int requestSequence = request.getRequestSequence();
                response = new ServerResponse(requestSequence, sessionId, message, serverPayload, getServerId());
            }
            Logger.trace("Exit handleAdminRequest()");
            return response;
    }

    private int getServerId() {
        Logger.trace("Enter getServerId()");
        int serverId = -1;
        if(serverDaemon != null){
            serverId = serverDaemon.getServerId();
        }
        Logger.trace("Exit getServerId()");
        return serverId;
    }
}