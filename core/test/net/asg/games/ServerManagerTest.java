package net.asg.games;

import com.github.czyzby.websocket.WebSocket;

import net.asg.games.game.ServerManager;
import net.asg.games.utils.TestingUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerManagerTest {
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
        Class<?>[] args = new Class<?>[1];
        args[0] = String.class;
        Object[] params = new Object[1];
        params[0] = "TestCreated";
        System.out.println("Object=" + TestingUtils.invokeStaticMethod(daemon.getClass(),
                "createLounge", args,params,daemon));

        Object[] params2 = new Object[1];
        params[0] = "";
        System.out.println("Object=" + TestingUtils.invokeStaticMethod(daemon.getClass(),
                "createLounge", args,params2,daemon));
        //private YokelLounge createLounge(String loungeName)
        throw new Exception("Test Case not Set");
    }
}