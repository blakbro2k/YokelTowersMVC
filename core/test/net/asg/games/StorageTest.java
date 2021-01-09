package net.asg.games;

import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.objects.AbstractYokelObject;
import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelBlockMove;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.storage.MemoryStorage;
import net.asg.games.storage.YokelMemoryStorage;
import net.asg.games.storage.YokelStorageAdapter;

import org.junit.Assert;
import org.junit.Test;

public class StorageTest {

    @Test
    public void testMemoryStorage() throws Exception {
        MemoryStorage storage = new YokelMemoryStorage();

        YokelPlayer player = new YokelPlayer();
        YokelSeat seat = new YokelSeat();
        YokelLounge lounge = new YokelLounge();

        String expectedId = player.getId();
        String expectedSeatId = seat.getId();
        String expectedLoungeId = lounge.getId();

        String expectedName = "expectedName";
        Assert.assertNull(storage.getObjectById(YokelPlayer.class, expectedId));
        Assert.assertNull(storage.getObjectById(YokelPlayer.class, expectedName));
        Assert.assertEquals("", storage.getKey(player, true));
        player.setName(expectedName);
        Assert.assertEquals(expectedName, storage.getKey(player, true));
        Assert.assertEquals(expectedId, storage.getKey(player, false));
        Assert.assertEquals(AbstractYokelObject.class, storage.getClassFromSuper(AbstractYokelObject.class, player));
        Assert.assertEquals(AbstractYokelObject.class, storage.getClassFromSuper(AbstractYokelObject.class, new YokelBlock()));
        Assert.assertEquals(Object.class, storage.getClassFromSuper(AbstractYokelObject.class, new YokelBlockEval()));
        Assert.assertEquals(AbstractYokelObject.class, storage.getClassFromSuper(AbstractYokelObject.class, new YokelBlockMove()));
        storage.saveObject(player);
        Assert.assertEquals(1, storage.getTransactions());
        storage.rollBackTransactions();
        Assert.assertEquals(0, storage.getTransactions());
        storage.saveObject(player);
        Assert.assertEquals(1, storage.getTransactions());
        storage.commitTransactions();
        Assert.assertEquals(0, storage.getTransactions());
        Assert.assertEquals(player, storage.getObjectByName(YokelPlayer.class, expectedName));
        Assert.assertNull(storage.getObjectByName(YokelPlayer.class, expectedSeatId));
        storage.saveObject(seat);
        storage.saveObject(lounge);
        Assert.assertNull(storage.getObjectById(YokelPlayer.class, expectedSeatId));
        Assert.assertNull(storage.getObjectById(YokelPlayer.class, expectedLoungeId));
        storage.commitTransactions();
        Assert.assertEquals(seat, storage.getObjectById(YokelSeat.class, expectedSeatId));
        Assert.assertEquals(lounge, storage.getObjectById(YokelLounge.class, expectedLoungeId));

        storage.dispose();
    }

    @Test
    public void testYokelMemoryStorage() throws Exception {
        YokelStorageAdapter storage = new YokelMemoryStorage();

        String clientId1 = "1";
        String clientId2 = "2";

        YokelPlayer player1 = new YokelPlayer("test1");
        YokelPlayer player2 = new YokelPlayer("test2");

        //registerPlayer
        try{
            storage.registerPlayer(null, null);
        } catch(Exception e){
            String message = e.getMessage();
            Assert.assertEquals("Player was null.", message);
        }

        try{
            storage.registerPlayer(null, player1);
        } catch(Exception e){
            String message = e.getMessage();
            Assert.assertEquals("clientID was null.", message);
        }

        try{
            storage.registerPlayer(clientId1, null);
        } catch(Exception e){
            String message = e.getMessage();
            Assert.assertEquals("Player was null.", message);
        }

        storage.registerPlayer(clientId1, player1);
        storage.registerPlayer(clientId2, player2);

        Assert.assertEquals(2, countIterator(storage.getAllRegisteredPlayers()));
        storage.unRegisterPlayer(clientId1);
        Assert.assertEquals(1, countIterator(storage.getAllRegisteredPlayers()));
        storage.unRegisterPlayer(clientId1);
        Assert.assertEquals(1, countIterator(storage.getAllRegisteredPlayers()));
        storage.unRegisterPlayer(clientId2);
        Assert.assertEquals(0, countIterator(storage.getAllRegisteredPlayers()));

        Assert.assertNull(storage.getRegisteredPlayerGivenId(player1.getPlayerId()));
        Assert.assertFalse(storage.isPlayerRegistered(player1.getPlayerId()));
        Assert.assertFalse(storage.isClientRegistered(clientId1));

        storage.registerPlayer(clientId1, player1);
        storage.registerPlayer(clientId2, player2);

        Assert.assertEquals(player1, storage.getRegisteredPlayerGivenId(player1.getPlayerId()));
        Assert.assertEquals(player2, storage.getRegisteredPlayerGivenId(player2.getPlayerId()));

        Assert.assertEquals(player1, storage.getRegisteredPlayer(player1));
        Assert.assertEquals(player2, storage.getRegisteredPlayer(player2));

        YokelLounge lounge = new YokelLounge("testLounge");
        storage.putLounge(lounge);
        Assert.assertEquals(lounge, storage.getLounge("testLounge"));
        Assert.assertEquals(lounge, storage.getLounge(lounge.getId()));
        Assert.assertNull(storage.getLounge("NottestLounge"));

        YokelRoom room = new YokelRoom("testRoom");
        storage.putRoom(room);
        Assert.assertEquals(room, storage.getRoom("testRoom"));
        Assert.assertEquals(room, storage.getRoom(room.getId()));
        Assert.assertNull(storage.getRoom("NottestLounge"));

        YokelTable table = new YokelTable(1);
        storage.putTable(table);
        Assert.assertNull(storage.getTable("testRoom"));
        Assert.assertEquals(table, storage.getTable(table.getId()));
        Assert.assertNull(storage.getTable("NottestLounge"));

        YokelSeat seat = new YokelSeat(1);
        storage.putSeat(seat);
        Assert.assertNull(storage.getSeat("testSeat"));
        Assert.assertEquals(seat, storage.getSeat(seat.getId()));
        Assert.assertNull(storage.getSeat("NottestLounge"));

        /*
        @Override
        public void putPlayer(YokelPlayer player) throws Exception {

        }

        @Override
        public YokelPlayer getPlayer(String nameOrId) {
            return null;
        }

        @Override
        public ObjectMap.Values<YokelLounge> getAllLounges() {
            return lounges.values();
        }

        @Override
        public void putGame(String id, GameManager game) {
            throw new RuntimeException("addGame not implemented.");
        }

        @Override
        public GameManager getGame(String gameId) {
            return games.get(gameId);
        }

        @Override
        public ObjectMap.Values<GameManager> getAllGames() {
            return games.values();
        }
*/
    }

    private int countIterator(ObjectMap.Values<YokelPlayer> allRegisteredPlayers) {
        int count = 0;
        if(allRegisteredPlayers != null){
            while(allRegisteredPlayers.hasNext()){
                ++count;
                allRegisteredPlayers.next();
            }
        }
        return count;
    }
}