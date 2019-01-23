package net.asg.games;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.ServerManager;
import net.asg.games.server.serialization.Packets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
    private WebSocket socket;
    private ServerManager daemon;

    @Before
    public void startDaemon() {
        this.daemon = new ServerManager();
    }

    @After
    public void stopDaemon() {
        daemon.shutDownServer(-1);
    }

    @Test
    public void createLoungeTest() throws Exception {
        throw new Exception("Test Case not Set");
    }
}