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

import java.util.concurrent.TimeUnit;

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

    public void setPlayer(YokelPlayer player){
        if(player != null){
            this.player = player;
        }
    }

    public YokelPlayer getPlayer(){
        return this.player;
    }

    public boolean register(YokelPlayer player) throws InterruptedException {
        setPlayer(player);
        boolean register = initializeSockets();
        registerClient();
        return register;
    }

    private boolean initializeSockets() throws WebSocketException, InterruptedException {
        System.out.println("initializeSockets called");
        socket = ExtendedNet.getNet().newWebSocket(host, port);
        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.

        socket.connect();
        socket.addListener(getServerListener());
        Packets.register(serializer);

        socket.send(ServerRequest.REQUEST_CLIENT_ID.toString());


        waitForRequest(30);
        String[] request = getRequests().removeFirst();
        clientId = request[0];

        isConnected = true;
        System.out.println("initializeSockets ended");
        return isConnected;
    }

    private void registerClient() throws InterruptedException {
        requestPlayerRegister(player);
        waitForRequest(30);
    }

    public boolean isAlive() {
        //TODO: Implement ping test interval
        //internal interval, if hit.
        //do a test connection
        //
        return isConnected;
    }

    public void requestLounges() throws InterruptedException {
        sendClientRequest(ServerRequest.REQUEST_LOUNGE_ALL, null);
    }

    public void requestPlayers() throws InterruptedException {
        sendClientRequest(ServerRequest.REQUEST_ALL_REGISTERED_PLAYERS, null);
    }

    public void requestPlayerRegister(YokelPlayer player) throws InterruptedException {
        sendClientRequest(ServerRequest.REQUEST_PLAYER_REGISTER, PayloadUtil.createPlayerRegisterRequest(clientId, player));
    }

    public void requestJoinRoom(YokelPlayer player, String loungeName, String roomName) throws InterruptedException {
        sendClientRequest(ServerRequest.REQUEST_ROOM_JOIN, PayloadUtil.createJoinRoomRequest(player, loungeName, roomName));
    }

    public void requestCreateGame(String loungeName, String roomName, YokelTable.ACCESS_TYPE type, boolean isRated) throws InterruptedException {
        sendClientRequest(ServerRequest.REQUEST_CREATE_GAME, PayloadUtil.createNewGameRequest(loungeName, roomName, type, isRated));
    }

    public void requestTableSit(YokelPlayer player, String loungeName, String roomName, int tableNumber, int seatNumber) throws InterruptedException {
        sendClientRequest(ServerRequest.REQUEST_TABLE_SIT, PayloadUtil.createTableSitRequest(player, loungeName, roomName, tableNumber, seatNumber));
    }

    public void handleServerResponse(ServerResponse request) {
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

    private WebSocketListener getServerListener() {
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

    private void checkConnection() throws InterruptedException {
        if (!isAlive()) {
            initializeSockets();
        }
    }

    private void sendClientRequest(ServerRequest serverRequest, String[] payload) throws InterruptedException {
        checkConnection();
        final ClientRequest request = new ClientRequest(++requestId, "1", serverRequest.toString(), payload);
        socket.send(request);
    }
}
