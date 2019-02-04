package net.asg.games;

import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.websocket.WebSocket;

import net.asg.games.game.ServerManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.utils.TestingUtils;
import net.asg.games.utils.Util;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;

public class ServerManagerTest {
    private WebSocket socket;
    private static ServerManager daemon;

    @BeforeClass
    public static void startDaemon() {
        String[] args = {ServerManager.LOG_LEVEL_ATTR, "trace", ServerManager.DEBUG_ATTR};
        daemon = new ServerManager(args);
    }

    @AfterClass
    public static void stopDaemon() {
        daemon.shutDownServer(-1);
    }

    @Test
    public void createLoungeTest() throws Exception {
        //startDaemon();
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

    @Test
    public void getLoungeTest() throws Exception {
        //startDaemon();
        String testLoungeName = "TestCreated";
        YokelLounge expected = new YokelLounge(testLoungeName);

        String testingMethod2 = "getLounge";
        Class<?>[] args = new Class<?>[1];
        args[0] = String.class;
        Object[] params = new Object[1];
        params[0] = testLoungeName;

        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod2,args,params,daemon));
        Assert.assertEquals(expected,TestingUtils.invokeStaticMethod(daemonClass(),testingMethod2,args,params,daemon));
    }

    @Test
    public void getAllLoungesTest() throws Exception {
        OrderedMap<String, YokelLounge> lounges = new OrderedMap<>();

        Logger.trace("Enter generateDefaultLounges()");

        Logger.info("Creating Social: Eiffel Tower");
        YokelLounge socialLounge = new YokelLounge(YokelLounge.SOCIAL_GROUP);
        YokelRoom room1 = new YokelRoom("Eiffel Tower");
        socialLounge.addRoom(room1);

        Logger.info("Creating Social: Leaning Tower of Pisa");
        YokelRoom room3 = new YokelRoom("Leaning Tower of Pisa");
        socialLounge.addRoom(room3);

        Logger.info("Creating Beginning: Chang Tower");
        YokelLounge beginningLounge = new YokelLounge(YokelLounge.BEGINNER_GROUP);
        YokelRoom room2 = new YokelRoom("Chang Tower");
        beginningLounge.addRoom(room2);

        YokelLounge TestLounge = new YokelLounge("TestCreated");


        lounges.put(YokelLounge.SOCIAL_GROUP,socialLounge);
        lounges.put(YokelLounge.BEGINNER_GROUP,beginningLounge);
        lounges.put("TestCreated",TestLounge);

        String testingMethod = "getAllLounges";
        Class<?>[] args = new Class<?>[0];
        //args[0] = String.class;
        Object[] params = new Object[0];
        //params[0] = "getAllLounges";
        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod,args,params,daemon));
        Assert.assertEquals(Util.getValuesArray(lounges.values()), TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon));
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

        String testingMethod = "getRegisterPlayerFromPayload";
        Class<?>[] args = new Class<?>[1];
        args[0] = String[].class;
        Object[] params = new Object[1];

        params[0] = tooBig;
        TestingUtils.TestMethod getRegisterPlayerFromPayload = new TestingUtils.TestMethod("getRegisterPlayerFromPayload",daemonClass(),args,params,daemon);

        Assert.assertNull(TestingUtils.invokeMethod(getRegisterPlayerFromPayload,true));
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
    }

    private boolean parsePayloadBoolean(Object payload){
        if(payload instanceof String[]){
            String[] gotten = (String[]) payload;
            return Boolean.parseBoolean(Util.otos(gotten[0]));
        }
        return false;
    }

    @Test
    public void getRoomTest() throws Exception {
        String expectedRoomName1 = "Eiffel Tower";
        String expectedRoomName2 = "Chang Tower";
        YokelRoom room1 = new YokelRoom(expectedRoomName1);
        YokelRoom room2 = new YokelRoom(expectedRoomName2);

        Assert.assertEquals(room1, daemon.getRoom(YokelLounge.SOCIAL_GROUP, expectedRoomName1));
        Assert.assertNull(daemon.getRoom(YokelLounge.SOCIAL_GROUP, expectedRoomName2));
        Assert.assertEquals(room2, daemon.getRoom(YokelLounge.BEGINNER_GROUP, expectedRoomName2));
        Assert.assertNull(daemon.getRoom(YokelLounge.SOCIAL_GROUP, null));
        Assert.assertNull(daemon.getRoom(null, expectedRoomName2));
    }
}