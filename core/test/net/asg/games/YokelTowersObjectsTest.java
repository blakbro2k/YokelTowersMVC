package net.asg.games;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.YokelBlockType;

import org.junit.Assert;
import org.junit.Test;

import jdk.nashorn.internal.runtime.ECMAException;

public class YokelTowersObjectsTest {
    @Test
    public void YokelLoungeTest() {
        YokelLounge lounge;

        try{
            lounge = new YokelLounge(null);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        String loungeName = YokelLounge.SOCIAL_GROUP;
        String loungeName2 = YokelLounge.BEGINNER_GROUP;
        lounge = new YokelLounge(loungeName);
        Assert.assertEquals(loungeName, lounge.getName());
        lounge.setName(loungeName2);
        Assert.assertEquals(loungeName2, lounge.getName());
        Assert.assertEquals(0, lounge.getAllRooms().size);
        try{
            lounge.addRoom(null);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        String roomName = "Test Tower";
        YokelRoom room = new YokelRoom(roomName);
        lounge.addRoom(room);
        Assert.assertNull(lounge.getRoom(null));
        Assert.assertNull(lounge.getRoom("Invalid"));
        Assert.assertEquals(room, lounge.getRoom(roomName));
        Assert.assertEquals(1, lounge.getAllRooms().size);
        lounge.removeRoom(roomName);
        Assert.assertEquals(0, lounge.getAllRooms().size);
        Assert.assertEquals(getJSON(lounge), lounge.toString());
    }

    @Test
    public void YokelPlayerTest() {
        YokelPlayer player;

        try{
            player = new YokelPlayer(null);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        String name = "blakbro2k";
        player = new YokelPlayer(name);
        Assert.assertEquals(name, player.getName());
        Assert.assertEquals(1500, player.getRating());
        player.increaseRating(20);
        Assert.assertEquals(1520, player.getRating());
        player.decreaseRating(5);
        Assert.assertEquals(1515, player.getRating());
        Assert.assertNotNull(player.getPlayerId());
        player.setSessionId("212");
        Assert.assertEquals("212", player.getSessionId());
        Assert.assertEquals(getJSON(player), player.toString());
    }

    @Test
    public void YokelSeatTest() {
        YokelSeat seat;

        try{
            seat = new YokelSeat(-1);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            seat = new YokelSeat(11);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            seat = new YokelSeat(0);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        seat = new YokelSeat(1);
        Assert.assertFalse(seat.isOccupied());
        YokelPlayer player1 = new YokelPlayer("blakbro2k");
        seat.sitDown(player1);
        Assert.assertTrue(seat.isOccupied());
        Assert.assertEquals(player1, seat.getSeatedPlayer());
        Assert.assertEquals(1, seat.getSeatNumber());
        Assert.assertEquals(getJSON(seat), seat.toString());
    }

    @Test
    public void YokelRoomTest() {
        YokelRoom room;

        try{
            room = new YokelRoom(null);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        room = new YokelRoom("Effel Tower");
        Assert.assertNotNull(room.getRoomId());
        Assert.assertEquals(GdxMaps.newObjectMap(), room.getAllTables());
        room.addTable(1);
        Assert.assertEquals(1, room.getAllTables().size);
        room.addTable(11);
        Assert.assertEquals(2, room.getAllTables().size);
        Assert.assertNull(room.getTable(2));
        Assert.assertNotNull(room.getTable(1));
        Assert.assertNotNull(room.getTable(11));
        room.removeTable(11);
        Assert.assertEquals(1, room.getAllTables().size);
        YokelPlayer player1 = new YokelPlayer("blakbro2k");
        YokelPlayer player2 = new YokelPlayer("lholtham");
        YokelPlayer player3 = new YokelPlayer("notit");

        room.joinRoom(player1);
        Assert.assertEquals(1, room.getAllPlayers().size);
        room.joinRoom(player2);
        Assert.assertEquals(2, room.getAllPlayers().size);
        System.out.println("Room:" + room);
        room.joinRoom(player2);
        Assert.assertEquals(2, room.getAllPlayers().size);
        room.leaveRoom(player2);
        Assert.assertEquals(1, room.getAllPlayers().size);
        room.leaveRoom(player3);
        Assert.assertEquals(1, room.getAllPlayers().size);
        Assert.assertEquals(getJSON(room), room.toString());
    }


    @Test
    public void YokelTableTest() {
        YokelTable table1;
        try{
            table1 = new YokelTable(-1);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            table1 = new YokelTable(0);
        } catch (Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        table1 = new YokelTable(1);
        Assert.assertNotNull(table1.getTableId());
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PUBLIC, table1.getAccessType());
        Assert.assertFalse(table1.isRated());
        Assert.assertTrue(table1.isSoundOn());

        table1.setRated(true);
        Assert.assertTrue(table1.isRated());
        table1.setRated(false);
        Assert.assertFalse(table1.isRated());

        table1.setSound(true);
        Assert.assertTrue(table1.isSoundOn());
        table1.setSound(false);
        Assert.assertFalse(table1.isSoundOn());

        table1.setAccessType("private");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PRIVATE, table1.getAccessType());
        table1.setAccessType("PriVAte");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PRIVATE, table1.getAccessType());
        table1.setAccessType("PRIVATE");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PRIVATE, table1.getAccessType());

        table1.setAccessType("protected");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PROTECTED, table1.getAccessType());
        table1.setAccessType("prOTecTed");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PROTECTED, table1.getAccessType());
        table1.setAccessType("PROTECTED");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PROTECTED, table1.getAccessType());

        table1.setAccessType("public");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PUBLIC, table1.getAccessType());
        table1.setAccessType("PubLIc");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PUBLIC, table1.getAccessType());
        table1.setAccessType("PUBLIC");
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PUBLIC, table1.getAccessType());

        table1.setAccessType(YokelTable.ACCESS_TYPE.PRIVATE);
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PRIVATE, table1.getAccessType());
        table1.setAccessType(YokelTable.ACCESS_TYPE.PROTECTED);
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PROTECTED, table1.getAccessType());
        table1.setAccessType(YokelTable.ACCESS_TYPE.PUBLIC);
        Assert.assertEquals(YokelTable.ACCESS_TYPE.PUBLIC, table1.getAccessType());

        Assert.assertEquals(8, table1.getSeats().size);
        Assert.assertFalse(table1.isGameRunning());
        Assert.assertFalse(table1.isTableStartReady());
        Assert.assertFalse(table1.isGroupReady(-1));
        Assert.assertFalse(table1.isGroupReady(5));

        YokelPlayer player1 = new YokelPlayer("blakbro2k");
        YokelPlayer player2 = new YokelPlayer("lholtham");
        YokelPlayer player3 = new YokelPlayer("notit");

        /**
         * Test table grouping
         * If one of the seats grouped are occupied with
         * another seperate group occupied, then you can start the game
         */

        Assert.assertFalse(table1.isGroupReady(0));
        table1.getSeat(0).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(0));
        table1.getSeat(0).standUp();
        Assert.assertFalse(table1.isGroupReady(0));

        Assert.assertFalse(table1.isGroupReady(0));
        table1.getSeat(1).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(0));
        table1.getSeat(1).standUp();
        Assert.assertFalse(table1.isGroupReady(0));

        Assert.assertFalse(table1.isGroupReady(1));
        table1.getSeat(2).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(1));
        table1.getSeat(2).standUp();
        Assert.assertFalse(table1.isGroupReady(1));

        Assert.assertFalse(table1.isGroupReady(1));
        table1.getSeat(3).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(1));
        table1.getSeat(3).standUp();
        Assert.assertFalse(table1.isGroupReady(1));

        Assert.assertFalse(table1.isGroupReady(2));
        table1.getSeat(4).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(2));
        table1.getSeat(4).standUp();
        Assert.assertFalse(table1.isGroupReady(2));

        Assert.assertFalse(table1.isGroupReady(2));
        table1.getSeat(5).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(2));
        table1.getSeat(5).standUp();
        Assert.assertFalse(table1.isGroupReady(2));

        Assert.assertFalse(table1.isGroupReady(3));
        table1.getSeat(6).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(3));
        table1.getSeat(6).standUp();
        Assert.assertFalse(table1.isGroupReady(3));

        Assert.assertFalse(table1.isGroupReady(3));
        table1.getSeat(7).sitDown(player1);
        Assert.assertTrue(table1.isGroupReady(3));
        table1.getSeat(7).standUp();
        Assert.assertFalse(table1.isGroupReady(3));

        /**
         * Test table ready
         */
        table1.startGame();
        Assert.assertFalse(table1.isGameRunning());
        Assert.assertFalse(table1.isTableStartReady());
        table1.getSeat(0).sitDown(player2);
        Assert.assertFalse(table1.isTableStartReady());
        table1.getSeat(2).sitDown(player3);
        Assert.assertTrue(table1.isTableStartReady());
        table1.startGame();
        Assert.assertTrue(table1.isGameRunning());
        table1.stopGame();
        Assert.assertFalse(table1.isGameRunning());
        table1.getSeat(0).standUp();
        table1.getSeat(2).standUp();

        Assert.assertFalse(table1.isTableStartReady());
        table1.getSeat(1).sitDown(player2);
        Assert.assertFalse(table1.isTableStartReady());
        table1.getSeat(2).sitDown(player3);
        Assert.assertTrue(table1.isTableStartReady());
        table1.getSeat(1).standUp();
        table1.getSeat(2).standUp();

        Assert.assertFalse(table1.isTableStartReady());
        table1.getSeat(4).sitDown(player2);
        Assert.assertFalse(table1.isTableStartReady());
        table1.getSeat(2).sitDown(player3);
        Assert.assertTrue(table1.isTableStartReady());
        table1.getSeat(4).standUp();
        table1.getSeat(2).standUp();

        Assert.assertEquals(getJSON(table1), table1.toString());
    }

    private String getJSON(Object o){
        return Util.getJsonString(o);
    }

    @Test
    public void getType() {
        YokelBlock exp1 = new YokelBlock();
        System.out.println("Testing getType():" + exp1);
        Assert.assertEquals(YokelBlockType.class, exp1.getType().getClass());
        System.out.println("End getType() test:");
    }

    @Test
    public void setBroken() {
        YokelBlock exp1 = new YokelBlock();
        System.out.println("Testing setBroken():" + exp1);
        exp1.setBroken(true);
        Assert.assertTrue(exp1.isBroken());
        exp1.setBroken(false);
        Assert.assertFalse(exp1.isBroken());
        System.out.println("End setBroken() test:");
    }

    @Test
    public void isBroken() {
        YokelBlock exp1 = new YokelBlock();
        System.out.println("Testing getType():" + exp1);
        Assert.assertFalse(exp1.isBroken());
        System.out.println("End getType() test:");
    }

    @Test
    public void matchesType() {
        System.out.println("Testing matchesType():");
        YokelBlock exp1 = new YokelBlock(YokelBlockType.AttackY);
        YokelBlock exp2 = new YokelBlock(YokelBlockType.AttackO);
        YokelBlock exp3 = new YokelBlock(YokelBlockType.AttackY);

        System.out.println(exp1.matchesType(exp3.getType()));
        Assert.assertTrue(exp1.matchesType(exp3.getType()));
        Assert.assertTrue(exp3.matchesType(exp1.getType()));
        Assert.assertFalse(exp2.matchesType(exp1.getType()));
        Assert.assertFalse(exp2.matchesType(exp3.getType()));

        System.out.println("End matchesType() test:");
    }

    @Test
    public void matchesGenericType() {
        System.out.println("Testing matchesGenericType():");
        YokelBlock exp1 = new YokelBlock(YokelBlockType.NormalY);
        YokelBlock exp2 = new YokelBlock(YokelBlockType.AttackY);
        YokelBlock exp3 = new YokelBlock(YokelBlockType.DefenseY);
        YokelBlock exp4 = new YokelBlock(YokelBlockType.BrokenY);

        YokelBlock exp5 = new YokelBlock(YokelBlockType.NormalO);
        YokelBlock exp6 = new YokelBlock(YokelBlockType.AttackO);
        YokelBlock exp7 = new YokelBlock(YokelBlockType.DefenseO);
        YokelBlock exp8 = new YokelBlock(YokelBlockType.BrokenO);

        YokelBlock exp9 = new YokelBlock(YokelBlockType.NormalK);
        YokelBlock exp10 = new YokelBlock(YokelBlockType.AttackK);
        YokelBlock exp11 = new YokelBlock(YokelBlockType.DefenseK);
        YokelBlock exp12 = new YokelBlock(YokelBlockType.BrokenK);

        YokelBlock exp13 = new YokelBlock(YokelBlockType.NormalE);
        YokelBlock exp14 = new YokelBlock(YokelBlockType.AttackE);
        YokelBlock exp15 = new YokelBlock(YokelBlockType.DefenseE);
        YokelBlock exp16 = new YokelBlock(YokelBlockType.BrokenE);

        YokelBlock exp17 = new YokelBlock(YokelBlockType.NormalL);
        YokelBlock exp18 = new YokelBlock(YokelBlockType.AttackL);
        YokelBlock exp19 = new YokelBlock(YokelBlockType.DefenseL);
        YokelBlock exp20 = new YokelBlock(YokelBlockType.BrokenL);

        YokelBlock exp21 = new YokelBlock(YokelBlockType.NormalEx);
        YokelBlock exp22 = new YokelBlock(YokelBlockType.AttackEx);
        YokelBlock exp23 = new YokelBlock(YokelBlockType.DefenseEx);
        YokelBlock exp24 = new YokelBlock(YokelBlockType.BrokenEx);

        Assert.assertTrue(exp1.matchesGenericType(exp1));
        Assert.assertTrue(exp1.matchesGenericType(exp2));
        Assert.assertTrue(exp1.matchesGenericType(exp3));
        Assert.assertTrue(exp1.matchesGenericType(exp4));
        Assert.assertFalse(exp1.matchesGenericType(exp5));

        Assert.assertTrue(exp5.matchesGenericType(exp5));
        Assert.assertTrue(exp5.matchesGenericType(exp6));
        Assert.assertTrue(exp5.matchesGenericType(exp7));
        Assert.assertTrue(exp5.matchesGenericType(exp8));
        Assert.assertFalse(exp5.matchesGenericType(exp1));

        Assert.assertTrue(exp9.matchesGenericType(exp9));
        Assert.assertTrue(exp9.matchesGenericType(exp10));
        Assert.assertTrue(exp9.matchesGenericType(exp11));
        Assert.assertTrue(exp9.matchesGenericType(exp12));
        Assert.assertFalse(exp9.matchesGenericType(exp5));

        Assert.assertTrue(exp13.matchesGenericType(exp13));
        Assert.assertTrue(exp13.matchesGenericType(exp14));
        Assert.assertTrue(exp13.matchesGenericType(exp15));
        Assert.assertTrue(exp13.matchesGenericType(exp16));
        Assert.assertFalse(exp13.matchesGenericType(exp5));

        Assert.assertTrue(exp17.matchesGenericType(exp17));
        Assert.assertTrue(exp17.matchesGenericType(exp18));
        Assert.assertTrue(exp17.matchesGenericType(exp19));
        Assert.assertTrue(exp17.matchesGenericType(exp20));
        Assert.assertFalse(exp17.matchesGenericType(exp5));

        Assert.assertTrue(exp21.matchesGenericType(exp21));
        Assert.assertTrue(exp21.matchesGenericType(exp22));
        Assert.assertTrue(exp21.matchesGenericType(exp23));
        Assert.assertTrue(exp21.matchesGenericType(exp24));
        Assert.assertFalse(exp21.matchesGenericType(exp5));
        System.out.println("End matchesGenericType() test:");
    }

    @Test
    public void testToString() {
        System.out.println("Testing testToString():");
        YokelBlock exp1 = new YokelBlock();
        Assert.assertNotNull(exp1);
        Assert.assertEquals(String.class, exp1.toString().getClass());
        System.out.println("End testToString() test:");
    }

    @Test
    public void testRandomInitializer() {
        System.out.println("Testing testRandomInitializer():");
        YokelBlock exp1 = new YokelBlock();
        Assert.assertNotNull(exp1);
        Assert.assertEquals(YokelBlock.class, exp1.getClass());
        System.out.println("End testRandomInitializer() test:");
    }

    @Test
    public void testObjectEquals() {
        //YokelBlock
        YokelBlock exp1 = new YokelBlock(YokelBlockType.DefenseE);
        YokelBlock exp2 = new YokelBlock(YokelBlockType.DefenseY);
        YokelBlock exp3 = new YokelBlock(YokelBlockType.DefenseE);
        String str = "block";

        Assert.assertNotEquals(null, exp1);
        Assert.assertNotEquals(exp1, exp2);
        Assert.assertEquals(exp1, exp3);
        Assert.assertEquals(exp1.getType(), YokelBlockType.DefenseE);
        Assert.assertNotEquals(exp1, str);

        Array<YokelBlock> expectedBlocks = createAllBlockArray();
        Array<YokelBlock> testingBlocks = createAllBlockArray();

        for(int i = 0; i < testingBlocks.size; i++){
            Assert.assertEquals(expectedBlocks.get(i), testingBlocks.get(i));
        }

        //YokelLounge
        YokelLounge expected = new YokelLounge("testing");
        YokelLounge expected2 = new YokelLounge("testing2");
        YokelLounge testing = new YokelLounge("testing");
        YokelLounge testing2 = new YokelLounge("testing2");

        Assert.assertEquals(expected, testing);
        Assert.assertNotEquals(expected2, testing);
        expected2.addRoom(new YokelRoom("room"));
        Assert.assertNotEquals(expected2, testing);
        expected.addRoom(new YokelRoom("room"));
        testing.addRoom(new YokelRoom("room"));
        Assert.assertEquals(expected, testing);
        Assert.assertEquals(testing2, testing2);

        //YokelPlayer
        YokelPlayer player1 = new YokelPlayer("ReadyPlwyer1");
        YokelPlayer player2 = new YokelPlayer("ReadyPlwyer2");
        YokelPlayer player3 = new YokelPlayer("ReadyPlwyer1");
        YokelPlayer player4 = new YokelPlayer("ReadyPlwyer2");
        Assert.assertEquals(player1, player3);
        Assert.assertNotEquals(player1, player2);
        Assert.assertEquals(player4, player2);


        //YokelRoom
        YokelRoom room1 = new YokelRoom("Room1");
        //throw new AssertionError("YokelRoom not finished.");


        //YokelSeat
        YokelSeat seat1 = new YokelSeat(1);
        YokelSeat seat2 = new YokelSeat(1);
        seat2.sitDown(player1);
        YokelSeat seat3 = new YokelSeat(1);
        YokelSeat seat4 = new YokelSeat(2);
        Assert.assertEquals(seat1, seat3);
        Assert.assertNotEquals(seat1, seat2);
        seat2.standUp();
        Assert.assertEquals(seat1, seat2);
        seat2.sitDown(player1);
        seat1.sitDown(player2);
        seat3.sitDown(player1);

        Assert.assertEquals(seat2, seat3);
        Assert.assertNotEquals(player2, player1);
        Assert.assertNotEquals(seat1, seat2);
        Assert.assertNotEquals(seat1, seat4);
        seat4.sitDown(player2);
        Assert.assertNotEquals(seat1, seat4);

        //YokelTable
        throw new AssertionError("YokelTable not finished.");
    }

    @Test
    public void testSpecifiedRandomInitializer() {
        System.out.println("Testing testSpecifiedRandomInitializer():");
        YokelBlock exp1 = new YokelBlock(YokelBlock.INIT_TYPE.ANY);
        Assert.assertNotNull(exp1);
        Assert.assertEquals(YokelBlock.class, exp1.getClass());

        YokelBlock exp2 = new YokelBlock(YokelBlock.INIT_TYPE.POWER);
        Assert.assertNotNull(exp2);
        Assert.assertEquals(YokelBlock.class, exp2.getClass());
        Assert.assertTrue(Util.isPowerBlock(exp2));

        YokelBlock exp3 = new YokelBlock(YokelBlock.INIT_TYPE.DEFENSE);
        Assert.assertNotNull(exp3);
        Assert.assertEquals(YokelBlock.class, exp3.getClass());
        Assert.assertTrue(Util.isDefenseBlock(exp3));
        System.out.println("End testSpecifiedRandomInitializer() test:");
    }

    @Test
    public void testSpecifiedInitializer() {
        System.out.println("Testing testSpecifiedInitializer():");

        Array<YokelBlock> blocks = createAllBlockArray();
        Array<YokelBlockType> types = completeYokelTypeList();

        for(int i = 0; i < types.size; i++){
            System.out.println("Asserting type=" + types.get(i) + " against block=" + blocks.get(i));
            Assert.assertEquals(types.get(i), blocks.get(i).getType());
        }

        System.out.println("End testSpecifiedInitializer() test:");
    }

    @Test
    public void promote() {
        YokelBlock exp1 = new YokelBlock(YokelBlock.INIT_TYPE.NORMAL);
        exp1.promote(true);
        Assert.assertTrue(Util.isPowerBlock(exp1));
        exp1.promote(false);
        Assert.assertTrue(Util.isDefenseBlock(exp1));
    }

    @Test
    public void demote() {
        YokelBlock exp1 = new YokelBlock(YokelBlock.INIT_TYPE.NORMAL);
        exp1.promote(false);
        Assert.assertTrue(Util.isDefenseBlock(exp1));
        exp1.demote();
        Assert.assertTrue(!Util.isPowerBlock(exp1) && !Util.isDefenseBlock(exp1));
        exp1.promote(true);
        Assert.assertTrue(Util.isPowerBlock(exp1));
        exp1.demote();
        Assert.assertTrue(!Util.isPowerBlock(exp1) && !Util.isDefenseBlock(exp1));
    }

    @Test
    public void testBreakBlock() {
        YokelBlock exp1 = new YokelBlock(YokelBlock.INIT_TYPE.NORMAL);
        YokelBlock power = exp1.breakBlock();

        Assert.assertNull(power);
        Assert.assertTrue(Util.isBrokenBlock(exp1));

        YokelBlock exp2 = new YokelBlock(YokelBlock.INIT_TYPE.POWER);
        YokelBlock power2 = exp2.breakBlock();

        Assert.assertTrue(Util.isBrokenBlock(exp2));
        exp2.promote(true);
        Assert.assertEquals(power2, exp2);

        YokelBlock exp3 = new YokelBlock(YokelBlock.INIT_TYPE.DEFENSE);
        YokelBlock power3 = exp3.breakBlock();

        Assert.assertTrue(Util.isBrokenBlock(exp3));
        exp3.promote(false);
        Assert.assertEquals(power3, exp3);

        YokelBlock any = new YokelBlock(YokelBlock.INIT_TYPE.NORMAL);
        YokelBlock anyPower = any.breakBlock();

        Assert.assertNull(anyPower);
        Assert.assertTrue(Util.isBrokenBlock(any));

        any.promote(true);
        Assert.assertTrue(Util.isPowerBlock(any));
        anyPower = any.breakBlock();
        Assert.assertTrue(Util.isBrokenBlock(any));
        any.promote(true);
        Assert.assertEquals(anyPower, any);

        any.promote(false);
        Assert.assertTrue(Util.isDefenseBlock(any));
        anyPower = any.breakBlock();
        Assert.assertTrue(Util.isBrokenBlock(any));
        any.promote(false);
        Assert.assertEquals(anyPower, any);
    }

    private Array<YokelBlock> createAllBlockArray(){
        Array<YokelBlock> array = new Array<>();
        Array<YokelBlockType> types = completeYokelTypeList();

        for(YokelBlockType type : Util.toIterable(types)){
            array.add(new YokelBlock(type));
        }

        return array;
    }

    public static Array<YokelBlockType> completeYokelTypeList(){
        Array<YokelBlockType> array = new Array<>();
        array.add(YokelBlockType.AttackY);
        array.add(YokelBlockType.AttackO);
        array.add(YokelBlockType.AttackK);
        array.add(YokelBlockType.AttackE);
        array.add(YokelBlockType.AttackL);
        array.add(YokelBlockType.AttackEx);
        array.add(YokelBlockType.DefenseY);
        array.add(YokelBlockType.DefenseO);
        array.add(YokelBlockType.DefenseK);
        array.add(YokelBlockType.DefenseE);
        array.add(YokelBlockType.DefenseL);
        array.add(YokelBlockType.DefenseEx);
        array.add(YokelBlockType.NormalY);
        array.add(YokelBlockType.NormalO);
        array.add(YokelBlockType.NormalK);
        array.add(YokelBlockType.NormalE);
        array.add(YokelBlockType.NormalL);
        array.add(YokelBlockType.NormalEx);
        array.add(YokelBlockType.Clear);
        array.add(YokelBlockType.Midas);
        array.add(YokelBlockType.Medusa);
        array.add(YokelBlockType.Stone);
        return array;
    }
}