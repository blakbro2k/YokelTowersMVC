package net.asg.games.storage;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;

public class YokelMemoryStorage extends MemoryStorage implements YokelStorageAdapter {
    //<"lounge name", room object>
    private OrderedMap<String, String> clients;
    //<"lounge name", room object>
    private OrderedMap<String, YokelLounge> lounges;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> registeredPlayers;
    //<"table name", gameManager>
    private OrderedMap<String, GameManager> games;

    public YokelMemoryStorage(){
        super();
        lounges = new OrderedMap<>();
        registeredPlayers = new OrderedMap<>();
        games = new OrderedMap<>();
        clients = new OrderedMap<>();
    }

    @Override
    public void registerPlayer(String clientID, YokelPlayer player) throws Exception {
        if(player == null) throw new Exception("Player was null.");
        if(clientID == null) throw new Exception("clientID was null.");
        String playerId = player.getPlayerId();
        registeredPlayers.put(playerId, player);
        clients.put(clientID,playerId);
        saveObject(player);
    }

    @Override
    public void unRegisterPlayer(String clientID) throws Exception {
        //Remove user from all Seats
        //Remove user from all tables
        //Remove user from all Rooms
        registeredPlayers.remove(getPlayerIdFromClient(clientID));
        clients.remove(clientID);
    }


    @Override
    public ObjectMap.Values<YokelPlayer> getAllRegisteredPlayers() {
        return registeredPlayers.values();
    }

    private String getPlayerIdFromClient(String clientId){
        return clients.get(clientId);
    }

    @Override
    public YokelPlayer getRegisteredPlayer(String id) {
        return registeredPlayers.get(id);
    }

    @Override
    public YokelPlayer getRegisteredPlayer(YokelPlayer player) {
        return new YokelPlayer();
    }

    @Override
    public boolean isClientRegistered(String clientId) {
        return clients.containsKey(clientId);
    }

    @Override
    public boolean isPlayerRegistered(String playerId) {
        return false;
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
