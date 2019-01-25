package net.asg.games.storage;

import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;

public class MemoryStorage implements StorageInterface {
    //<"room id", room object>
    private OrderedMap<String, YokelLounge> lounges;
    //<"player id", player object>
    private OrderedMap<String, YokelPlayer> registeredPlayers;

    @Override
    public void putTableState(YokelTable table) {

    }

    @Override
    public YokelTable getTableState() {
        return null;
    }

    @Override
    public void putRegisteredPlayer(YokelPlayer player) {
        if(registeredPlayers == null){
            registeredPlayers = new OrderedMap<>();
        }
    }

    @Override
    public YokelPlayer getRegisteredPlayer() {
        return null;
    }

    @Override
    public void putLounge(YokelLounge lounge) {
        if(lounges == null){
            lounges = new OrderedMap<>();
        }
    }

    @Override
    public YokelLounge getLounge() {
        return null;
    }
}
