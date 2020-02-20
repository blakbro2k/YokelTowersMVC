package net.asg.games.storage;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;

public class MemoryStorage implements StorageInterface {
    //<"lounge name", room object>
    private OrderedMap<String, YokelLounge> lounges;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> registeredPlayers;
    //<"table name", gameManager>
    private OrderedMap<String, GameManager> games;

    public MemoryStorage(){
        lounges = new OrderedMap<>();
        registeredPlayers = new OrderedMap<>();
        games = new OrderedMap<>();
    }

    @Override
    public void putTableState(YokelTable table) {

    }

    @Override
    public YokelTable getTableState() {
        return null;
    }

    @Override
    public void putRegisteredPlayer(String clientID, YokelPlayer player) throws Exception {
        if(player == null) throw new Exception("Player was null.");
        registeredPlayers.put(clientID, player);
    }

    @Override
    public YokelPlayer getRegisteredPlayer(String id) {
        return registeredPlayers.get(id);
    }

    @Override
    public ObjectMap.Values<YokelPlayer> getAllRegisteredPlayers() {
        return registeredPlayers.values();
    }

    @Override
    public void putLounge(YokelLounge lounge) throws Exception {
        if(lounge == null) throw new Exception("Lounge was null");
        lounges.put(lounge.getName(), lounge);
    }

    @Override
    public YokelLounge getLounge(String lounge) {
        return lounges.get(lounge);
    }

    @Override
    public ObjectMap.Values<YokelLounge> getAllLounges() {
        return lounges.values();
    }

    @Override
    public void addGame(String id, GameManager game) {
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

    @Override
    public void dispose() {
        if(lounges != null){
            lounges.clear();
            lounges = null;
        }

        if(registeredPlayers != null){
            registeredPlayers.clear();
            registeredPlayers = null;
        }

        if(games != null){
            games.clear();
            games = null;
        }
    }
}
