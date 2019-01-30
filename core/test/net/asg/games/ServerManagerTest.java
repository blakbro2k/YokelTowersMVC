package net.asg.games;

import com.github.czyzby.websocket.WebSocket;

import net.asg.games.game.ServerManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.utils.TestingUtils;
import net.asg.games.utils.Util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.pmw.tinylog.Level;

import java.lang.reflect.InvocationTargetException;

public class ServerManagerTest {
    private WebSocket socket;
    private ServerManager daemon;

    @Before
    public void startDaemon() {
        String[] args = {ServerManager.LOG_LEVEL_ATTR, "info", ServerManager.DEBUG_ATTR};
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

        YokelLounge expected = new YokelLounge("TestCreated");
        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod,args,params,daemon));
        Assert.assertEquals(expected,TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon));

        Object[] params2 = new Object[1];
        params[0] = "";
        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod,args,params2,daemon));
        Assert.assertNull(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params2,daemon));
    }

    private Class<?> daemonClass(){
        if(daemon != null){
            return daemon.getClass();
        }
        return null;
    }

    @Test
    public void portTest() {
        if(daemon != null){
            daemon.setPort(5050);
            Assert.assertEquals(5050, daemon.getPort());
        }
    }

    @Test
    public void debugTest() {
        if(daemon != null){
            daemon.setDebug(true);
            Assert.assertTrue( daemon.getDebug());
            daemon.setDebug(false);
            Assert.assertFalse( daemon.getDebug());
        }
    }

    @Test
    public void logTest() {
        if(daemon != null){
            daemon.setLogLevel("warn");
            Assert.assertEquals(Level.WARNING, daemon.getLogLevel());
            daemon.setLogLevel("error");
            Assert.assertEquals(Level.ERROR, daemon.getLogLevel());
            daemon.setLogLevel("debug");
            Assert.assertEquals(Level.DEBUG, daemon.getLogLevel());
            daemon.setLogLevel("trace");
            Assert.assertEquals(Level.TRACE, daemon.getLogLevel());
        }
    }

    @Test
    public void tickRateTest() {
        if(daemon != null){
            daemon.setTickRate(567);
            Assert.assertEquals(567, daemon.getTickRate(),0);
        }
    }


    @Test
    public void registerPlayerTest() throws InvocationTargetException {
        YokelPlayer player1 = new YokelPlayer("blakbro2k");
        String[] payload = new String[1];
        String[] tooBig = new String[2];

        payload[0] = player1.toString();

        String testingMethod = "getPlayerFromPayload";
        Class<?>[] args = new Class<?>[1];
        args[0] = String[].class;
        Object[] params = new Object[1];

        params[0] = tooBig;
        Assert.assertNull(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon));
        params[0] = null;
        Assert.assertNull(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon));
        params[0] = payload;
        Assert.assertEquals(player1, TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon));

        testingMethod = "registerPlayerRequest";
        args[0] = YokelPlayer.class;
        params[0] = null;
        Assert.assertFalse(parsePayloadBoolean(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon)));
        params[0] = player1;
        Assert.assertTrue(parsePayloadBoolean(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon)));
        Assert.assertTrue(parsePayloadBoolean(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon)));
        //YokelPlayer player2 = new YokelPlayer();
        //player2.setSessionId();
    }

    private boolean parsePayloadBoolean(Object payload){
        if(payload instanceof String[]){
            String[] gotten = (String[]) payload;
            return Boolean.parseBoolean(Util.otos(gotten[0]));
        }
        return false;
    }
}

/*
    private String[] registerPlayerRequest(YokelPlayer player){
        String[] ret = new String[1];
        ret[0] = "false";
        if(player != null){
            validateRegisteredPlayers();
            String playerId = player.getPlayerId();
            if(!registeredPlayers.containsKey(playerId)){
                registeredPlayers.put(playerId, player);
                ret[0] = "true";
            } else {
                YokelPlayer regPlayer = registeredPlayers.get(playerId);
                ret[0] = StringUtils.equalsIgnoreCase(player.getName(), regPlayer.getName()) + "";
            }
        }
        return ret;
    }
    */