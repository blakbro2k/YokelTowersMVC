package net.asg.games.game.managers;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static net.asg.games.utils.enums.ServerRequest.REQUEST_LOUNGE_ALL;

public class ClientManager implements Disposable {
    private static WebSocket socket;
    private static boolean isConnected = false;
    private static String clientId;
    private static int requestId = 0;
    private static Queue<String[]> requests = new Queue<>();

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

    public ClientManager(){
        initializeSockets();
    }

    private boolean initializeSockets() throws WebSocketException {
        //
        System.out.println("initializeSockets called");
        socket = ExtendedNet.getNet().newWebSocket("localhost", 8080);
        //socket.addListener(getListener());
        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.

        socket.connect();
        socket.addListener(getListener());
        //clientId = "1";
        isConnected = true;
        Packets.register(serializer);
        return isConnected;
    }

    public boolean isAlive() {
        return isConnected;
    }

    public void requestLounges() {
        if (!isAlive()) {
            initializeSockets();
        }
        final ClientRequest request = new ClientRequest(++requestId, "1", REQUEST_LOUNGE_ALL + "", null);
        socket.send(request);
    }

    public void handleServerResponse(ServerResponse request) throws Exception {
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

    private void decodePayload(String message, String[] payload) throws Exception {
        String[] load = null;
        if(!StringUtils.isEmpty(message)){
            ServerRequest value = ServerRequest.valueOf(message);
            switch (value) {
                case REQUEST_LOUNGE_ALL:
                    requests.addFirst(payload);
                    break;
                case REQUEST_CREATE_GAME:
                    //createNewGame
                    break;
                default:
                    throw new Exception("Unknown Server Response: " + value);
            }
        }
    }

    private WebSocketListener getListener() {
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
}
