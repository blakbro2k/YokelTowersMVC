package net.asg.games.storage;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;

public class MemoryYokelStorage implements YokelStorage {
    //<"lounge name", room object>
    private OrderedMap<String, String> clients;
    //<"lounge name", room object>
    private OrderedMap<String, YokelLounge> lounges;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> registeredPlayers;
    //<"table name", gameManager>
    private OrderedMap<String, GameManager> games;

    public MemoryYokelStorage(){
        lounges = new OrderedMap<>();
        registeredPlayers = new OrderedMap<>();
        games = new OrderedMap<>();
        clients = new OrderedMap<>();
    }

    @Override
    public void putRegisteredPlayer(String clientID, YokelPlayer player) throws Exception {
        if(player == null) throw new Exception("Player was null.");
        if(clientID == null) throw new Exception("clientID was null.");
        String playerId = player.getPlayerId();
        registeredPlayers.put(playerId, player);
        clients.put(clientID,playerId);
    }

    @Override
    public void removeRegisteredPlayer(String clientID) throws Exception {
        //Remove user from all Seats
        //Remove user from all tables
        //Remove user from all Roomsv
        registeredPlayers.remove(getPlayerIdFromClient(clientID));
        clients.remove(clientID);
    }

    private String getPlayerIdFromClient(String clientId){
        return clients.get(clientId);
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

    @Override
    public boolean isClientRegistered(String clientId) {
        return clients.containsKey(clientId);
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

    @Override
    public void saveObject(Object object) {
        //Memory Storage objects are saved realtime
    }

    @Override
    public void commitTransactions() {
        //Memory Storage does not have a commit
    }

    @Override
    public void rollTransactions() {
        //Memory Storage does not have a rollback
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
    public <T> T getObject(Class<T> clazz, String name) {
        return null;
    }
}
