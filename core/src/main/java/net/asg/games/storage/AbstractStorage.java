package net.asg.games.storage;

import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelObject;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;

public abstract class AbstractStorage implements YokelStorage {
    @Override
    public abstract void putLounge(YokelLounge lounge) throws Exception;

    @Override
    public abstract YokelLounge getLounge(String nameOrId);

    @Override
    public void putRoom(YokelRoom room) throws Exception {
    }

    @Override
    public YokelRoom getRoom(String nameOrId) {
        return null;
    }

    @Override
    public void putTable(YokelTable table) throws Exception {

    }

    @Override
    public YokelTable getTable(String nameOrId) {
        return null;
    }

    @Override
    public void putSeat(YokelSeat lounge) throws Exception {

    }

    @Override
    public YokelSeat getSeat(String nameOrId) {
        return null;
    }

    @Override
    public void putPlayer(YokelPlayer player) throws Exception {

    }

    @Override
    public YokelPlayer getPlayer(String nameOrId) {
        return null;
    }

    @Override
    public abstract ObjectMap.Values<YokelLounge> getAllLounges();

    @Override
    public abstract void putGame(String id, GameManager game);

    @Override
    public abstract GameManager getGame(String gameId);

    @Override
    public abstract ObjectMap.Values<GameManager> getAllGames();

    @Override
    public abstract void dispose();

    @Override
    public abstract void registerPlayer(String clientId, YokelPlayer player) throws Exception;

    @Override
    public abstract void unRegisterPlayer(String clientID) throws Exception;

    @Override
    public abstract YokelPlayer getRegisteredPlayer(String playerId);

    @Override
    public abstract YokelPlayer getRegisteredPlayer(YokelPlayer player);

    @Override
    public abstract ObjectMap.Values<YokelPlayer> getAllRegisteredPlayers();

    @Override
    public abstract boolean isClientRegistered(String clientId);

    @Override
    public boolean isPlayerRegistered(String playerId) {
        return false;
    }

    @Override
    public <T> T getObjectByName(Class<T> clazz, String name) {
        return null;
    }

    @Override
    public <T> T getObjectById(Class<T> clazz, String id) {
        return null;
    }

    @Override
    public void saveObject(YokelObject object) {

    }

    @Override
    public void commitTransactions() {

    }

    @Override
    public void rollTransactions() {

    }
}
