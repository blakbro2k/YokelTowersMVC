package net.asg.games;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.websocket.WebSocket;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.managers.ServerManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.storage.YokelMemoryStorage;
import net.asg.games.storage.YokelStorageAdapter;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.TestingUtils;
import net.asg.games.utils.YokelUtilities;
import net.asg.games.utils.enums.ServerRequest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.testng.annotations.DataProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ServerManagerTest {
    private WebSocket socket;
    private static ServerManager daemon;

    @BeforeClass
    public static void startDaemon() {
        String[] args = {ServerManager.LOG_LEVEL_ATTR, "trace", ServerManager.DEBUG_ATTR};
        YokelStorageAdapter storage = new YokelMemoryStorage();
        daemon = new ServerManager(storage, args);
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
        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod,args,params,daemon, true));
        Assert.assertEquals(expected,TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon, true));

        Object[] params2 = new Object[1];
        params[0] = "";
        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod,args,params2,daemon, true));
        Assert.assertNull(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params2,daemon, true));
    }

    @Test
    public void getLoungeInvokeTest() throws Exception {
        //startDaemon();
        String testLoungeName = "TestCreated";
        YokelLounge expected = new YokelLounge(testLoungeName);

        String testingMethod2 = "getLounge";
        Class<?>[] args = new Class<?>[1];
        args[0] = String.class;
        Object[] params = new Object[1];
        params[0] = testLoungeName;

        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod2,args,params,daemon, true));
        Assert.assertEquals(expected,TestingUtils.invokeStaticMethod(daemonClass(),testingMethod2,args,params,daemon, true));
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
        System.out.println(TestingUtils.printTestMethod(daemonClass(),testingMethod,args,params,daemon, true));
        Assert.assertEquals(lounges.values(), TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon, true));
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
        getRegisterPlayerFromPayload.setPrivate(true);

        Assert.assertNull(getRegisterPlayerFromPayload.invoke());
        System.out.println("NNN:" + getRegisterPlayerFromPayload);

        params[0] = null;
        getRegisterPlayerFromPayload.setParameterValues(params[0]);
        Assert.assertNull(getRegisterPlayerFromPayload.invoke());

        params[0] = payload;
        getRegisterPlayerFromPayload.setParameterValues(params[0]);
        Assert.assertEquals(player1, getRegisterPlayerFromPayload.invoke());

        testingMethod = "registerPlayerRequest";
        args[0] = String[].class;
        params[0] = null;

        Assert.assertFalse(parsePayloadBoolean(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon, true)));
        params[0] = new String[]{player1.toString()};
        Assert.assertTrue(parsePayloadBoolean(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon, true)));
        Assert.assertFalse(parsePayloadBoolean(TestingUtils.invokeStaticMethod(daemonClass(),testingMethod,args,params,daemon, true)));
    }

    private boolean parsePayloadBoolean(Object payload){
        if(payload instanceof String[]){
            String[] gotten = (String[]) payload;
            return Boolean.parseBoolean(YokelUtilities.otos(gotten[0]));
        }
        return false;
    }

    @Test
    public void internalServerFunctionalTest() throws Exception {
        String expectedRoomName1 = "Eiffel Tower";
        String expectedRoomName2 = "Chang Tower";
        YokelRoom room1 = new YokelRoom(expectedRoomName1);
        YokelRoom room2 = new YokelRoom(expectedRoomName2);

        //private YokelLounge getLounge(String key) throws Exception
        YokelLounge expectedLounge = new YokelLounge(YokelLounge.BEGINNER_GROUP);
        TestingUtils.TestMethod getLoungeTestMethod = new TestingUtils.TestMethod("getLounge",
                daemonClass(),
                new Class[]{String.class},
                new String[]{null},
                daemon,
                true);

        //Null Test
        Assert.assertNull(getLoungeTestMethod.invoke());
        getLoungeTestMethod.setParameterValues(YokelLounge.BEGINNER_GROUP);
        Assert.assertNotNull(getLoungeTestMethod.invoke());
        YokelLounge testingLounge = (YokelLounge) getLoungeTestMethod.invoke();
        Assert.assertEquals(expectedLounge.getName(), testingLounge.getName());

        //private YokelLounge createLounge(String loungeName) throws Exception{
        YokelLounge testLounge = new YokelLounge("TestLounge");
        TestingUtils.TestMethod createLoungeTestMethod = new TestingUtils.TestMethod("createLounge",
                daemonClass(),
                new Class[]{String.class},
                new String[]{null},
                daemon,
                true);
        //Null Test
        Assert.assertNull(createLoungeTestMethod.invoke());
        createLoungeTestMethod.setParameterValues(testLounge.getName());
        Assert.assertNotNull(createLoungeTestMethod.invoke());
        testingLounge = (YokelLounge) createLoungeTestMethod.invoke();
        Assert.assertEquals(testLounge.getName(), testingLounge.getName());

        //private Array<YokelLounge> getAllLounges(){
        TestingUtils.TestMethod getAllLoungesTestMethod = new TestingUtils.TestMethod("getAllLounges",
                daemonClass(),
                null,
                null,
                daemon,
                true);
        //Null Test
        Assert.assertNotNull(getAllLoungesTestMethod.invoke());
        Array<YokelLounge> lounges = (Array<YokelLounge>) getAllLoungesTestMethod.invoke();
        System.out.println("test return=" + getAllLoungesTestMethod.returnType());



        //private Array<YokelRoom> getAllRooms(String loungeName) throws Exception {
                /*

    private Array<YokelTable> getAllTables(String loungeName, String roomName) throws Exception {
    private Array<YokelPlayer> getAllRoomPlayers(String loungeName, String roomName) throws Exception {
    private YokelRoom getRoom(String loungeName, String roomName) throws Exception {
    private YokelTable getTable(String loungeName, String roomName, int tableNumber) throws Exception {
    private YokelSeat getSeat(String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
    private boolean joinLeaveRoom(YokelPlayer player, String loungeName, String roomName, boolean isJoin) throws Exception {
    private boolean sitAtTable(YokelPlayer player, String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
    private boolean standAtTable(String loungeName, String roomName, int tableNumber, int seatNumber) throws Exception {
    private boolean registerPlayer(YokelPlayer player){
    private YokelPlayer getRegisteredPlayer(String playerId){
         */
    }

    @Test
    public void serverRequestsTest() throws Exception {
        daemon.setLogLevel("trace");
        System.out.println("log=" + daemon.getLogLevel());
        YokelPlayer player1 = new YokelPlayer("blakbro2k");
        YokelPlayer player2 = new YokelPlayer("lholtham");
        String roomName = "Eiffel Tower";

        //Assert String contains names
        //System.out.println(Arrays.toString(PayloadUtil.createPlayerRegisterRequest(player1)));
        //System.out.println(Arrays.toString(PayloadUtil.createPlayerRegisterRequest(player2)));

        TestingUtils.TestMethod
                buildPayload = new TestingUtils.TestMethod("buildPayload",
                daemonClass(),
                new Class[]{String.class, String[].class},
                new Object[]{YokelUtilities.otos(ServerRequest.REQUEST_PLAYER_REGISTER), PayloadUtil.createPlayerRegisterRequest("", player1)},
                daemon,
                true);

        /*
                    case REQUEST_LOGIN:
                    case REQUEST_ALL_DEBUG_PLAYERS:
                    #case REQUEST_PLAYER_REGISTER:
                    #case REQUEST_CREATE_GAME:
                    #case REQUEST_PLAY_GAME:
                    #case REQUEST_TABLE_STAND:
                    #case REQUEST_TABLE_JOIN:
                    #case REQUEST_TABLE_SIT:
                    case REQUEST_ROOM:
                    #case REQUEST_ROOM_JOIN:
                    case REQUEST_ROOM_LEAVE:
                    #case REQUEST_TABLE_INFO:
                    case REQUEST_LOUNGE:
                    case REQUEST_LOUNGE_ALL:
         */
        Object result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_PLAYER_REGISTER), PayloadUtil.createPlayerRegisterRequest("", player2));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_ROOM_JOIN), PayloadUtil.createJoinLeaveRoomRequest(player1, YokelLounge.SOCIAL_GROUP, roomName));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_CREATE_GAME), PayloadUtil.createNewGameRequest(YokelLounge.SOCIAL_GROUP, roomName, YokelTable.ACCESS_TYPE.PUBLIC, false));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_TABLE_JOIN), PayloadUtil.createTableJoinRequest(player1, YokelLounge.SOCIAL_GROUP, roomName, 1, 1));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_PLAY_GAME), PayloadUtil.createGameStartRequest(YokelLounge.SOCIAL_GROUP, roomName, 1));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertFalse(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_TABLE_JOIN), PayloadUtil.createTableJoinRequest(player2, YokelLounge.SOCIAL_GROUP, roomName, 1, 1));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertFalse(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_TABLE_JOIN), PayloadUtil.createTableJoinRequest(player2, YokelLounge.SOCIAL_GROUP, roomName, 1, 2));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_TABLE_STAND), PayloadUtil.createTableStandRequest(YokelLounge.SOCIAL_GROUP, roomName, 1, 2));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_TABLE_SIT), PayloadUtil.createTableJoinRequest(player2, YokelLounge.SOCIAL_GROUP, roomName, 1, 3));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_TABLE_INFO), PayloadUtil.createTableInfoRequest(YokelLounge.SOCIAL_GROUP, roomName));
        result = buildPayload.invoke();
        printInvocationResult(result);
        //Assert.assertTrue(getInvocationBoolean(result));

        buildPayload.setParameterValues(YokelUtilities.otos(ServerRequest.REQUEST_PLAY_GAME), PayloadUtil.createGameStartRequest(YokelLounge.SOCIAL_GROUP, roomName, 1));
        result = buildPayload.invoke();
        printInvocationResult(result);
        Assert.assertFalse(getInvocationBoolean(result));

        //GameRunner testGame = new GameRunner(daemon, room);
        //testGame.start();
        //testGame.setRunning(false);

    }

    private static class GameRunner implements Runnable{
        ServerManager serverManager;
        GameManager gameManager;
        boolean running;
        float tickRate;


        public GameRunner(ServerManager manager, YokelTable table){
            this.serverManager = manager;
            this.gameManager = new GameManager(table);
            tickRate = daemon.getTickRate();
        }

        public void setRunning(boolean b){
            this.running = b;
        }

        private void tick(){
            gameManager.update(1);
        }

        public void run() {
            /**
             * while(true)
             *     check for client commands
             *     sanity check client commands
             *     move all entities
             *     resolve collisions
             *     sanity check world data
             *     send updates about the game to the clients
             *     handle client disconnects
             * end while
             */
            double ns = 1000000000.0 / 60.0;
            double delta = 0;

            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            setRunning(true);

            while(running){
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while(delta >= tickRate){
                    tick();
                    delta--;
                }
            }
        }
    }

    private void printInvocationResult(Object o){
        if(o instanceof String[]){
            System.out.println(Arrays.toString((String[]) o));
        } else {
            System.out.println(o);
        }
    }

    private boolean getInvocationBoolean(Object o){
        if(o instanceof String[]){
            Array<String> a = YokelUtilities.arrayToList((String[]) o);
            return Boolean.parseBoolean(a.get(0));
        } else {
            return Boolean.parseBoolean(o.toString());
        }
    }
}