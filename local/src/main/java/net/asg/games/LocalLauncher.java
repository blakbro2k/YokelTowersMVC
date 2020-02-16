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

import net.asg.games.game.managers.ClientManager;
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

/**
 * Launches the server application.
 */
public class LocalLauncher {
    private static WebSocket socket;
private static boolean isConnected;
    private static ClientManager client = new ClientManager();

    public static void main(final String... args) throws Exception {
        CommonWebSockets.initiate();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        try{
            // Input from player
            Logger.trace("Local Launcher Failed: ");
            initializeSockets();

            // Input from player
            // The game logic starts here
            while (isRunning) {
                //screen.PrintScreen();
                // Get input from player and do something
                System.out.println("Queue=" + client.getRequests());
                System.out.println("Enter command:");
                char input = scanner.nextLine().charAt(0);

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
                        //snake.moveDown(screen, snake);
                        break;
                    case 'q':
                        isRunning = false;
                        break;

                }
            }
        } catch(Exception e){
            Logger.trace("Local Launcher Failed: ");
            e.printStackTrace();
        } finally {
            scanner.close();
            socket.close();
        }
    }

    //private static Object

    private static void printStream(String out) {
        System.out.println(out);
    }

    private static boolean initializeSockets() throws WebSocketException {
        //
        System.out.println("initializeSockets called");
        socket = ExtendedNet.getNet().newWebSocket("localhost", 8000);
        //socket.addListener(getListener());
        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.

        try {
            socket.connect();
            //clientId = "1";
            isConnected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Packets.register(serializer);
        return isConnected;
    }


    private static WebSocketListener getListener() {
        final WebSocketHandler handler = new WebSocketHandler();
        // Registering ServerResponse handler:
        System.out.println("adding listener");

        handler.registerHandler(ServerResponse.class, (WebSocketHandler.Handler<ServerResponse>) (webSocket, packet) -> {
            try {

                client.handleServerResponse(packet);
            } catch (Exception e) {
                System.out.println("3packet=" + packet);
                e.printStackTrace();
            }
            return true;
        });
        return handler;
    }



    public static boolean isAlive() {
        return isConnected;
    }

    public static void requestLounges() {
        if (!isAlive()) {
            initializeSockets();
        }
        final ClientRequest request = new ClientRequest(-1, "1", REQUEST_LOUNGE_ALL + "", null);
        System.out.println("Starting requestLounges" + request);

        socket.send(request);
    }
}