package net.asg.games.game.managers;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static net.asg.games.utils.enums.ServerRequest.REQUEST_CLIENT_ID;

public class ClientManager implements Disposable {
    private static WebSocket socket;
    private static boolean isConnected;
    private static String clientId = "";
    private static int requestId = 0;
    private static Queue<String[]> requests;
    private final String host;
    private final int port;
    private YokelPlayer player;

    public Queue<String[]> getRequests() {
        return requests;
    }

    @Override
    public void dispose() {
        socket.close();
    }

    public boolean isRunning(){
        return isConnected;
    }

    public ClientManager(String host, int port){
        isConnected = false;
        requests = new Queue<>();
        this.host = host;
        this.port = port;
    }

    public boolean authenticate(YokelPlayer player){
        this.player = player;
        return initializeSockets();
    }

    private boolean initializeSockets() throws WebSocketException {
        System.out.println("initializeSockets called");
        socket = ExtendedNet.getNet().newWebSocket(host, port);
        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.

        socket.connect();
        socket.addListener(getListener());
        socket.send(REQUEST_CLIENT_ID + "");

        /*
        try {
            waitForRequest(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //String[] request = getRequests().removeFirst();
        //System.out.println(Arrays.toString(request));
        //clientId = request[0];
        isConnected = true;
        Packets.register(serializer);
        System.out.println("initializeSockets ended");
        return isConnected;
    }

    public boolean isAlive() {
        //TODO: Implement ping test interval
        //internal interval, if hit.
        //do a test connection
        //
        return isConnected;
    }

    public void requestLounges() {
        sendClientRequest(ServerRequest.REQUEST_LOUNGE_ALL, null);
    }

    public void requestPlayers() {
        sendClientRequest(ServerRequest.REQUEST_ALL_REGISTERED_PLAYERS, null);
    }

    public void requestPlayerRegister(YokelPlayer player) {
        sendClientRequest(ServerRequest.REQUEST_PLAYER_REGISTER, PayloadUtil.createPlayerRegisterRequest(clientId, player));
    }

    public void requestJoinRoom(YokelPlayer player, String loungeName, String roomName) {
        sendClientRequest(ServerRequest.REQUEST_ROOM_JOIN, PayloadUtil.createJoinRoomRequest(player, loungeName, roomName));
    }

    public void requestCreateGame(String loungeName, String roomName, YokelTable.ACCESS_TYPE type, boolean isRated) {
        sendClientRequest(ServerRequest.REQUEST_CREATE_GAME, PayloadUtil.createNewGameRequest(loungeName, roomName, type, isRated));
    }

    public void requestTableSit(YokelPlayer player, String loungeName, String roomName, int tableNumber, int seatNumber) {
        sendClientRequest(ServerRequest.REQUEST_TABLE_SIT, PayloadUtil.createTableSitRequest(player, loungeName, roomName, tableNumber, seatNumber));
    }

    public void handleServerResponse(ServerResponse request) {
        System.out.println("request: " + request);

        String sessionId = null;
        String message = null;
        int requestSequence = -1;
        String[] payload = null;

        if(request != null){
            message = request.getMessage();
            sessionId = request.getSessionId();
            requestSequence = request.getRequestSequence();
            payload = request.getPayload();
            decodePayload(message, payload);
        }
    }

    private void decodePayload(String message, String[] payload) {
        if(!StringUtils.isEmpty(message)){
            requests.addFirst(payload);
        }
    }

    private WebSocketListener getListener() {
        Logger.trace("");
        final WebSocketHandler handler = new WebSocketHandler();
        // Registering ServerResponse handler:
        handler.registerHandler(ServerResponse.class, (WebSocketHandler.Handler<ServerResponse>) (webSocket, packet) -> {
            try {

                handleServerResponse(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        });
        return handler;
    }

    public void waitForRequest(int maxWait, int numberOfRequests) throws InterruptedException {
        int timeout = 0;
        boolean waiting = true;
        int targetSize = requests.size + numberOfRequests;

        while(waiting){
            if(requests.size >= targetSize){
                waiting = false;
            } else {
                if(timeout > maxWait){
                    throw new WebSocketException("Timed out waiting for WebSocket Request.");
                } else {
                    TimeUnit.SECONDS.sleep(1);
                    ++timeout;
                }
            }
        }
    }

    public void waitForRequest(int maxWait) throws InterruptedException {
        waitForRequest(maxWait, 1);
    }

    private void checkConnection() {
        if (!isAlive()) {
            initializeSockets();
        }
    }

    private void sendClientRequest(ServerRequest serverRequest, String[] payload) {
        checkConnection();
        final ClientRequest request = new ClientRequest(++requestId, "1", serverRequest + "", payload);
        socket.send(request);
    }
}
