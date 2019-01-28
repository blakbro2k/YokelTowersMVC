package net.asg.games;

import com.github.czyzby.websocket.WebSocket;

import net.asg.games.game.ServerManager;
import net.asg.games.utils.TestingUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;


public class ServerManagerTest {
    private WebSocket socket;
    private ServerManager daemon;

    @Before
    public void startDaemon() {
        String[] args = {ServerManager.LOG_LEVEL_ATTR, "trace", ServerManager.DEBUG_ATTR};
        this.daemon = new ServerManager(args);
    }

    @After
    public void stopDaemon() {
        daemon.shutDownServer(-1);
    }

    @Test
    public void createLoungeTest() throws Exception {
        startDaemon();
        String testingMethod = "createLounge";
        Class<?>[] args = new Class<?>[1];
        args[0] = String.class;
        Object[] params = new Object[1];
        params[0] = "TestCreated";
        printInvokeResult("Lounge",daemonClass(),testingMethod,args,params,daemon);

        Object[] params2 = new Object[1];
        params[0] = "";
        printInvokeResult("Lounge",daemonClass(),testingMethod,args,params2,daemon);
    }

    @Test
    public void printLoungeTest() throws Exception {
        String testingMethod = "printLounges";
        Class<?>[] args = new Class<?>[0];
        //args[0] = String.class;
        printInvokeResult("Lounges",daemonClass(),testingMethod,args,null,daemon);
    }

    private Class<?> daemonClass(){
        if(daemon != null){
            return daemon.getClass();
        }
        return null;
    }

    private Object attemptInvoke(Class<?> clazz, String method, Class<?>[] types, Object[] params, Object o) throws InvocationTargetException {
        return TestingUtils.invokeStaticMethod(clazz,method,types,params,o);
    }

    private void printInvokeResult(String printName, Class<?> clazz, String method, Class<?>[] types, Object[] params, Object o) throws InvocationTargetException {
        System.out.println(printName + "=" + attemptInvoke(clazz,method,types,params,o));
    }
}