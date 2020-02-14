package net.asg.games;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.managers.GameRunner;
import net.asg.games.game.managers.ServerManager;
import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.storage.MemoryStorage;
import net.asg.games.storage.StorageInterface;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.Scanner;

import static net.asg.games.utils.enums.ServerRequest.REQUEST_ALL_DEBUG_PLAYERS;
import static net.asg.games.utils.enums.ServerRequest.REQUEST_LOUNGE_ALL;

/** Launches the server application. */
public class LocalLauncher {
    private final static float MS_PER_UPDATE = 1f;
    private static WebSocket socket;
    private static boolean isConnected = false;
    private static String clientId;
    private static Queue<Request> requests = new Queue<>();

    private class Request{
        private final String message;
        private final Object result;

        Request(String message, Object result){
            this.message = message;
            this.result = result;
        }
    }

    public static void main(final String... args) throws Exception {
        CommonWebSockets.initiate();
        Scanner scanner = new Scanner(System.in);

        // Input from player
        try {
            Logger.trace("Local Launcher Failed: ");
            initializeSockets();

            // Input from player

            // The game logic starts here
            boolean isRunning = true;

            while (isRunning) {
                //screen.PrintScreen();
                // Get input from player and do something
                char input = scanner.nextLine().charAt(0);
                System.out.println("input=" + input);
                System.out.println("isRunning=" + isRunning);

                switch (input) {
                    case 'a':

                        requestLounges();
                        //printStream("requestLounges");
                        break;
                    case 'd':
                        System.out.println("request=");


                        //snake.moveRight(screen, snake);
                        break;
                    case 'w':
                        System.out.println("requedfdst=");

                        //snake.moveUp(screen, snake);
                        break;
                    case 's':
                        System.out.println("reqfdfduest=");
                        isRunning = false;
                        //snake.moveDown(screen, snake);
                        break;
                    case 'q':
                        System.out.println("stop=");
                        throw new Exception("Game Over.");

                }
                System.out.println(isRunning);

            }
            Logger.trace("Local Launcher Failed: ");
        } catch (Exception e) {
            Logger.error(e, "Local Launcher Failed: ");
            throw new Exception("Local Launcher Failed: ", e);
        } finally {
            scanner.close();
        }
    }

    //private static Object

    private static void printStream(String out){
        System.out.println(out);
    }
    private static boolean initializeSockets() throws WebSocketException {
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

        try{
            socket.connect();
            clientId = "1";
            isConnected = true;
        } catch(Exception e){
            e.printStackTrace();
        }
        Packets.register(serializer);
        return isConnected;
    }


    private static WebSocketListener getListener() {
        final WebSocketHandler handler = new WebSocketHandler();
        // Registering ServerResponse handler:
        handler.registerHandler(ServerResponse.class, (WebSocketHandler.Handler<ServerResponse>) (webSocket, packet) -> {
            try {
                System.out.println("jsonPlayer");

                handleServerResponse(packet);
            } catch (Exception e) {
                System.out.println("3packet=" + packet);
                e.printStackTrace();
            }
            return true;
        });
        return handler;
    }
    private static void handleServerResponse(ServerResponse request) throws Exception {
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

    private static void decodePayload(String message, String[] payload) throws Exception {
        String[] load = null;
        System.out.println("decode");
        if(!StringUtils.isEmpty(message)){

            ServerRequest value = ServerRequest.valueOf(message);
            switch (value) {
                case REQUEST_ALL_DEBUG_PLAYERS:
                    System.out.println(REQUEST_ALL_DEBUG_PLAYERS);

                    buildTestPlayersFromJSON(payload);
                    break;
                case REQUEST_LOUNGE_ALL:
                    System.out.println(REQUEST_LOUNGE_ALL);

                    buildLoungeFromJSON(payload);
                    break;
                case REQUEST_LOGIN:
                    break;
                case REQUEST_PLAYER_REGISTER:
                    break;
                case REQUEST_CREATE_GAME:
                    //createNewGame
                    break;
                case REQUEST_PLAY_GAME:
                    break;
                case REQUEST_TABLE_STAND:
                    break;
                case REQUEST_TABLE_JOIN:
                    break;
                case REQUEST_TABLE_SIT:
                    break;
                case REQUEST_ROOM:
                    break;
                case REQUEST_ROOM_JOIN:
                    break;
                case REQUEST_ROOM_LEAVE:
                    break;
                case REQUEST_LOUNGE:
                    break;
                case REQUEST_TABLE_INFO:
                    break;
                default:
                    throw new Exception("Unknown Server Response: " + value);
            }
        }
    }

    private static void buildTestPlayersFromJSON(String[] jsonPlayers){
        Json json  = new Json();

        if(jsonPlayers != null){
            System.out.println("jsonPlayer" + Arrays.asList(jsonPlayers));

            for(String jsonPlayer : jsonPlayers){
                if(!StringUtils.isEmpty(jsonPlayer)){
                    System.out.println("jsonPlayer" + jsonPlayer);
                    YokelPlayer player = json.fromJson(YokelPlayer.class, jsonPlayer);

                    if(player != null){
                        //players.put(player.getName(), player);
                    }
                }
            }
        }
        //System.out.println("players: " + players);
    }

    private static void buildLoungeFromJSON(String[] jsonLounges){
        Json json  = new Json();
        System.out.println("jsonLounges" + Arrays.asList(jsonLounges));

    }
    private static long getCurrentTime(){
        return System.nanoTime();
    }

    private static void processInput(char input){
        System.out.println("input= " + input);
    }

    public static boolean isAlive() {
        return isConnected;
    }

    public static void requestLounges() {
        if(!isAlive()){
            initializeSockets();
        }
        final ClientRequest request = new ClientRequest(-1, clientId, REQUEST_LOUNGE_ALL + "", null);
        System.out.println("Starting requestLounges" + request);

        socket.send(request);
    }
}