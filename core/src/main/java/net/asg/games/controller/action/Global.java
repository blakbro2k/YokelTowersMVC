package net.asg.games.controller.action;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.server.YokelLounge;
import net.asg.games.server.YokelPlayer;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Since this class implements ActionContainer and is annotated with ViewActionContainer, its methods will be reflected
 * and available in all LML templates. Note that this class is a component like any other, so it can inject any fields,
 * use Initiate-annotated methods, etc.
 */
@ViewActionContainer("global")
public class Global implements ActionContainer {
    private WebSocket socket;
    private String message = "Connecting...";
    private boolean isConnected = false;
    private OrderedMap<String, YokelPlayer> players = new OrderedMap<String, YokelPlayer>();
    private OrderedMap<String, YokelLounge> lounges = new OrderedMap<String, YokelLounge>();

    /**
     * This is a mock-up method that does nothing. It will be available in LML templates through "close" (annotation
     * argument) and "noOp" (method name) IDs.
     */
    @LmlAction("close")
    public void noOp() {
    }

    @LmlAction("isDebug")
    public boolean isDebug() {
        return true;
    }

    @LmlAction("initConnection")
    public boolean initializeSockets() throws WebSocketException {
        System.out.println("initializeSockets called");

        // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
        if(socket == null){
            socket = ExtendedNet.getNet().newWebSocket("localhost", 8000);
        }

        socket.addListener(getListener());

        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.

        try{
            socket.connect();
            isConnected = true;
        } catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("socket=" + socket);
        Packets.register(serializer);
        return isConnected;
    }

    @LmlAction("isAlive")
    public boolean isAlive() {
        return isConnected;
    }

    @LmlAction("requestPlayers")
    public void requestPlayers() {
        System.out.println("Starting requestPlayers");
        if(!isAlive()){
            initializeSockets();
        }
        final ClientRequest request = new ClientRequest(-1, "new", ServerRequest.REQUEST_TEST_PLAYER_LIST + "", null);
        socket.send(request);
    }

    @LmlAction("requestLounges")
    public void requestLounges() {
        System.out.println("Starting requestLounges");
        if(!isAlive()){
            initializeSockets();
        }
        final ClientRequest request = new ClientRequest(-1, "new", ServerRequest.REQUEST_GAME_LOUNGE + "", null);
        socket.send(request);
    }

    @LmlAction("requestPlayerRegistration")
    public void requestPlayerRegistration(final Object player) {
        System.out.println("Starting requestPlayers");
        if(!isAlive()){
            initializeSockets();
        }
       // Json json  = new Json();
        System.out.println("player=" + player);
        System.out.println("player=" + player.getClass());
        //System.out.println("pleyr=" + json.toJson(player));
        //final ClientRequest request = new ClientRequest(-1, "state:" + socket.getState().getId(), ServerRequest.REQUEST_LOGIN + "", null);
        //socket.send(request);
        //getPlayers();
    }

    @LmlAction("getPlayers")
    public Array<String> getPlayers() {
        System.out.println("getPlayers");

        if(players == null){
            requestPlayers();
        }
        System.out.println("players" + players);
        return players.orderedKeys();
    }

    @LmlAction("getLoungeTitles")
    public ObjectMap.Values<YokelLounge> getLoungeTitles() {
        System.out.println("getLoungeTitles");

        if(lounges == null){
            requestLounges();
        }
        System.out.println("lounges=" + lounges);
        return lounges.values();
    }

    @LmlAction("getRooms")
    public Array<String> getRooms(String loungeName) {
        System.out.println("getRooms");

        if(lounges == null){
            requestLounges();
        }
        System.out.println("lounges" + lounges);
        return lounges.get(loungeName).getAllRooms().orderedKeys();
    }

    private WebSocketListener getListener() {
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
                case REQUEST_TEST_PLAYER_LIST:
                    buildTestPlayersFromJSON(payload);
                    break;
                case REQUEST_GAME_LOUNGE:
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

                if(player != null){
                    players.put(player.getName(), player);
                }
            }
        }
        System.out.println("players: " + players);
    }

    private void buildLoungeFromJSON(String[] jsonLounges){
        Json json  = new Json();
        for(String jsonLounge : jsonLounges){
            if(!StringUtils.isEmpty(jsonLounge)){
                //System.out.println("jsonPlayer" + jsonPlayer);
                YokelLounge lounge = json.fromJson(YokelLounge.class, jsonLounge);

                if(lounges != null){
                    lounges.put(lounge.getName(), lounge);
                }
            }
        }
        System.out.println("lounges: " + lounges);
    }
}
