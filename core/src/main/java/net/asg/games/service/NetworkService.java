package net.asg.games.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.NetworkUtil;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

@Component
public class NetworkService {
    private static final Logger LOGGER = LoggerService.forClass(NetworkService.class);

    // @Inject-annotated fields will be automatically filled by the context initializer.
    @Inject private InterfaceService interfaceService;

    private WebSocket socket = null;
    private String message = "Connecting...";
    private OrderedMap<String, Object> serverResponse = new OrderedMap<String, Object>();

    public boolean initializeSockets() throws WebSocketException {
        //TODO: Create Unique client ID
        //TODO: Create PHPSESSION token
        //TODO: Create CSRF Token

        LOGGER.debug("Enter initializeSockets()");

        LOGGER.info("Listening for server on //" + getServerHost() + ":" + getServerPort());
        // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
        if(socket == null){
            socket = ExtendedNet.getNet().newWebSocket(getServerHost(), getServerPort());
        }

        socket.addListener(getClientListener());

        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.

        try{
            socket.connect();
        } catch(Exception e){
            LOGGER.error(e,"Error initializeSockets");
            e.printStackTrace();
        } finally {
            socket.close();
        }

        Packets.register(serializer);
        LOGGER.debug("Exit initializeSockets()");
        return isConnected();
    }

    public int getServerPort(){
        return 8000;
    }


    public String getServerHost(){
        return "localHost";
    }

    public boolean isInternetAvailable(){
        return NetworkUtil.isInternetAvailable();
    }

    private WebSocketListener getClientListener() {
        final WebSocketHandler handler = new WebSocketHandler();
        // Registering ServerResponse handler:
        handler.registerHandler(ServerResponse.class, new WebSocketHandler.Handler<ServerResponse>() {
            @Override
            public boolean handle(final WebSocket webSocket, final ServerResponse packet) {
                try {
                    handleServerResponse(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        //handler.registerHandler(ServerResponse.class, new ServerResponseHandler());

        return handler;
    }

    private void handleServerResponse(ServerResponse request) throws Exception {
        String sessionId = null;
        String message = null;
        int requestSequence = -1;
        String[] payload = null;

        if(request != null){
            System.out.println("Received: " + request);
            message = request.getMessage();
            sessionId = request.getSessionId();
            requestSequence = request.getRequestSequence();
            payload = request.getPayload();
            decodePayload(message, payload);
        }
    }

    private void decodePayload(String message, String[] payload) throws Exception {
        String[] load = null;
        if(!StringUtils.isEmpty(message)){
            ServerRequest value = ServerRequest.valueOf(message);
            switch (value) {
                case REQUEST_ALL_DEBUG_PLAYERS:
                    buildTestPlayersFromJSON(payload);
                    //push <UpdatePlayerTable, playerList>
                    break;
                case REQUEST_LOUNGE_ALL:
                    buildLoungeFromJSON(payload);
                    break;
                default:
                    throw new Exception("Unknown Server Response: " + value);
            }
        }
    }

    private void buildTestPlayersFromJSON(String[] jsonPlayers){
        Json json  = new Json();
        for(String jsonPlayer : jsonPlayers){
            if(!StringUtils.isEmpty(jsonPlayer)){
                //System.out.println("jsonPlayer" + jsonPlayer);
                YokelPlayer player = json.fromJson(YokelPlayer.class, jsonPlayer);

                //if(players != null){
                    //players.put(player.getName(), player);
                //}
            }
        }
        //System.out.println("players: " + players);
    }

    private void buildLoungeFromJSON(String[] jsonLounges){
        Json json  = new Json();
        for(String jsonLounge : jsonLounges){
            if(!StringUtils.isEmpty(jsonLounge)){
                //System.out.println("jsonPlayer" + jsonPlayer);
                YokelLounge lounge = json.fromJson(YokelLounge.class, jsonLounge);
                //TODO: implement a way for handled responses be queued for processing.
                //if(lounges != null){
                    //lounges.put(lounge.getName(), lounge);
               // }
            }
        }
        //System.out.println("lounges: " + lounges);
    }

    public void requestDebugPlayersFromServer() {
        System.out.println("Starting requestPlayers");
        sendClientRequest(new ClientRequest(-1, "new", ServerRequest.REQUEST_ALL_DEBUG_PLAYERS + "", null));
    }

    private void sendClientRequest(ClientRequest request){
        LOGGER.debug("Enter sendClientRequest()");

        if(request != null){
            if(!isConnected()){
                initializeSockets();
            }
            LOGGER.info("Sending client request to server");
            socket.send(request);
        }
        LOGGER.debug("Enter sendClientRequest()");
    }

    public boolean isConnected() {
        return socket == null;
    }

    private class ServerResponseHandler implements WebSocketHandler.Handler<ServerResponse>{
        @Override
        public boolean handle(final WebSocket webSocket, final ServerResponse packet) {
            LOGGER.info("Handling a response from server");

            try {
                handleServerResponse(packet);
                LOGGER.info("Response fully handled");
                return WebSocketHandler.FULLY_HANDLED;
            } catch (Exception e) {
                LOGGER.error(e, "Error handling server response");
                LOGGER.info("Response not fully handled");
                return WebSocketHandler.NOT_HANDLED;
            }
        }
    }
}