package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketHandler.Handler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.enums.ServerRequest;

@View(id = "debug", value = "ui/templates/debug.lml")
public class DebugController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    private WebSocket socket;
    private String message = "Connecting...";
    private boolean isConnected = false;

    public void initializeSockets() throws WebSocketException {
        System.out.println("initializeSockets called");
        // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
        socket = ExtendedNet.getNet().newWebSocket("localhost", 8000);
        socket.addListener(getListener());
        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        // Connecting with the server.
        socket.connect();

        System.out.println("socket=" + socket);
        Packets.register(serializer);
    }

    @Override
    public void render(Stage stage, float delta) {
        if (!isConnected) {
            initializeSockets();
            isConnected = true;
        }
        stage.act(delta);
        stage.draw();

        //
    }

    @LmlAction("getPlayerList")
    public void getPlayerList() {
        final ClientRequest request = new ClientRequest(-1, "new", ServerRequest.GET_PLAYER_LIST + "");
        socket.send(request);
    }

    private WebSocketListener getListener() {
        final WebSocketHandler handler = new WebSocketHandler();
        // Registering ServerResponse handler:
        handler.registerHandler(ServerResponse.class, new Handler<ServerResponse>() {
            @Override
            public boolean handle(final WebSocket webSocket, final ServerResponse packet) {
                System.out.println("Received: " + packet + " : " + packet.getMessage());
                return true;
            }
        });
        return handler;
    }
}
