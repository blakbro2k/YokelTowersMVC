package net.asg.games;

import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.Util;

import org.junit.Assert;
import org.junit.Test;

public class YokelTowersObjectsTest {
    @Test
    public void YokelLoungeTest() throws Exception{
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
        Assert.assertEquals(convertJSON(lounge), lounge.toString());
    }

    @Test
    public void YokelPlayerTest() throws Exception{
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
        Assert.assertEquals(convertJSON(player), player.toString());
    }

    @Test
    public void YokelSeatTest() throws Exception{
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
        Assert.assertEquals(convertJSON(seat), seat.toString());
    }

    @Test
    public void YokelRoomTest() throws Exception{
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
        Assert.assertEquals(convertJSON(room), room.toString());
    }


    @Test
    public void YokelTableTest() throws Exception{
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

        Assert.assertEquals(convertJSON(table1), table1.toString());
    }

    private String convertJSON(Object o){
        return Util.convertJsonString(Util.getJsonString(o));
    }
}