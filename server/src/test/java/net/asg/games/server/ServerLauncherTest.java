package net.asg.games.server;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.server.serialization.Packets;

import org.junit.BeforeClass;

public class ServerLauncherTest {
    private WebSocket socket;

    @BeforeClass
    public boolean initializeSockets() throws WebSocketException {
        System.out.println("initializeSockets called");

        // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
        if(socket == null){
            socket = ExtendedNet.getNet().newWebSocket("localhost", 8000);
        }

        //socket.addListener(getListener());

        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.

        try{
            socket.connect();
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }

        System.out.println("socket=" + socket);
        Packets.register(serializer);
        return true;
    }
}